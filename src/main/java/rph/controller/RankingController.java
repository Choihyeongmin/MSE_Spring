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

    private final RankingService rankingService;

    @GetMapping
    public List<RankingResponse> getAllRankings() {
        return rankingService.getAllRankings();
    }
    
    @GetMapping("/top/{count}")
    public List<RankingResponse> getTopRankings(@Valid @PathVariable int count) {
        return rankingService.getTopRankings(count);
    }
    
}
