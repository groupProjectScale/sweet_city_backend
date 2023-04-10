package com.example.controller;

import com.example.dto.SearchRangeDto;
import com.example.model.Activity;
import com.example.service.SearchServiceJpa;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchServiceJpa searchServiceJpa;

    public SearchController(SearchServiceJpa searchServiceJpa) {
        this.searchServiceJpa = searchServiceJpa;
    }

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @GetMapping("/activity")
    public ResponseEntity<List<Activity>> searchByActivityName(@RequestParam("q") String query) {
        if (query == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Activity> results = searchServiceJpa.searchByActivityName(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/activity-by-location")
    public CompletableFuture<ResponseEntity<List<Activity>>> searchByNearbyLocation(
            @RequestBody SearchRangeDto searchRangeDto) {
        if (searchRangeDto == null) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(null));
        }

        return searchServiceJpa
                .searchByNearbyLocation(
                        searchRangeDto.getLongitude(),
                        searchRangeDto.getLatitude(),
                        searchRangeDto.getRadius())
                .thenApply(ResponseEntity::ok)
                .exceptionally(
                        ex -> {
                            throw new RuntimeException(ex);
                        });
    }

    @GetMapping("tags")
    public ResponseEntity<List<Activity>> searchByTags(@RequestParam List<String> tags) {
        List<Activity> activities = searchServiceJpa.searchByTags(tags);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/get/tag/{tagId}")
    public ResponseEntity<List<Activity>> getActivityByTagId(@PathVariable UUID tagId) {
        List<Activity> res = searchServiceJpa.getActivityByTagId(tagId);
        return ResponseEntity.ok(res);
    }
}
