package com.example.hiringProcess.Skill;

import com.example.hiringProcess.Cand_Score.Cand_Score;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Questions.Questions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Skill {

        @Id //δηλώνει πως το κύριο κλειδί για το Skill είναι το skill_id (δηλώνεται ακριβώς από κάτω)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_sequence") //Ορίζει ότι το ID θα αυξάνεται αυτόματα χρησιμοποιώντας sequence generator
        @SequenceGenerator(name = "skill_sequence", sequenceName = "skill_sequence", allocationSize = 1)
        private int skill_id;

        private String skill_name;

        @OneToOne(mappedBy = "skill") // Inverse πλευρά της σχέσης Skill-Questions
        @JsonIgnore
        private Questions question;

        // Σχέση skill με Cand_Score (OneToOne)
        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // Σωστή πλευρά του OneToOne
        @JoinColumn(name = "Cand_Score_id", referencedColumnName = "Cand_Score_id") // Foreign key για τη σχέση με Step την οποία διαχειρίζεται το Questions
        private Cand_Score cand_score;

        //Σχεση skil με Job_ad one to many
        @ManyToOne  // Σωστή σχέση (Many Steps -> One Jobad)
        @JsonIgnore

        @JoinColumn(name = "Job_ad_id")
        private JobAd jobAd;

        public Skill() {};

        public Skill(String skill_name){
            this.skill_name = skill_name;
        }

        // Getters and Setters

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

        public Cand_Score getCand_score() {
            return cand_score;
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

    public String toString() {
            return "Skill{" +
                    "skill_id=" + skill_id +
                    ", skill_name=" + skill_name +
                    ", question=" + (question != null ? question.getId() : "null") +
                    ", cand_score=" + (cand_score != null ? cand_score.getId() : "null") +
                    ", Job_ad=" + (jobAd != null ? jobAd.getId() : "null") +

                    '}';
        }

    // Μέθοδος που ρυθμίζει τη σχέση με το Cand_Score
    public void addcand_score(Cand_Score cand_score) {
        this.cand_score = cand_score;  // Ορίζει το skill στο question
        cand_score.setSkill(this);  // Ορίζει το question στο skill (αντίστροφη σχέση)
    }
}
