package com.example.hiringProcess.Questions;

import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    // Σχέση question με skill (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // Σωστή πλευρά του OneToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "skill_id") // Foreign key για τη σχέση με Step την οποία διαχειρίζεται το Questions
    private Skill skill;

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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", step=" + (step != null ? step.getId() : "null") +
                " question skill" + (skill != null ? skill.getId()  : "null") +
                '}';
    }

    // Μέθοδος που ρυθμίζει τη σχέση με το Skill
    public void addSkill(Skill skill) {
        this.skill = skill;  // Ορίζει το skill στο question
        skill.setQuestion(this);  // Ορίζει το question στο skill (αντίστροφη σχέση)
    }

}
