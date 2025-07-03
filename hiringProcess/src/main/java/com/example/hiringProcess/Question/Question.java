package com.example.hiringProcess.Question;

import com.example.hiringProcess.QuestionScore.QuestionScore;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question {
    @Id
    @SequenceGenerator(
            name = "questions_sequence",
            sequenceName = "questions_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "questions_sequence"
    )

    private int id;

    private String name;
    private String description;

    // Σχέση Question με Step
    @ManyToOne
    @JoinColumn(name = "step_id", referencedColumnName = "id")
    @JsonIgnore
    private Step step;

    // Σχέση Question με Skill
    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    //Σχεση Question με QuestionScore
    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<QuestionScore> questionScore= new ArrayList<>();

    public Question() {}

    public Question(String name){
        this.name = name;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", step=" + (step != null ? step.getId() : "null") +
                //", question_score=" + (questionScore != null ? questionScore.getScore() : "null") + '}';
        '}';
    }

//    // Μέθοδος που ρυθμίζει τη σχέση με το Skill
//    public void addSkill(Skill skill) {
//        this.skill = skill;  // Ορίζει το skill στο question
//        skill.setQuestion(this);  // Ορίζει το question στο skill (αντίστροφη σχέση)
//    }

//    public void  addScore(QuestionScore score){
//        this.questionScore=score;
//        score.setQuestion(this);
//    }




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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<QuestionScore> getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(List<QuestionScore> questionScore) {
        this.questionScore = questionScore;
    }
}
