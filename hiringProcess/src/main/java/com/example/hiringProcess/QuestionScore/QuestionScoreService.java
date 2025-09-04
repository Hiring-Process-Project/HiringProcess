package com.example.hiringProcess.QuestionScore;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
import com.example.hiringProcess.Question.QuestionRepository;
import com.example.hiringProcess.SkillScore.SkillScoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionScoreService {

    private final QuestionScoreRepository questionScoreRepository;
    private final QuestionRepository questionRepository;
    private final SkillScoreRepository skillScoreRepository;
    private final QuestionScoreMapper questionScoreMapper;

    // + CandidateRepository μόνο για convenience method (bridge από candidateId)
    private final CandidateRepository candidateRepository;

    @Autowired
    public QuestionScoreService(QuestionScoreRepository questionScoreRepository,
                                QuestionRepository questionRepository,
                                SkillScoreRepository skillScoreRepository,
                                QuestionScoreMapper questionMetricsItemMapper,
                                CandidateRepository candidateRepository) {
        this.questionScoreRepository = questionScoreRepository;
        this.questionRepository = questionRepository;
        this.skillScoreRepository = skillScoreRepository;
        this.questionScoreMapper = questionMetricsItemMapper;
        this.candidateRepository = candidateRepository;
    }

    /* ======= CRUD ======= */

    public List<QuestionScore> getAll() {
        return questionScoreRepository.findAll();
    }

    public Optional<QuestionScore> getById(Integer id) {
        return questionScoreRepository.findById(id);
    }

    public QuestionScore create(QuestionScore questionScore) {
        questionScore.setId(0); // force insert
        return questionScoreRepository.save(questionScore);
    }

    @Transactional
    public QuestionScore update(Integer id, QuestionScore updatedFields) {
        QuestionScore existing = questionScoreRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("QuestionScore with id " + id + " does not exist"));

        if (existing.getScore() != updatedFields.getScore()) {
            existing.setScore(updatedFields.getScore());
        }
        return existing;
    }

    @Transactional
    public void delete(Integer id) {
        QuestionScore existing = questionScoreRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("QuestionScore with id " + id + " does not exist"));

        existing.setQuestion(null);
        existing.setStepScore(null);
        questionScoreRepository.delete(existing);
    }

    /* ======= METRICS ======= */

    /**
     * Για κάθε questionId επιστρέφει:
     *  - totalSkills   : πλήθος DISTINCT skills της ερώτησης (Question -> question_skill)
     *  - ratedSkills   : πλήθος βαθμολογημένων skills για το συγκεκριμένο interviewReport
     *  - averageScore  : μέσος όρος score (0..100) ή null αν δεν υπάρχει καμία βαθμολογία
     *
     * ΠΗΓΗ δεδομένων: skill_score (ΟΧΙ question_score), ώστε τα analytics να “βλέπουν” ό,τι βαθμολογείς στα skills.
     */
    public List<QuestionMetricsItemDTO> getQuestionMetricsByReport(Integer interviewReportId,
                                                                   List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) return List.of();

        // 1) total skills ανά ερώτηση (distinct) από το QuestionRepository
        Map<Integer, Integer> totalByQ = new HashMap<>();
        for (Object[] row : questionRepository.countSkillsForQuestions(questionIds)) {
            Integer qid = ((Number) row[0]).intValue();
            int cnt     = ((Number) row[1]).intValue();
            totalByQ.put(qid, cnt);
        }

        // 2) ratedSkills & average από SkillScoreRepository (aggregate σε skill_score)
        Map<Integer, Integer> ratedByQ = new HashMap<>();
        Map<Integer, Double>  avgByQ   = new HashMap<>();
        if (interviewReportId != null) {
            List<Object[]> rows = skillScoreRepository.aggregateByReportAndQuestionIds(interviewReportId, questionIds);
            for (Object[] r : rows) {
                Integer qid  = ((Number) r[0]).intValue();
                Double  avg  = (r[1] == null) ? null : ((Number) r[1]).doubleValue();
                int     cnt  = ((Number) r[2]).intValue();
                ratedByQ.put(qid, cnt);
                if (avg != null) avgByQ.put(qid, avg);
            }
        }

        // 3) mapping σε DTO (με τον MapStruct mapper)
        List<QuestionMetricsItemDTO> out = new ArrayList<>(questionIds.size());
        for (Integer qid : questionIds) {
            int total = totalByQ.getOrDefault(qid, 0);
            int rated = ratedByQ.getOrDefault(qid, 0);

            Integer avgRounded = null;
            Double avg = avgByQ.get(qid);
            if (avg != null) avgRounded = (int) Math.round(avg);

            out.add(questionScoreMapper.toDto(qid, total, rated, avgRounded));
        }
        return out;
    }

    /**
     * Convenience: ίδια με το παραπάνω αλλά δέχεται candidateId.
     * Βρίσκουμε εδώ το interviewReportId και ξαναχρησιμοποιούμε το getQuestionMetricsByReport.
     * (Δεν αλλάζει τίποτα στη συμπεριφορά του controller σου—είναι απλώς βοηθητικό.)
     */
    public List<QuestionMetricsItemDTO> getQuestionMetricsByCandidate(Integer candidateId,
                                                                      List<Integer> questionIds) {
        if (candidateId == null) return List.of();
        Candidate cand = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException("Candidate " + candidateId + " does not exist"));

        Integer interviewReportId = (cand.getInterviewReport() != null)
                ? cand.getInterviewReport().getId()
                : null;

        return getQuestionMetricsByReport(interviewReportId, questionIds);
    }
}
