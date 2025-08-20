package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // Skills ενός step (όπως έχεις)
    @Query("""
           select distinct s
           from Question q
           join q.skills s
           where q.step.id = :stepId
           """)
    List<Skill> findDistinctSkillsByStepId(Integer stepId);

    // Όλες οι ερωτήσεις ενός step (χωρίς σειρά)
    List<Question> findByStep_Id(Integer stepId);

    // ΝΕΟ: Όλες οι ερωτήσεις ενός step ταξινομημένες με βάση position ASC
    List<Question> findByStep_IdOrderByPositionAsc(Integer stepId);

    // ΝΕΟ: για κινήσεις/reorder μπορούμε να φορτώσουμε μαζικά
    List<Question> findByIdIn(List<Integer> ids);

    // ΝΕΟ: πόσες ερωτήσεις έχει ένα step (για assign "στο τέλος")
    long countByStep_Id(Integer stepId);
}
