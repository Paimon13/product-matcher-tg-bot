package com.example.messageparsermatcher.searchquery.service.impl;

import com.example.messageparsermatcher.kafka.KafkaProducer;
import com.example.messageparsermatcher.matcher.Matcher;
import com.example.messageparsermatcher.matcher.dto.MatchDto;
import com.example.messageparsermatcher.searchquery.dto.SearchQueryDto;
import com.example.messageparsermatcher.searchquery.model.SearchQuery;
import com.example.messageparsermatcher.searchquery.repository.SearchQueryRepository;
import com.example.messageparsermatcher.searchquery.service.SearchQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SearchQueryServiceImpl implements SearchQueryService {
    private final SearchQueryRepository searchQueryRepository;
    private final Matcher matcher;
    private final KafkaProducer kafkaProducer;

    public SearchQueryServiceImpl(SearchQueryRepository searchQueryRepository, Matcher matcher, KafkaProducer kafkaProducer) {
        this.searchQueryRepository = searchQueryRepository;
        this.matcher = matcher;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void saveSearchQuery(SearchQueryDto dto){
        SearchQuery searchQuery = new SearchQuery(
                dto.getChatId(),
                dto.getUserId(),
                dto.getProduct(),
                dto.getMaxPrice(),
                LocalDateTime.now()
        );

        searchQueryRepository.save(searchQuery);
    }
    @Override
    public void searchMatchOrSave(SearchQueryDto dto){
        saveSearchQuery(dto);

       MatchDto match =  matcher.searchMatchWithQuery(dto);

       if(match != null){
           kafkaProducer.send(match);
       }
    }
}
