package com.example.hiringProcess.SkillScore;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.Question.QuestionRepository;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillScoreService {

    private final SkillScoreRepository skillScoreRepository;
    private final CandidateRepository candidateRepository;
    private final QuestionRepository questionRepository;
    private final SkillRepository skillRepository;

    /**
     * Upsert βαθμολογίας για (candidate, question, skill).
     * Επιστρέφει DTO με flag created=true όταν έγινε insert, αλλιώς false (update).
     */
    @Transactional
    public SkillScoreResponseDTO upsert(SkillScoreUpsertRequestDTO req) {
        // validations / lookups
        Candidate cand = candidateRepository.findById(req.candidateId())
                .orElseThrow(() -> new IllegalStateException("Candidate " + req.candidateId() + " not found"));
        Question q = questionRepository.findById(req.questionId())
                .orElseThrow(() -> new IllegalStateException("Question " + req.questionId() + " not found"));
        Skill sk = skillRepository.findById(req.skillId())
                .orElseThrow(() -> new IllegalStateException("Skill " + req.skillId() + " not found"));

        if (req.score() == null)
            throw new IllegalArgumentException("score is required");
        if (req.score() < 0 || req.score() > 100)
            throw new IllegalArgumentException("score must be between 0 and 100");

        // find existing
        SkillScore entity = skillScoreRepository
                .findByCandidateAndQuestionAndSkill(cand, q, sk)
                .orElse(null);

        boolean created = false;
        if (entity == null) {
            entity = new SkillScore();
            entity.setCandidate(cand);
            entity.setQuestion(q);
            entity.setSkill(sk);
            created = true;
        }

        // set values
        entity.setScore(req.score());
        entity.setComment(req.comment());
        entity.setRatedBy(req.ratedBy());
        entity.setRatedAt(Instant.now());

        // save
        SkillScore saved = skillScoreRepository.save(entity);

        // map response
        return new SkillScoreResponseDTO(
                saved.getId(),
                saved.getCandidate().getId(),
                saved.getQuestion().getId(),
                saved.getSkill().getId(),
                saved.getScore(),
                saved.getComment(),
                saved.getRatedAt(),
                saved.getRatedBy(),
                created
        );
    }

    /** Διαγραφή μίας εγγραφής με id. */
    @Transactional
    public void deleteById(long id) {
        if (skillScoreRepository.existsById(id)) {
            skillScoreRepository.deleteById(id);
        }
    }

    /** Διαγραφή βάσει tuple (candidate, question, skill). */
    @Transactional
    public void deleteTuple(int candidateId, int questionId, int skillId) {
        List<SkillScore> list =
                skillScoreRepository.findByCandidateIdAndQuestionIdAndSkillId(candidateId, questionId, skillId);
        if (!list.isEmpty()) {
            skillScoreRepository.deleteAll(list);
        }
    }

    /** Λίστα βαθμολογιών για όλα τα skills μιας ερώτησης για συγκεκριμένο υποψήφιο. */
    @Transactional(readOnly = true)
    public List<SkillScoreResponseDTO> listForCandidateQuestion(int candidateId, int questionId) {
        return skillScoreRepository.findByCandidateIdAndQuestionId(candidateId, questionId)
                .stream()
                .map(s -> new SkillScoreResponseDTO(
                        s.getId(),
                        s.getCandidate().getId(),
                        s.getQuestion().getId(),
                        s.getSkill().getId(),
                        s.getScore(),
                        s.getComment(),
                        s.getRatedAt(),
                        s.getRatedBy(),
                        false   // για λίστα δεν μας νοιάζει το created flag
                ))
                .toList();
    }
}
