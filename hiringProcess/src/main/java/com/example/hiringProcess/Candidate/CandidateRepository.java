package com.example.hiringProcess.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c FROM Candidate c WHERE c.name = ?1")
 Optional<Candidate> findCandidateByName(String name);
}
