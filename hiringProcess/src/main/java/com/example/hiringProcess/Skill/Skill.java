package com.example.hiringProcess.Skill;

import com.example.hiringProcess.Questions.Questions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Skill {

        @Id //δηλώνει πως το κύριο κλειδί για το Skill είναι το skill_id (δηλώνεται ακριβώς από κάτω)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_sequence") //Ορίζει ότι το ID θα αυξάνεται αυτόματα χρησιμοποιώντας sequence generator
        @SequenceGenerator(name = "questions_sequence", sequenceName = "skill_sequence", allocationSize = 1) //Δημιουργεί έναν sequence generator
        private int skill_id;

        private String skill_name;

        @OneToOne(mappedBy = "skill") // Inverse πλευρά της σχέσης Skill-Questions
        @JsonIgnore
        private Questions question;

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

        public String toString() {
            return "Skill{" +
                    "skill_id=" + skill_id +
                    ", skill_name=" + skill_name +
                    ", question=" + (question != null ? question.getId() : "null") +
                    '}';
        }
}
