package com.example.hiringProcess.SkillScore;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.Skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SkillScoreRepository extends JpaRepository<SkillScore, Long> {

    Optional<SkillScore> findByCandidateAndQuestionAndSkill(
            Candidate candidate, Question question, Skill skill
    );

    List<SkillScore> findByCandidateIdAndQuestionId(int candidateId, int questionId);

    Optional<SkillScore> findByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId
    );

    List<SkillScore> findAllByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId
    );

    void deleteByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId
    );

    /* === Aggregations === */

    @Query("""
       select ss.question.id as questionId,
              avg(ss.score)   as avgScore,
              count(ss)       as ratedSkills
       from SkillScore ss
       where ss.candidate.id = :candidateId
         and ss.question.step.interview.id = :interviewId
         and ss.score is not null
         and ss.skill member of ss.question.skills
       group by ss.question.id
    """)
    List<Object[]> questionAverages(
            @Param("interviewId") int interviewId,
            @Param("candidateId") int candidateId
    );

    @Query("""
       select ss.question.id as questionId,
              avg(ss.score)   as avgScore,
              count(ss)       as ratedSkills
       from SkillScore ss
       where ss.candidate.interviewReport.id = :reportId
         and ss.question.id in :qids
         and ss.score is not null
         and ss.skill member of ss.question.skills
       group by ss.question.id
    """)
    List<Object[]> aggregateByReportAndQuestionIds(
            @Param("reportId") Integer reportId,
            @Param("qids") Collection<Integer> qids
    );

    // SkillScoreRepository.java
    @Query("""
   SELECT ss.question.id, COUNT(ss.score), AVG(ss.score)
   FROM SkillScore ss
   WHERE ss.candidate.id = :candidateId
     AND ss.question.id IN :qids
     AND ss.score IS NOT NULL
   GROUP BY ss.question.id
""")
    List<Object[]> aggregateByCandidateAndQuestionIdsRaw(
            @Param("candidateId") Integer candidateId,
            @Param("qids") Collection<Integer> qids
    );

}
