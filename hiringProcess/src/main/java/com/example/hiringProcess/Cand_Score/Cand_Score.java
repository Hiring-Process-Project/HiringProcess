package com.example.hiringProcess.Cand_Score;

import com.example.hiringProcess.Skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Cand_Score {
    @Id //δηλώνει πως το κύριο κλειδί για το Cand_Score είναι τοCand_Score_id (δηλώνεται ακριβώς από κάτω)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Cand_Score_sequence") //Ορίζει ότι το ID θα αυξάνεται αυτόματα χρησιμοποιώντας sequence generator
    @SequenceGenerator(name = "Cand_Score_sequence", sequenceName = "Cand_Score_sequence", allocationSize = 1)
    private int Cand_Score_id;

    private Integer score;

    @OneToOne(mappedBy = "cand_score") // Σύνδεση με το αντίστοιχο πεδίο στην Skill
    @JsonIgnore
    private Skill skill;

    public Cand_Score() {};

    public Cand_Score(Integer score){
        this.score=score;
    }
    //Getters and Setters

    public int getId() {
        return Cand_Score_id;
    }

    public void setId(int Cand_Score_id) {
        this.Cand_Score_id = Cand_Score_id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String toString() {
        return "Skill{" +
                "Cand_Score_id=" + Cand_Score_id +
                ", score=" + score +
                ", skill=" + (skill != null ? skill.getId() : "null") +
                '}';
    }
}
