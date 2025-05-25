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
            name = "stepresults_sequence",
            sequenceName = "stepresults_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stepresults_sequence"
    )
    private int id;  // ΤΟ ID ΠΡΕΠΕΙ ΝΑ ΔΗΛΩΘΕΙ ΠΡΩΤΟ!

    // Σχέση StepResults με step (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "step_id", referencedColumnName = "step_id")
    Step step;

    @ManyToOne  // Σωστή σχέση (Many StepResults -> One InterviewReport)
    @JoinColumn(name = "interviewreport_id")
    @JsonIgnore
    private InterviewReport interviewReport;

    // Σχέση QuestionScore με stepresults (OneToMany)
    @OneToMany(mappedBy = "questionscore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionScore> questionScore = new ArrayList<>();

    public StepResults() {
    }
}
