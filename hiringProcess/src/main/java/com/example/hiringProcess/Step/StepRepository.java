package com.example.hiringProcess.Step;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepRepository extends JpaRepository<Step, Integer> {

    List<Step> findByInterviewIdOrderByPositionAsc(int interviewId);

    @Query("select coalesce(max(s.position), -1) from Step s where s.interview.id = :interviewId")
    int findMaxPositionByInterviewId(@Param("interviewId") int interviewId);

    Optional<Step> findByInterviewIdAndPosition(int interviewId, int position);
}
