package com.example.hiringProcess.QuestionScore;

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
    private final QuestionScoreMapper questionScoreMapper; // <-- προστέθηκε

    @Autowired
    public QuestionScoreService(QuestionScoreRepository questionScoreRepository,
                                QuestionRepository questionRepository,
                                SkillScoreRepository skillScoreRepository,
                                QuestionScoreMapper questionMetricsItemMapper) { // <-- προστέθηκε
        this.questionScoreRepository = questionScoreRepository;
        this.questionRepository = questionRepository;
        this.skillScoreRepository = skillScoreRepository;
        this.questionScoreMapper = questionMetricsItemMapper; // <-- προστέθηκε
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

    /* ======= METRICS by interviewReportId ======= */
    /**
     * Για κάθε questionId επιστρέφει:
     *  - totalSkills   : πλήθος distinct skills της ερώτησης (Question->skills)
     *  - ratedSkills   : πλήθος βαθμολογημένων skills στο συγκεκριμένο interviewReport
     *  - averageScore  : μέσος όρος score (0..100) ή null
     */
    public List<QuestionMetricsItemDTO> getQuestionMetricsByReport(Integer interviewReportId,
                                                                   List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) return List.of();

        // 1) total skills ανά question (distinct) από το QuestionRepository
        Map<Integer, Integer> totalByQ = new HashMap<>();
        for (Object[] row : questionRepository.countSkillsForQuestions(questionIds)) {
            Integer qid = ((Number) row[0]).intValue();
            int cnt     = ((Number) row[1]).intValue();
            totalByQ.put(qid, cnt);
        }

        // 2) ratedSkills & average από SkillScore
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

        // 3) χρήση mapper (όχι manual new)
        List<QuestionMetricsItemDTO> out = new ArrayList<>(questionIds.size());
        for (Integer qid : questionIds) {
            int total = totalByQ.getOrDefault(qid, 0);
            int rated = ratedByQ.getOrDefault(qid, 0);

            Integer avgRounded = null;
            Double avg = avgByQ.get(qid);
            if (avg != null) avgRounded = (int) Math.round(avg);

            out.add(questionScoreMapper.toDto(qid, total, rated, avgRounded)); // <-- μόνο αυτό αλλάζει
        }
        return out;
    }
}

