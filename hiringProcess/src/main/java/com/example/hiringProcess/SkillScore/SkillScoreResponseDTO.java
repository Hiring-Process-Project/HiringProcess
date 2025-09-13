package com.example.hiringProcess.SkillScore;

import java.time.Instant;

public record SkillScoreResponseDTO(
        long id,
        int candidateId,
        int questionId,
        int skillId,
        Integer score,
        String comment,
        Instant ratedAt,
        String ratedBy,
        boolean created
) {}
