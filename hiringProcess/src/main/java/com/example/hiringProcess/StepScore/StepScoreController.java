// src/main/java/com/example/hiringProcess/StepScore/StepScoreController.java
package com.example.hiringProcess.StepScore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"})
@RestController
@RequestMapping("/api/v1/step-scores")

public class StepScoreController {

    private final StepScoreService service;

    public StepScoreController(StepScoreService service) {
        this.service = service;
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<StepMetricsItemDTO>> getMetricsByCandidate(
            @RequestParam Integer candidateId,
            @RequestParam String stepIds
    ) {
        List<Integer> sids = Arrays.stream(stepIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .toList();

        return ResponseEntity.ok(service.getStepMetricsByCandidate(candidateId, sids));
    }

}
