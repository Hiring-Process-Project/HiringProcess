package com.example.hiringProcess.Candidate;

import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.JobAd.JobAd;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Candidate {
    @Id
    @SequenceGenerator(
            name = "candidate_sequence",
            sequenceName = "candidate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "candidate_sequence"
    )
    private int id;

    private String firstName;
    private String lastName;
    private String email;
    private String info;
    private String decision;
    private String reasoning;

    // Σχέση Candidate με JobAd
    @ManyToOne
    @JoinColumn(name = "job_ad_id", referencedColumnName = "id")
    @JsonIgnore
    private JobAd jobAd;

    // Σχέση Candidate με InterviewReport
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "interviewReport_id", referencedColumnName = "id")
    @JsonIgnore
    private InterviewReport interviewReport;

    public Candidate() {}

    public Candidate(String firstName,String lastName, String email, String info, String decision, String reasoning) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.info = info;
        this.decision = decision;
        this.reasoning = reasoning;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", first name='" + firstName + '\'' +
                ", last name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", info='" + info + '\'' +
                ", decision='" + decision + '\'' +
                ", reasoning='" + reasoning + '\'' +
                ", jobAdId=" + (jobAd != null ? jobAd.getId() : "null") +
                '}';
    }

    //Getters and Setters

    public void setJobAd(JobAd jobAd) {
        this.jobAd=jobAd;
    }

    public int getId() {
       return id;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }
}




