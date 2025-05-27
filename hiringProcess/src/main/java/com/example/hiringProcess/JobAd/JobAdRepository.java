package com.example.hiringProcess.JobAd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

    @Repository
    public interface JobAdRepository extends JpaRepository<JobAd, Integer> {
        @Query("SELECT c FROM JobAd c WHERE c.id = ?1")
        Optional<JobAd> findJobAdById(Integer id);
    }


