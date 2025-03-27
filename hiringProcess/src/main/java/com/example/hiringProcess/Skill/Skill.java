package com.example.hiringProcess.Skill;

import com.example.hiringProcess.Cand_Score.Cand_Score;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Questions.Questions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_sequence")
    @SequenceGenerator(name = "skill_sequence", sequenceName = "skill_sequence", allocationSize = 1)
    private int skill_id;

    private String skill_name;

    @OneToOne(mappedBy = "skill")
    @JsonIgnore
    private Questions question;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Cand_Score_id", referencedColumnName = "Cand_Score_id")
    private Cand_Score cand_score;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "Job_ad_id")
    private JobAd jobAd;

    public Skill() {}

    public Skill(String skill_name) {
        this.skill_name = skill_name;
    }

    public int getId() {
        return skill_id;
    }

    public void setId(int skill_id) {
        this.skill_id = skill_id;
    }

    public String getName() {
        return skill_name;
    }

    public void setName(String skill_name) {
        this.skill_name = skill_name;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    @JsonIgnore // Δεν θα εμφανίζεται στη λίστα skills της JobAd
    public Cand_Score getCand_score() {
        return cand_score;
    }

    @JsonProperty("cand_score") // Θα εμφανίζεται μόνο αν υπάρχει σχετική ερώτηση
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Cand_Score getCandScoreForQuestion() {
        return (question != null) ? cand_score : null;
    }

    public void setCand_score(Cand_Score cand_score) {
        this.cand_score = cand_score;
    }

    public JobAd getJobAd() {
        return jobAd;
    }

    public void setJobAd(JobAd jobAd) {
        this.jobAd = jobAd;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skill_id=" + skill_id +
                ", skill_name=" + skill_name +
                ", question=" + (question != null ? question.getId() : "null") +
                ", cand_score=" + (cand_score != null ? cand_score.getId() : "null") +
                ", Job_ad=" + (jobAd != null ? jobAd.getId() : "null") +
                '}';
    }

    public void addcand_score(Cand_Score cand_score) {
        this.cand_score = cand_score;
        cand_score.setSkill(this);
    }
}
