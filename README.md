# product-matcher-tg-bot

Telegram channel parser and product matcher with bot interface

This project monitors Telegram channels and turns unstructured messages into searchable product data.

## Features

- Collects messages from Telegram channels  
- Parses text into structured data (product name, price, additional info)  
- Stores and normalizes data  
- Allows users to search via a Telegram bot  
- Returns relevant matches based on user queries  

---

## Requirements

Make sure you have installed:

- Docker  
- Docker Compose  

---

## Configuration

The project consists of three microservices.

### 1. telegram-bot

Create `application.properties` from the example:

```bash
cp telegram-bot/src/main/resources/application-example.properties telegram-bot/src/main/resources/application.properties
```

Fill in the required values inside:

```
telegram-bot/src/main/resources/application.properties
```

You may also want to customize bot messages.
You can change them here:

```
telegram-bot/src/main/java/com/example/telegrambot/bot/ProductBot.java
```

---

### 2. telegram-listener

Create `application.properties` from the example:

```bash
cp telegram-listener/src/main/resources/application-example.properties telegram-listener/src/main/resources/application.properties
```

Fill in the required values inside:

```
telegram-listener/src/main/resources/application.properties
```

---

### 3. message-parser-matcher

Only rename the existing file:

```bash
cp message-parser-matcher/src/main/resources/application-example.properties message-parser-matcher/src/main/resources/application.properties
```

No additional changes required unless you want to customize it.

---

## Run with Docker Compose

Run the project from the root directory (`product-matcher-tg-bot`):

```bash
docker compose up --build
```

Or explicitly:

```bash
docker compose -f docker-compose.yml up --build
```

Run in background:

```bash
docker compose up -d --build
```

---

## Telegram Authorization

After the containers start, you must authorize the Telegram bot.

Attach to the bot container:

```bash
docker attach bot
```

Then enter:

```
1. Enter 'phone' command
2. Enter your phone number
3. Enter Telegram confirmation code
4. Enter password (if 2FA is enabled)
```

After successful login, the system will start processing messages.

---

## Useful Commands

Stop containers:

```bash
docker compose down
```

View logs:

```bash
docker compose logs -f
```
