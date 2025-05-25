package com.example.hiringProcess.InterviewReport;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Questions.Questions;
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class InterviewReport {
    @Id
    @SequenceGenerator(
            name = "interviewreport_sequence",
            sequenceName = "interviewreport_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interviewreport_sequence"
    )
    private int id;  // ΤΟ ID ΠΡΕΠΕΙ ΝΑ ΔΗΛΩΘΕΙ ΠΡΩΤΟ!

    //Σχεση interviewReprt με Candidate OneToOne
    @OneToOne(mappedBy = "interviereport")
    @JsonIgnore
    Candidate candidate;

    // Σχέση InterviewReposrt με stepresults (OneToMany)
    @OneToMany(mappedBy = "stepresults", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepResults> stepResults = new ArrayList<>();

    // Σχέση InterviewReport με interview (OneToOne)
    @OneToOne
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    Interview interview;

    public InterviewReport() {
    }

}
