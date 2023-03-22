package com.example.controllers;

import com.example.model.Activity;
import com.example.services.SearchService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping("tags")
    public ResponseEntity<List<Activity>> searchByTags(@RequestParam List<String> tags) {
        List<Activity> activities = searchService.searchByTags(tags);
        return ResponseEntity.ok(activities);
    }
}
