package com.example.hiringProcess.Interview;

import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interview_sequence")
    @SequenceGenerator(name = "interview_sequence", sequenceName = "interview_sequence", allocationSize = 1)
    private int id;

    @OneToOne(mappedBy = "interview") // Inverse πλευρά της σχέσης
    @JsonIgnore
    private JobAd jobAd;

    // Σχέση interview με step (OneToMany)
    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true) // Σωστό mapping
    private List<Step> steps = new ArrayList<>();

    // Σχέση InterviewReport με interview (OneToOne)
    @OneToOne(mappedBy = "interview")
    @JsonIgnore
    private InterviewReport interviewReport;

    public Interview() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Προσθήκη step στη λίστα (βοηθητική μέθοδος)
    public void addStep(Step step) {
        if (step != null) {
            steps.add(step);
            step.setInterview(this);  // Σύνδεση του step με το interview
        }
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", jobAd=" + (jobAd != null ? jobAd.getId() : "null") +
                ", steps=" + stepsToString() +
                '}';
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των steps
    private String stepsToString() {
        if (steps == null || steps.isEmpty()) {
            return "[]";
        }
        return steps.stream()
                .map(step -> "{id=" + step.getId() + ", category=" + step.getCategory() + "}")
                .toList()
                .toString();
    }

}
