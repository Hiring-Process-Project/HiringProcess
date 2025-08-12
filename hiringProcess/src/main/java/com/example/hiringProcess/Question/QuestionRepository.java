package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query("""
           select distinct s
           from Question q
           join q.skills s
           where q.step.id = :stepId
           """)
    List<Skill> findDistinctSkillsByStepId(@Param("stepId") Integer stepId);

    // ΝΕΟ: όλες οι ερωτήσεις ενός step
    List<Question> findByStep_Id(Integer stepId);
}
