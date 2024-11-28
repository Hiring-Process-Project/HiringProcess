//package com.example.hiringProcess.Evaluation;
//
//import jakarta.persistence.*;
//import java.util.HashMap;
//import java.util.Map;
//
//@Entity
//public class Evaluation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private int candidateId;
//
//    private Map<Integer, Double> skillScores = new HashMap<>();
//
//    // Constructor
//    public Evaluation() {}
//
//    public Evaluation(int candidateId) {
//        this.candidateId = candidateId;
//    }
//
//    // Methods
//    public void assignSkillScore(int skillId, double score) {
//        skillScores.put(skillId, score);
//    }
//
//
//    // Methods
//    public void assignSkillScore(int skillId, double score) {
//        skillScores.put(skillId, score);
//    }
//
//    public Double getSkillScore(int skillId) {
//        return skillScores.get(skillId);
//    }
//
//    // Getters and Setters
//    public int getId() { return id; }
//    public int getCandidateId() { return candidateId; }
//    public int getJobAdId() { return jobAdId; }
//    public Map<Integer, Double> getSkillScores() { return skillScores; }
//}
//
