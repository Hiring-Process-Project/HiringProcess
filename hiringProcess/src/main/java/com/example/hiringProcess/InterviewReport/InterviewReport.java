package com.example.hiringProcess.InterviewReport;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InterviewReport {
    @Id
    @SequenceGenerator(
            name = "interviewReport_sequence",
            sequenceName = "interviewReport_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interviewReport_sequence"
    )
    private int id;

    //Σχεση interviewReport με Candidate
    @OneToOne(mappedBy = "interviewReport")
    @JsonIgnore
    Candidate candidate;

    // Σχέση InterviewReport με stepResults
    @OneToMany(mappedBy = "interviewReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepResults> stepResults = new ArrayList<>();



    // Σχέση InterviewReport με Interview
    @OneToOne
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    @JsonIgnore
    Interview interview;

    public InterviewReport() {
    }

}
