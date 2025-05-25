package com.example.hiringProcess.Step;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Questions.Questions;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "step_sequence")
    @SequenceGenerator(name = "step_sequence", sequenceName = "step_sequence", allocationSize = 1)
    private int id;

    private String category; // Διορθωμένο όνομα μεταβλητής (σύμφωνα με Java naming conventions)

    @ManyToOne  // Σωστή σχέση (Many Steps -> One Interview)
    @JoinColumn(name = "interview_id")  // Το step κρατάει το foreign key
    @JsonIgnore
    private Interview interview;

    // Σχέση questions με step (OneToMany)
    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions = new ArrayList<>();

    // Σχέση StepResults με step (OneToOne)
    @OneToOne(mappedBy = "step")
    @JsonIgnore
    StepResults stepResults;

    public Step() {}

    public Step(String category){
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    // Προσθήκη ερώτησης στη λίστα (βοηθητική μέθοδος)
    public void addQuestion(Questions question) {
        if (questions != null) {
            questions.add(question);
            question.setStep(this);  // Σύνδεση της ερώτησης με το step
        }
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                ", questions=" + questionsToString() +
                '}';

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
}
