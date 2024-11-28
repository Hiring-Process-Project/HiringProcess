//package com.example.hiringProcess.Evaluation;
//
//import com.example.hiringProcess.Candidate.Candidate;
//import com.example.hiringProcess.Skill;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class EvaluationManager {
//
//    private final CandidateRepository candidateRepository;
//
//    public EvaluationManager(CandidateRepository candidateRepository) {
//        this.candidateRepository = candidateRepository;
//    }
//
//    public void assignScoreToSkill(Candidate candidate, Skill skill, int score) {
//        for (Skill candidateSkill : candidate.getSkills()) {
//            if (candidateSkill.getName().equals(skill.getName())) {
//                candidateSkill.setScore(score);
//                break;
//            }
//        }
//        candidateRepository.save(candidate);
//    }
//
//    public void markCandidateAsHired(Candidate candidate) {
//        candidate.setHired(true);
//        candidateRepository.save(candidate);
//    }
//}
//
