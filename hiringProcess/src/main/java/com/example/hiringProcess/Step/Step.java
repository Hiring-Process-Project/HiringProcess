package com.example.hiringProcess.Step;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Step {
    @Id
    @SequenceGenerator(
            name = "step_sequence",
            sequenceName = "step_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "step_sequence"
    )

    private int id;

    private String title;
    private String description;

    // Σχέση Step με Interview
    @ManyToOne()
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    @JsonIgnore
    private Interview interview;

    // Σχέση Step με Question
    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    // Σχέση Step με StepResults
    @OneToMany(mappedBy = "step")
    @JsonIgnore
    private List<StepResults> stepResults = new ArrayList<>() ;

    public Step() {}

    public Step(String title, String description){
        this.title = title;
        this. description = description;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                ", questions=" + questionsToString() +
                '}';

    }

    // Βοηθητική μέθοδος για προσθήκη ερώτησης στη λίστα
    public void addQuestion(Question question) {
        if (questions != null) {
            questions.add(question);
            question.setStep(this);  // Σύνδεση της ερώτησης με το step
        }
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των ερωτήσεων
    private String questionsToString() {
        if (questions == null || questions.isEmpty()) {
            return "[]";
        }
        return questions.stream()
                .map(q -> "{id=" + q.getId() + ", name=" + q.getName() + "}")
                .toList()
                .toString();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getDescription() {
        return description;
    }

}
