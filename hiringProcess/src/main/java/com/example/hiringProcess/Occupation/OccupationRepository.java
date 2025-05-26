package com.example.hiringProcess.Occupation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

    @Repository
    public interface OccupationRepository extends JpaRepository<Occupation, Integer> {
        @Query("SELECT c FROM Occupation c WHERE c.title = ?1")
        Optional<Occupation> findOccupationByTitle(String title);
    }

