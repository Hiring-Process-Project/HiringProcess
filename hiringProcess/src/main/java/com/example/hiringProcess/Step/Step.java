package com.example.hiringProcess.Step;

import com.example.hiringProcess.Interview.Interview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                '}';
    }
}

