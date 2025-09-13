//package com.example.hiringProcess.InterviewReport;
//
//import com.example.hiringProcess.Candidate.Candidate;
//import com.example.hiringProcess.Interview.Interview;
//import com.example.hiringProcess.StepScore.StepScore;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class InterviewReport {
//    @Id
//    @SequenceGenerator(
//            name = "interviewReport_sequence",
//            sequenceName = "interviewReport_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "interviewReport_sequence"
//    )
//    private int id;
//
//    //Σχεση interviewReport με Candidate
//    @OneToOne(mappedBy = "interviewReport")
//    @JsonIgnore
//    Candidate candidate;
//
//    // Σχέση InterviewReport με stepResults
//    @OneToMany(mappedBy = "interviewReport", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StepScore> stepResults = new ArrayList<>();
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Candidate getCandidate() {
//        return candidate;
//    }
//
//    public void setCandidate(Candidate candidate) {
//        this.candidate = candidate;
//    }
//
//    public List<StepScore> getStepResults() {
//        return stepResults;
//    }
//
//    public void setStepResults(List<StepScore> stepResults) {
//        this.stepResults = stepResults;
//    }
//
//    public Interview getInterview() {
//        return interview;
//    }
//
//    public void setInterview(Interview interview) {
//        this.interview = interview;
//    }
//
//    // Σχέση InterviewReport με Interview
//    @ManyToOne
//    @JoinColumn(name = "interview_id", referencedColumnName = "id")
//    @JsonIgnore
//    Interview interview;
//
//    public InterviewReport() {
//    }
//
//}
// src/main/java/com/example/hiringProcess/InterviewReport/InterviewReport.java
package com.example.hiringProcess.InterviewReport;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.StepScore.StepScore;
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

    // Σχέση InterviewReport -> Interview (πολλά reports στο ίδιο interview)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Interview interview;

    // Σχέση InterviewReport με Candidate (1–1 inverse)
    @OneToOne(mappedBy = "interviewReport")
    @JsonIgnore
    private Candidate candidate;

    // Σχέση InterviewReport με stepResults
    @OneToMany(mappedBy = "interviewReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepScore> stepResults = new ArrayList<>();

    public InterviewReport() {}

    // Getters & Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Interview getInterview() { return interview; }
    public void setInterview(Interview interview) { this.interview = interview; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public List<StepScore> getStepResults() { return stepResults; }
    public void setStepResults(List<StepScore> stepResults) { this.stepResults = stepResults; }
}
