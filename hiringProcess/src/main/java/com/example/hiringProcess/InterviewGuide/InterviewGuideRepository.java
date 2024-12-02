package com.example.hiringProcess.InterviewGuide;

import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public class InterviewGuideRepository {
    @Query("SELECT c FROM Candidate c WHERE c.name = ?1")
    Optional<InterviewGuide> findCandidateByName(String name);
}
