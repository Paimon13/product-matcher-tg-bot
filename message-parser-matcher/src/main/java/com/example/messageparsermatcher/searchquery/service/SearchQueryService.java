package com.example.messageparsermatcher.searchquery.service;

import com.example.messageparsermatcher.searchquery.dto.SearchQueryDto;
import org.springframework.stereotype.Service;

@Service
public interface SearchQueryService {
    void saveSearchQuery(SearchQueryDto dto);

    void searchMatchOrSave(SearchQueryDto dto);
}
