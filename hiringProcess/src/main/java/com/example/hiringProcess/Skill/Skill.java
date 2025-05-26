package com.example.hiringProcess.Skill;

import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Skill {
    @Id
    @SequenceGenerator(
            name = "skill_sequence",
            sequenceName = "skill_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "skill_sequence"
    )

    private int id;

    private String name;
    private String title;
    private String escoId;

    //Σχέση Skill με Question
    @OneToOne(mappedBy = "skill")
    @JsonIgnore
    private Question question;

    public Skill() {}

    public Skill(String skill_name) {
        this.name = name;
        this.title = title;
        this.escoId = escoId;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name=" + name +
                ", title=" + title +
                ", escoId=" + escoId +
                ", question=" + (question != null ? question.getId() : "null") +
                '}';
    }

    //Getters and Setters

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
