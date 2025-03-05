package com.example.hiringProcess.Questions;

import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_sequence")
    @SequenceGenerator(name = "questions_sequence", sequenceName = "questions_sequence", allocationSize = 1)
    private int id;

    private String name;

    @ManyToOne  // Σωστή σχέση (Many questions -> One Step)
    @JoinColumn(name = "step_id")  // Το question κρατάει το foreign key
    @JsonIgnore
    private Step step;

    public Questions() {}

    public Questions(String name){
        this.name = name;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", step=" + (step != null ? step.getId() : "null") +
                '}';
    }
}
