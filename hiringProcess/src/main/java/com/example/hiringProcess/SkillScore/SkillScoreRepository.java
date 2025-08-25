package com.example.hiringProcess.SkillScore;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.Skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillScoreRepository extends JpaRepository<SkillScore, Long> {

    Optional<SkillScore> findByCandidateAndQuestionAndSkill(
            Candidate candidate, Question question, Skill skill);

    List<SkillScore> findByCandidateIdAndQuestionId(int candidateId, int questionId);

    // 👇 προσθήκες για το πρόβλημά σου
    List<SkillScore> findByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId);

    Optional<SkillScore> findFirstByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId);

    void deleteByCandidateIdAndQuestionIdAndSkillId(
            int candidateId, int questionId, int skillId);

    @Query("""
       select ss.question.id as questionId,
              avg(ss.score)  as avgScore,
              count(ss)      as ratedSkills
       from SkillScore ss
       where ss.candidate.id = :candidateId
         and ss.question.step.interview.id = :interviewId
       group by ss.question.id
    """)
    List<Object[]> questionAverages(
            @Param("interviewId") int interviewId,
            @Param("candidateId") int candidateId
    );
}
