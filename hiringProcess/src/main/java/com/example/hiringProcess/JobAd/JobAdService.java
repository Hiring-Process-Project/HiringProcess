package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

@Service
public class JobAdService {
    private final JobAdRepository jobAdRepository;

    @Autowired
    public JobAdService(JobAdRepository jobAdRepository) {
        this.jobAdRepository = jobAdRepository;
    }

    public List<JobAd> getJobAds(){
        return jobAdRepository.findAll();
    }

    public Optional<JobAd> getJobAd(Integer jobAdId) {
        return jobAdRepository.findById(jobAdId);
    }

    public void addNewJobAd(JobAd jobAd) {
        Optional<JobAd> jobAdByName = jobAdRepository.findJobAdById(jobAd.getId());
        if (jobAdByName.isPresent()) {
            throw new IllegalStateException("Id already taken");
        }
        jobAdRepository.save(jobAd);
    }

    public void deleteJobAd(Integer jobAdId) {
        boolean exists = jobAdRepository.existsById(jobAdId);

        if(!exists){
            throw  new IllegalStateException("jobAd with id" + jobAdId + "does not exists");
        }
        jobAdRepository.deleteById(jobAdId);
    }

    public List<JobAdSummaryDTO> getJobAdSummaries() {
        return jobAdRepository.findAll().stream()
                .flatMap(jobAd -> jobAd.getDepartments().stream().map(department ->
                        new JobAdSummaryDTO(
                                jobAd.getTittle(),
                                jobAd.getOccupation() != null ? jobAd.getOccupation().getTitle() : "Χωρίς Occupation",
                                department.getName()
                        )
                ))
                .toList();
    }


    @Transactional
    public void updateJobAd(Integer jobAdId, JobAd updatedJobAd) {
        JobAd existingJobAd = jobAdRepository.findById(jobAdId)
                .orElseThrow(() -> new IllegalStateException("JobAd with id " + jobAdId + " does not exist"));

        if (updatedJobAd.getDescription() != null) {
            existingJobAd.setDescription(updatedJobAd.getDescription());
        }

        if (updatedJobAd.getPublishDate() != null) {
            existingJobAd.setPublishDate(updatedJobAd.getPublishDate());
        }

        if (updatedJobAd.getStatus() != null) {
            existingJobAd.setStatus(updatedJobAd.getStatus());
        }

        if (updatedJobAd.getOccupation() != null) {
            existingJobAd.setOccupation(updatedJobAd.getOccupation());
        }

        if (updatedJobAd.getInterview() != null) {
            existingJobAd.setInterview(updatedJobAd.getInterview());
        }

        if (updatedJobAd.getDepartments() != null && !updatedJobAd.getDepartments().isEmpty()) {
            existingJobAd.setDepartments(updatedJobAd.getDepartments());
        }

        // Αν θέλεις να αντικαταστήσεις και τους υποψηφίους (προαιρετικό)
        if (updatedJobAd.getCandidates() != null) {
            existingJobAd.getCandidates().clear();
            for (Candidate candidate : updatedJobAd.getCandidates()) {
                existingJobAd.addCandidate(candidate);
            }
        }
    }

}
