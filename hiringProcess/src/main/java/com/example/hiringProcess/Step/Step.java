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

    @ManyToOne  // Σωστή σχέση (Many Steps -> One Interview)
    @JoinColumn(name = "interview_id")  // Το step κρατάει το foreign key
    @JsonIgnore
    private Interview interview;

    public Step() {}

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

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                '}';
    }
}
