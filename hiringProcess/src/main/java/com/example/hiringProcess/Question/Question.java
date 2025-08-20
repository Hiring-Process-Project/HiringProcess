package com.example.hiringProcess.Question;

import com.example.hiringProcess.QuestionScore.QuestionScore;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @SequenceGenerator(name = "questions_sequence", sequenceName = "questions_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_sequence")
    private int id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "step_id", referencedColumnName = "id")
    @JsonIgnore
    private Step step;

    // Ordering μέσα στο step
    @Column(name = "position")
    private Integer position;

    // === Many-to-Many με Skill (OWNING SIDE) ===
    @ManyToMany
    @JoinTable(
            name = "question_skill",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<QuestionScore> questionScore = new ArrayList<>();

    public Question() {}
    public Question(String title){ this.title = title; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Step getStep() { return step; }
    public void setStep(Step step) { this.step = step; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public Set<Skill> getSkills() { return skills; }
    public void setSkills(Set<Skill> skills) { this.skills = skills; }

    public List<QuestionScore> getQuestionScore() { return questionScore; }
    public void setQuestionScore(List<QuestionScore> questionScore) { this.questionScore = questionScore; }
}
