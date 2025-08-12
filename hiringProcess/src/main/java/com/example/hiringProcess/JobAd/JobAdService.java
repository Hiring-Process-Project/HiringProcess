package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Department.Department;
import com.example.hiringProcess.Department.DepartmentRepository;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Occupation.Occupation;
import com.example.hiringProcess.Occupation.OccupationRepository;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Skill.SkillDTO;
import com.example.hiringProcess.Skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobAdService {

    private final JobAdRepository jobAdRepository;
    private final JobAdMapper jobAdMapper;
    private final DepartmentRepository departmentRepository;
    private final OccupationRepository occupationRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public JobAdService(JobAdRepository jobAdRepository,
                        JobAdMapper jobAdMapper,
                        DepartmentRepository departmentRepository,
                        OccupationRepository occupationRepository,
                        SkillRepository skillRepository) {
        this.jobAdRepository = jobAdRepository;
        this.jobAdMapper = jobAdMapper;
        this.departmentRepository = departmentRepository;
        this.occupationRepository = occupationRepository;
        this.skillRepository = skillRepository;
    }

    public List<JobAd> getJobAds() {
        return jobAdRepository.findAll();
    }

    public Optional<JobAd> getJobAd(Integer jobAdId) {
        return jobAdRepository.findById(jobAdId);
    }

    public List<JobAdSummaryDTO> getJobAdSummaries() {
        return jobAdRepository.findAll().stream()
                .map(jobAdMapper::jobAdToSummaryDTO)
                .collect(Collectors.toList());
    }

    public Optional<JobAdDetailsDTO> getJobAdDetails(Integer jobAdId) {
        return jobAdRepository.findById(jobAdId)
                .map(jobAdMapper::jobAdToDetailsDTO);
    }

    @Transactional
    public void addNewJobAd(JobAd jobAd) {
        if (jobAd.getInterview() == null) {
            jobAd.setInterview(new Interview());
        }
        jobAdRepository.save(jobAd);
    }

    @Transactional
    public JobAd addNewJobAdByNames(JobAdCreateByNamesRequest req) {
        Department dept = departmentRepository.findByName(req.getDepartmentName())
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setName(req.getDepartmentName());
                    return departmentRepository.save(d);
                });

        Occupation occ = occupationRepository.findOccupationByTitle(req.getOccupationTitle())
                .orElseGet(() -> {
                    Occupation o = new Occupation();
                    o.setTitle(req.getOccupationTitle());
                    return occupationRepository.save(o);
                });

        JobAd ja = new JobAd();
        ja.setTittle(req.getTittle());
        ja.setDescription(req.getDescription());
        ja.setStatus(req.getStatus());
        ja.setPublishDate(req.getPublishDate());
        ja.setOccupation(occ);
        ja.setDepartments(Set.of(dept));
        ja.setInterview(new Interview()); // create empty interview

        return jobAdRepository.save(ja);
    }

    public void deleteJobAd(Integer jobAdId) {
        jobAdRepository.deleteById(jobAdId);
    }

    @Transactional
    public void updateJobAd(Integer jobAdId, JobAd updatedJobAd) {
        updatedJobAd.setId(jobAdId);
        if (updatedJobAd.getInterview() == null) {
            updatedJobAd.setInterview(new Interview());
        }
        jobAdRepository.save(updatedJobAd);
    }

    @Transactional
    public JobAd updateDetails(Integer jobAdId, JobAdUpdateDTO dto) {
        JobAd ja = jobAdRepository.findById(jobAdId).orElseThrow();

        if (dto.getDescription() != null) {
            ja.setDescription(dto.getDescription());
        }

        if (dto.getSkills() != null) {
            List<String> names = dto.getSkills().stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .distinct()
                    .toList();

            List<Skill> existing = names.isEmpty() ? List.of() : skillRepository.findByNameIn(names);
            Map<String, Skill> byName = existing.stream()
                    .collect(Collectors.toMap(Skill::getName, s -> s));

            Set<Skill> newSet = new HashSet<>();
            for (String n : names) {
                Skill s = byName.get(n);
                if (s == null) {
                    s = new Skill();
                    s.setName(n);
                    s = skillRepository.save(s);
                }
                newSet.add(s);
            }
            ja.setSkills(newSet);
        }

        return jobAdRepository.save(ja);
    }

    @Transactional
    public JobAd publish(Integer jobAdId) {
        JobAd ja = jobAdRepository.findById(jobAdId).orElseThrow();
        ja.setStatus("Published");
        if (ja.getPublishDate() == null) {
            ja.setPublishDate(LocalDate.now());
        }
        return jobAdRepository.save(ja);
    }

    public List<SkillDTO> getSkillsFromInterview(Integer jobAdId) {
        JobAd jobAd = jobAdRepository.findById(jobAdId)
                .orElseThrow(() -> new RuntimeException("JobAd not found"));

        Interview interview = jobAd.getInterview();
        if (interview == null) {
            return Collections.emptyList();
        }

        return interview.getSteps().stream()
                .flatMap(step -> step.getQuestions().stream())
                .flatMap(question -> question.getSkills().stream())
                .distinct()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
                .toList();
    }
}
