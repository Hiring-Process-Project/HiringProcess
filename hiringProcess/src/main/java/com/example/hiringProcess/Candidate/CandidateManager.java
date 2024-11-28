//package com.example.hiringProcess.Candidate;
//
//import com.example.hiringProcess.JobAd.JobAd;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//public class CandidateManager {
//
//    private final CandidateRepository candidateRepository;
//    private final JobAdRepository jobAdRepository;
//
//    public CandidateManager(CandidateRepository candidateRepository, JobAdRepository jobAdRepository) {
//        this.candidateRepository = candidateRepository;
//        this.jobAdRepository = jobAdRepository;
//    }
//
//    public Candidate addCandidateToJobAd(int jobAdId, Candidate candidate) {
//        JobAd jobAd = jobAdRepository.findById(jobAdId)
//                .orElseThrow(() -> new IllegalArgumentException("Job Ad not found"));
//
//        candidate.setJobAd(jobAd);
//        return candidateRepository.save(candidate);
//    }
//
//    public List<Candidate> getCandidatesForJobAd(int jobAdId) {
//        return candidateRepository.findByJobAdId(jobAdId);
//    }
//
//    public Candidate updateCandidate(Candidate candidate) {
//        if (candidateRepository.existsById(candidate.getId())) {
//            return candidateRepository.save(candidate);
//        }
//        throw new IllegalArgumentException("Candidate not found");
//    }
//
//    public void deleteCandidate(int candidateId) {
//        candidateRepository.deleteById(candidateId);
//    }
//}
//
