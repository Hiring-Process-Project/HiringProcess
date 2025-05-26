package com.example.hiringProcess.QuestionScore;

import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
    private int id;

    // Σχέση QuestionScore με Question
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    Question questions;

    // Σχέση QuestionScore με StepResults
    @ManyToOne
    @JoinColumn(name = "stepResults_id", referencedColumnName = "id")
    @JsonIgnore
    private StepResults stepResults;

    private double score;

    public QuestionScore(double score){
        this.score=score;
    }
}
