package com.example.hiringProcess.Skill;

import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Skill {
    @Id
    @SequenceGenerator(name = "skill_sequence", sequenceName = "skill_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_sequence")
    private int id;

    @Column(unique = true)
    private String name;

    private String title;
    private String escoId;

    // === Many-to-Many inverse προς Question ===
    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<JobAd> jobAds;

    public Skill() {}
    public Skill(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getEscoId() { return escoId; }
    public void setEscoId(String escoId) { this.escoId = escoId; }

    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public Set<JobAd> getJobAds() { return jobAds; }
    public void setJobAds(Set<JobAd> jobAds) { this.jobAds = jobAds; }
}
