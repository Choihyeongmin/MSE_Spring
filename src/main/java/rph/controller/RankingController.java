package rph.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rph.dto.ranking.RankingResponse;
import rph.service.ranking.RankingService;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService; // Service for ranking operations

    @GetMapping
    public List<RankingResponse> getAllRankings() {
        // Get all rankings
        return rankingService.getAllRankings();
    }

    @GetMapping("/top/{count}")
    public List<RankingResponse> getTopRankings(@Valid @PathVariable int count) {
        // Get top 'count' rankings
        return rankingService.getTopRankings(count);
    }
}
