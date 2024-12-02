package com.example.hiringProcess.Evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    @Query("SELECT c FROM Candidate c WHERE c.name = ?1")
    Optional<Evaluation> findCandidateByName(String name);
}