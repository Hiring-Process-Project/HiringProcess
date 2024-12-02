package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JobAdService {
    private final JobAdRepository jobAdRepository;

    @Autowired
    public JobAdService(JobAdRepository jobAdRepository) {
        this.jobAdRepository = jobAdRepository;
    }

    @GetMapping
    public List<JobAd> getCandidates(){
        return jobAdRepository.findAll();
    }

    public void addNewCandidate(JobAd candidate) {
        Optional<JobAd> candidateByName = jobAdRepository.findCandidateByName(jobAd.getName());
        if (candidateByName.isPresent()){
            throw new IllegalStateException("name taken");
        }
        jobAdRepository.save(jobAd);
    }

    public void deleteCandidate(Integer candidateId) {
        boolean exists = jobAdRepository.existsById(jobAdId);

        if(!exists){
            throw  new IllegalStateException("candidate with id" + candidateId + "does not exists");
        }
        jobAdRepository.deleteById(candidateId);
    }

    @Transactional
    public void updateCandidate(Integer candidateId, String name) {
        Candidate candidate = interviewGuideRepository.findById(candidateId).orElseThrow(() -> new IllegalStateException(
                "candidate with id" + candidateId + "does not exists"));

        if(name != null && name.length()>0 && !Objects.equals(candidate.getName(),name)){
            jobAd.setName(name);
        }
    }
}
