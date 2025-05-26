package com.example.hiringProcess.Interview;

import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Interview {
    @Id
    @SequenceGenerator(
            name = "interview_sequence",
            sequenceName = "interview_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interview_sequence"
    )
    private int id;

    private String title;
    private String description;

    // Σχέση Interview με JobAd
    @OneToOne(mappedBy = "interview")
    @JsonIgnore
    private JobAd jobAd;

    // Σχέση Interview με Step
    @OneToMany (mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Step> steps = new ArrayList<>();

    // Σχέση Interview με InterviewReport
    @OneToOne(mappedBy = "interview")
    @JsonIgnore
    private InterviewReport interviewReport;

    public Interview() {}

    public Interview(String title, String description, List<Step> steps) {
        this.title = title;
        this.description = description;
        this.steps = steps != null ? steps : new ArrayList<>(); // σιγουρευόμαστε πως το steps δεν είναι κενό
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", jobAd=" + (jobAd != null ? jobAd.getId() : "null") +
                ", steps=" + stepsToString() +
                '}';
    }

    // Προσθήκη step στη λίστα (βοηθητική μέθοδος)
    public void addStep(Step step) {
        if (step != null) {
            steps.add(step);
            step.setInterview(this);  // Σύνδεση του step με το interview
        }
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των steps
    private String stepsToString() {
        if (steps == null || steps.isEmpty()) {
            return "[]";
        }
        return steps.stream()
                .map(step -> "{id=" + step.getId() + ", category=" + step.getDescription() + "}")
                .toList()
                .toString();
    }

    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
