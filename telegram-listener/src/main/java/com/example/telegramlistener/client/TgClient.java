package com.example.telegramlistener.client;


import com.example.telegramlistener.client.dto.TgMessageDto;
import com.example.telegramlistener.kafka.KafkaProducer;
import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class TgClient {

    @Value("${telegram.api-id}")
    private int apiId;

    @Value("${telegram.api-hash}")
    private String apiHash;

    @Value("${telegram.chat-id}")
    private List<Long> chatIds;

    private final KafkaProducer kafkaProducer;

    private SimpleTelegramClient client;
    private SimpleTelegramClientFactory factory;

    public TgClient(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostConstruct
    public void start() throws Exception {
        Init.init();
        Log.setVerbosityLevel(1);

        APIToken apiToken = new APIToken(apiId, apiHash);
        TDLibSettings settings = TDLibSettings.create(apiToken);

        Path sessionRoot = Paths.get("/app/tdlight-session");
        settings.setDatabaseDirectoryPath(sessionRoot.resolve("db"));
        settings.setDownloadedFilesDirectoryPath(sessionRoot.resolve("downloads"));

        factory = new SimpleTelegramClientFactory();
        SimpleTelegramClientBuilder builder = factory.builder(settings);

        builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, update -> {
            System.out.println("AUTH STATE -> " + update.authorizationState.getClass().getSimpleName());

            if (update.authorizationState instanceof TdApi.AuthorizationStateReady) {
                System.out.println("Authorized");
            }
        });

        builder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::handleNewMessage);

        client = builder.build(AuthenticationSupplier.consoleLogin());

        System.out.println("TG client is running");
    }

    @PreDestroy
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
        if (factory != null) {
            factory.close();
        }
    }

    private void handleNewMessage(TdApi.UpdateNewMessage update) {
        TdApi.Message msg = update.message;

        if (!chatIds.contains(msg.chatId)) {
            return;
        }

        String text = extractText(msg);
        if (text == null || text.isBlank()) {
            return;
        }

        client.send(new TdApi.GetChat(msg.chatId))
                .whenComplete((chat, error) -> {
                    if (error != null || chat == null) {
                        publish(msg.chatId, msg.id >> 20, "unknown", text, msg.date);
                        return;
                    }

                    if (chat.type instanceof TdApi.ChatTypeSupergroup supergroupType) {
                        long supergroupId = supergroupType.supergroupId;

                        client.send(new TdApi.GetSupergroup(supergroupId))
                                .whenComplete((supergroup, err) ->
                                        publish(msg.chatId, msg.id >> 20, extractUsername(supergroup), text, msg.date)
                                );
                    } else {
                        publish(msg.chatId, msg.id >> 20, chat.title != null ? chat.title : "not_channel", text, msg.date);
                    }
                });
    }

    private void publish(long chatId, long messageId, String title, String text, int date) {
        LocalDateTime dateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        TgMessageDto dto = new TgMessageDto(chatId, messageId, title, text, dateTime);

        print(dto);
        kafkaProducer.send(dto);
    }

    private String extractText(TdApi.Message msg) {
        if (msg.content instanceof TdApi.MessageText textContent) {
            return textContent.text.text;
        }

        if (msg.content instanceof TdApi.MessagePhoto photoContent && photoContent.caption != null) {
            return photoContent.caption.text;
        }

        if (msg.content instanceof TdApi.MessageVideo videoContent && videoContent.caption != null) {
            return videoContent.caption.text;
        }

        return null;
    }

    private String extractUsername(TdApi.Supergroup supergroup) {
        if (supergroup != null
                && supergroup.usernames != null
                && supergroup.usernames.activeUsernames != null
                && supergroup.usernames.activeUsernames.length > 0) {
            return "@" + supergroup.usernames.activeUsernames[0];
        }
        return "no_username";
    }

    private void print(TgMessageDto dto) {
        System.out.println("------");
        System.out.println("ChannelId: " + dto.getChatId());
        System.out.println("MessageId: " + dto.getMessageId());
        System.out.println("Channel: " + dto.getChannel());
        System.out.println("Text: " + dto.getText());
        System.out.println("Date: " + dto.getDate());
        System.out.println("------");
    }
}