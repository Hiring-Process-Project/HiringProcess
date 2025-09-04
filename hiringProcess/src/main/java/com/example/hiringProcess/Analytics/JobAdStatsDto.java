package com.example.hiringProcess.Analytics;

import java.util.List;

public class JobAdStatsDto {
    private double approvalRate;
    private double rejectionRate;
    private double avgCandidateScore;

    private List<ScoreBucketDto> scoreDistribution;
    private List<StepAvgDto>     stepAverages;
    private List<QuestionAvgDto> questionDifficulty;
    private List<SkillAvgDto>    skillDifficulty;

    private long totalCandidates;

    // NEW: true όταν υπάρχει έστω ένας candidate με status "Hired"
    private boolean complete;

    public JobAdStatsDto() {}

    public JobAdStatsDto(
            double approvalRate,
            double rejectionRate,
            double avgCandidateScore,
            List<ScoreBucketDto> scoreDistribution,
            List<StepAvgDto> stepAverages,
            List<QuestionAvgDto> questionDifficulty,
            List<SkillAvgDto> skillDifficulty,
            long totalCandidates,
            boolean complete
    ) {
        this.approvalRate = approvalRate;
        this.rejectionRate = rejectionRate;
        this.avgCandidateScore = avgCandidateScore;
        this.scoreDistribution = scoreDistribution;
        this.stepAverages = stepAverages;
        this.questionDifficulty = questionDifficulty;
        this.skillDifficulty = skillDifficulty;
        this.totalCandidates = totalCandidates;
        this.complete = complete;
    }

    public double getApprovalRate() { return approvalRate; }
    public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }

    public double getRejectionRate() { return rejectionRate; }
    public void setRejectionRate(double rejectionRate) { this.rejectionRate = rejectionRate; }

    public double getAvgCandidateScore() { return avgCandidateScore; }
    public void setAvgCandidateScore(double avgCandidateScore) { this.avgCandidateScore = avgCandidateScore; }

    public List<ScoreBucketDto> getScoreDistribution() { return scoreDistribution; }
    public void setScoreDistribution(List<ScoreBucketDto> scoreDistribution) { this.scoreDistribution = scoreDistribution; }

    public List<StepAvgDto> getStepAverages() { return stepAverages; }
    public void setStepAverages(List<StepAvgDto> stepAverages) { this.stepAverages = stepAverages; }

    public List<QuestionAvgDto> getQuestionDifficulty() { return questionDifficulty; }
    public void setQuestionDifficulty(List<QuestionAvgDto> questionDifficulty) { this.questionDifficulty = questionDifficulty; }

    public List<SkillAvgDto> getSkillDifficulty() { return skillDifficulty; }
    public void setSkillDifficulty(List<SkillAvgDto> skillDifficulty) { this.skillDifficulty = skillDifficulty; }

    public long getTotalCandidates() { return totalCandidates; }
    public void setTotalCandidates(long totalCandidates) { this.totalCandidates = totalCandidates; }

    // boolean accessor για σωστό JSON ("complete": true/false)
    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }
}
