package com.example.hiringProcess.QuestionScore;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.Questions.Questions;
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class QuestionScore {
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
    private int id;  // ΤΟ ID ΠΡΕΠΕΙ ΝΑ ΔΗΛΩΘΕΙ ΠΡΩΤΟ!

    // Σχέση QuestionScore με question (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    Questions questions;

    @ManyToOne  // Σωστή σχέση (Many QuestionScore -> One Stepresult)
    @JoinColumn(name = "stepresults_id")
    @JsonIgnore
    private StepResults stepResults;



    private double score;

    public QuestionScore(double score){
        this.score=score;
    }
}
