package com.example.hiringProcess.Analytics;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

@Service
public class AnalyticsService {

    private final AnalyticsRepository repo;

    public AnalyticsService(AnalyticsRepository repo) {
        this.repo = repo;
    }

    /* ------------------------ Helpers ------------------------ */

    private double percent(long part, long total) {
        if (total == 0) return 0.0;
        return BigDecimal.valueOf(part * 100.0 / total)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private double round1(double v) {
        return BigDecimal.valueOf(v)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /* -------------------- Organization scope -------------------- */

    public OrganizationStatsDto getOrganizationStats(int orgId) {
        long total    = repo.countTotalCandidatesByOrg(orgId);
        long approved = repo.countCandidatesByStatusOrg(orgId, "Approved");
        long rejected = repo.countCandidatesByStatusOrg(orgId, "Rejected");
        long hires    = repo.countCandidatesByStatusOrg(orgId, "Hired");

        double approvalRate  = percent(approved, total);
        double rejectionRate = percent(rejected, total);
        double hireRate      = percent(hires, total);

        double avgCandPerJobAd = round1(repo.avgCandidatesPerJobAdOrg(orgId));


        List<SkillAvgDto> top5  = repo.topSkillsByOrg(orgId, 5);
        List<SkillAvgDto> weak5 = repo.weakestSkillsByOrg(orgId, 5);

        List<Double> scores = repo.candidateAvgScoresByOrg(orgId);
        long[] buckets = new long[10];
        for (Double s : scores) {
            if (s == null) continue;
            double v = Math.max(0.0, Math.min(10.0, s));
            int idx = (int) Math.floor(v);
            if (idx < 0) idx = 0; if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        List<ScoreBucketDto> distribution = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int from = i * 10;
            int to   = (i == 9) ? 100 : (i * 10 + 9);
            distribution.add(new ScoreBucketDto(from, to, buckets[i]));
        }

        return new OrganizationStatsDto(
                approvalRate,
                rejectionRate,
                hireRate,
                hires,
                total,
                top5,
                weak5,
                distribution,
                avgCandPerJobAd
        );
    }

    /* --------------------- Department scope --------------------- */

    public DepartmentStatsDto getDepartmentStats(int deptId) {
        long total    = repo.countTotalCandidatesByDept(deptId);
        long approved = repo.countCandidatesByStatusDept(deptId, "Approved");
        long rejected = repo.countCandidatesByStatusDept(deptId, "Rejected");
        long hires    = repo.countHiresByDept(deptId);

        double approvalRate  = percent(approved, total);
        double rejectionRate = percent(rejected, total);
        double hireRate      = percent(hires, total);

        double avgCandPerJob = repo.avgCandidatesPerJobAdInDept(deptId);

        // === ΝΕΟ: φέρνουμε τους μέσους όρους (0..10) και κάνουμε bucketing εδώ ===
        List<Double> scores = repo.candidateAvgScoresByDept(deptId); // <-- δείτε repo method παρακάτω
        long[] buckets = new long[10];
        for (Double s : scores) {
            if (s == null) continue;
            double v = Math.max(0.0, Math.min(10.0, s));
            int idx = (int) Math.floor(v);
            if (idx < 0) idx = 0;
            if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        List<BucketDto> distribution = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int from = i * 10;
            int to   = (i == 9) ? 100 : (i * 10 + 9);
            distribution.add(new BucketDto(from, to, buckets[i]));
        }

        List<OccupationAvgDto> occDiff = repo.occupationDifficultyByDept(deptId);

        return new DepartmentStatsDto(
                approvalRate,
                rejectionRate,
                round1(avgCandPerJob),
                distribution,
                occDiff,
                hireRate,
                hires,
                total
        );
    }


    /* ---------------------- Occupation scope --------------------- */

    public OccupationStatsDto getOccupationStats(int deptId, int occId) {
        long total    = repo.countTotalCandidatesByDeptOcc(deptId, occId);
        long approved = repo.countCandidatesByStatusDeptOcc(deptId, occId, "Approved");
        long rejected = repo.countCandidatesByStatusDeptOcc(deptId, occId, "Rejected");
        long hires = repo.countCandidatesByStatusDeptOcc(deptId, occId, "Hired");

        double approvalRate  = percent(approved, total);
        double rejectionRate = percent(rejected, total);
        double hireRate      = percent(hires, total);

        double candPerJobAd  = repo.avgCandidatesPerJobAdDeptOcc(deptId, occId);

        List<Double> scores = repo.candidateAvgScoresByDeptOcc(deptId, occId);
        long[] buckets = new long[10];
        for (Double s : scores) {
            if (s == null) continue;
            double v = Math.max(0.0, Math.min(10.0, s));
            int idx = (int)Math.floor(v);
            if (idx < 0) idx = 0; if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        List<ScoreBucketDto> dist = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int from = i * 10;
            int to   = (i == 9) ? 100 : (i * 10 + 9);
            dist.add(new ScoreBucketDto(from, to, buckets[i]));
        }

        // συνολικοί υποψήφιοι για το συγκεκριμένο dept+occ
        long totalCandidates = total; // το 'total' το έχεις ήδη παραπάνω

        // ranking: Job Ad Difficulty (lower = harder)
        List<JobAdAvgDto> jobAdDiff = repo.jobAdDifficultyByDeptOcc(deptId, occId);


        // Προσθέτουμε το hireRate στο DTO
        return new OccupationStatsDto(
                approvalRate,
                rejectionRate,
                hireRate,
                hires,
                round1(candPerJobAd),
                dist,
                totalCandidates,
                jobAdDiff
        );

    }

    /* ------------------------ Job Ad scope ------------------------ */

    public JobAdStatsDto getJobAdStats(int jobAdId) {
        long total    = repo.countTotalCandidatesByJobAd(jobAdId);
        long approved = repo.countCandidatesByStatusJobAd(jobAdId, "Approved");
        long rejected = repo.countCandidatesByStatusJobAd(jobAdId, "Rejected");

        // ΝΕΟ: hires & hireRate
        long hires    = repo.countCandidatesByStatusJobAd(jobAdId, "Hired");
        double hireRate = percent(hires, total);

        double approvalRate  = percent(approved, total);
        double rejectionRate = percent(rejected, total);

        boolean complete = hires > 0;

        List<Double> candAvgs = repo.candidateAvgScoresByJobAd(jobAdId);

        double avgCandidateScore = round1(
                candAvgs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
        );

        double maxSeen = candAvgs.stream().filter(v -> v != null).mapToDouble(Double::doubleValue).max().orElse(0.0);
        boolean isHundredScale = maxSeen > 10.0;

        long[] bucketsArr = new long[10];
        for (Double s : candAvgs) {
            if (s == null) continue;
            double v100 = isHundredScale ? s : (s * 10.0);
            double clamped = Math.max(0.0, Math.min(100.0, v100));
            int idx = (int)(clamped / 10.0);
            if (idx > 9) idx = 9;
            bucketsArr[idx]++;
        }

        List<ScoreBucketDto> distribution = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int from = i * 10;
            int to   = (i == 9) ? 100 : (i * 10 + 9);
            distribution.add(new ScoreBucketDto(from, to, bucketsArr[i]));
        }

        List<StepAvgDto>      stepAvg       = repo.stepDifficultyByJobAd(jobAdId);
        List<QuestionAvgDto>  questionDiff  = repo.questionDifficultyByJobAd(jobAdId);
        List<SkillAvgDto>     skillDiff     = repo.skillDifficultyByJobAd(jobAdId);

        // ΝΕΟΣ constructor με hireRate & hireCount
        return new JobAdStatsDto(
                approvalRate,
                rejectionRate,
                hireRate,
                hires,
                avgCandidateScore,
                distribution,
                stepAvg,
                questionDiff,
                skillDiff,
                total,
                complete
        );
    }


    /* ------------------------ Candidate scope ------------------------ */

    public CandidateStatsDto getCandidateStats(int candidateId) {
        double overall = round1(repo.candidateOverallScore(candidateId));

        var stepScores      = repo.candidateStepScores(candidateId);
        var questionScores  = repo.candidateQuestionScores(candidateId);
        var skillScores     = repo.candidateSkillScores(candidateId);

        var strengths = repo.topSkillsForCandidate(candidateId, 3);
        var weaknesses = repo.weakSkillsForCandidate(candidateId, 3);

        return new CandidateStatsDto(
                overall,
                stepScores,
                questionScores,
                skillScores,
                strengths,
                weaknesses
        );
    }
    // Λίστα υποψηφίων για job ad (για το αριστερό box στο Candidates tab)
    public List<CandidateLiteDto> getJobAdCandidates(int jobAdId) {
        return repo.candidatesByJobAd(jobAdId);
    }

    /* ------------------------ Step scope ------------------------ */

    public StepStatsDto getStepStats(int jobAdId, int stepId) {
        // 1) Avg step score (όλων των ερωτήσεων στο step, σε όλους τους υποψήφιους)
        double avgStepScore = round1(repo.avgStepScoreForJobAdStep(jobAdId, stepId));

        // 2) Candidate step averages (για pass rate + histogram)
        List<Double> avgs = repo.candidateStepAverages(jobAdId, stepId);

        // Pass rate: % των υποψηφίων με avg >= 5.0 (50%)
        long total = avgs.size();
        long passes = avgs.stream().filter(a -> a != null && a >= 5.0).count();
        double passRate = percent(passes, total);

        // Histogram 0..100 (deciles 0..9) από candidate avg (0..10)
        long[] buckets = new long[10];
        for (Double a : avgs) {
            if (a == null) continue;
            double v = Math.max(0.0, Math.min(10.0, a));
            int idx = (int)Math.floor(v);
            if (idx < 0) idx = 0;
            if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        List<ScoreBucketDto> distribution = new java.util.ArrayList<>(10);
        for (int b = 0; b < 10; b++) {
            int from = b * 10;
            int to   = (b == 9) ? 100 : (b + 1) * 10;
            distribution.add(new ScoreBucketDto(from, to, buckets[b]));
        }

        var questionRanking = repo.questionRankingByStep(jobAdId, stepId);
        var skillRanking    = repo.skillRankingByStep(jobAdId, stepId);

        return new StepStatsDto(
                round1(passRate),
                avgStepScore,
                distribution,
                questionRanking,
                skillRanking
        );
    }

    public java.util.List<StepLiteDto> getStepsForJobAd(int jobAdId) {
        return repo.stepsForJobAd(jobAdId);
    }

    /* ------------------------ Question scope ------------------------ */

    public QuestionStatsDto getQuestionStats(int jobAdId, int questionId) {
        double avg = round1(repo.avgScoreForQuestionInJobAd(jobAdId, questionId));

        List<Double> avgs = repo.candidateQuestionAverages(jobAdId, questionId);
        long total = avgs.size();
        long passes = avgs.stream().filter(a -> a != null && a >= 5.0).count();
        double passRate = percent(passes, total);

        long[] buckets = new long[10];
        for (Double a : avgs) {
            if (a == null) continue;
            double v = Math.max(0.0, Math.min(10.0, a));
            int idx = (int)Math.floor(v);
            if (idx < 0) idx = 0;
            if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        List<ScoreBucketDto> distribution = new java.util.ArrayList<>(10);
        for (int b = 0; b < 10; b++) {
            int from = b * 10;
            int to   = (b == 9) ? 100 : (b + 1) * 10;
            distribution.add(new ScoreBucketDto(from, to, buckets[b]));
        }

        // ΛΙΣΤΑ δεξιοτήτων ταξινομημένη φθίνουσα (από το repo)
        List<SkillAvgDto> skillRanking = repo.skillAveragesForQuestion(jobAdId, questionId);

        return new QuestionStatsDto(
                avg,
                round1(passRate),
                distribution,
                skillRanking
        );
    }

    public java.util.List<QuestionLiteDto> getQuestionsForJobAdStep(int jobAdId, int stepId) {
        return repo.questionsForJobAdStep(jobAdId, stepId);
    }

    public java.util.List<SkillLiteDto> getSkillsForQuestion(int questionId) {
        return repo.skillsForQuestion(questionId);
    }

    /* ------------------------ Skill scope ------------------------ */

    public SkillStatsDto getSkillStats(int skillId) {
        // 1) Avg skill score (0..10)
        double avg = round1(repo.avgScoreForSkill(skillId));

        // 2) Candidate averages για pass rate + histogram
        var avgs = repo.candidateSkillAverages(skillId);
        long total = avgs.size();
        long passes = avgs.stream().filter(a -> a != null && a >= 5.0).count();
        double passRate = percent(passes, total);

        // 3) Histogram 0..100 από 0..10 (deciles)
        long[] buckets = new long[10];
        for (Double a : avgs) {
            if (a == null) continue;
            double v = Math.max(0.0, Math.min(10.0, a));
            int idx = (int) Math.floor(v);
            if (idx < 0) idx = 0;
            if (idx > 9) idx = 9;
            buckets[idx]++;
        }
        java.util.List<ScoreBucketDto> distribution = new java.util.ArrayList<>(10);
        for (int b = 0; b < 10; b++) {
            int from = b * 10;
            int to   = (b == 9) ? 100 : (b + 1) * 10;
            distribution.add(new ScoreBucketDto(from, to, buckets[b]));
        }

        return new SkillStatsDto(avg, round1(passRate), distribution);
    }
}
