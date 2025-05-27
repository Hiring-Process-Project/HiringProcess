package com.example.hiringProcess.StepResults;

import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.QuestionScore.QuestionScore;
import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class StepResults {
    @Id
    @SequenceGenerator(
            name = "stepResults_sequence",
            sequenceName = "stepResults_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stepResults_sequence"
    )
    private int id;

    // Σχέση StepResults με Step
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "step_id", referencedColumnName = "id")
    @JsonIgnore
    Step step;

    // Σχέση StepResults με InterviewReport
    @ManyToOne
    @JoinColumn(name = "interviewReport_id", referencedColumnName = "id")
    @JsonIgnore
    private InterviewReport interviewReport;

    // Σχέση StepResults με QuestionScore
    @OneToMany(mappedBy = "stepResults", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionScore> questionScores = new ArrayList<>();


    public StepResults() {
    }
}
