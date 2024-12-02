package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.InterviewGuide.InterviewGuide;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public class JobAdRepository {
    @Query("SELECT c FROM Candidate c WHERE c.name = ?1")
    Optional<InterviewGuide> findCandidateByName(String name);
}
