package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class JobAd {
    @Id
    @SequenceGenerator(
            name = "jobAd_sequence",
            sequenceName = "jobAd_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "jobAd_sequence"
    )
    private int id;

    private String title;
    private String description;
    private LocalDate date;
    private String status;

    @OneToOne(cascade = CascadeType.ALL) // Η owning πλευρά
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    private Interview interview;

    // Σχέση JobAd με Candidate (OneToMany)
    @OneToMany(mappedBy = "jobAd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Candidate> candidates = new ArrayList<>();

    // Σχέση JobAd με Skill (OneToMany)
    @OneToMany(mappedBy = "jobAd", cascade = CascadeType.ALL, orphanRemoval = true) // Σωστό mapping
    private List<Skill> skills = new ArrayList<>();



    public JobAd() {}

    public JobAd(String title, String description, LocalDate date, String status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    // Getters και Setters
    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }
    public int getId() {
        return id;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "JobAd{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                ", candidate="+candidatesToString()+
                ", skill="+skillsToString()+
                '}';
    }


    public String getTitle() {
        return title;
    }

    // Προσθήκη candidate στη λίστα (βοηθητική μέθοδος)
    public void addCandidate(Candidate candidate) {
        if (candidate != null) {
            candidates.add(candidate);
            candidate.setJobAd(this);
        }
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των steps
    private String candidatesToString() {
        if ( candidates == null || candidates.isEmpty()) {
            return "[]";
        }
        return candidates.stream()
                .map(candidate -> "{id=" + candidate.getId() + ", name=" + candidate.getName() + "}")
                .toList()
                .toString();
    }

    // Προσθήκη skill στη λίστα (βοηθητική μέθοδος)
    public void addSkill(Skill skill) {
        if (skill != null) {
            skills.add(skill);
            skill.setJobAd(this);
        }
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των skills
    private String skillsToString() {
        if ( skills == null || skills.isEmpty()) {
            return "[]";
        }
        return skills.stream()
                .map(skill -> "{id=" + skill.getId() + ", name=" + skill.getName() + "}")
                .toList()
                .toString();
    }
}

