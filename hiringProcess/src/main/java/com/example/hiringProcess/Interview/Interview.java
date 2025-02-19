package com.example.hiringProcess.Interview;

import com.example.hiringProcess.JobAd.JobAd;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Interview {
    @Id
    @SequenceGenerator(
            name = "interview_sequence",
            sequenceName = "interview_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interview_sequence"
    )
    private int id;

    @OneToOne(mappedBy = "interview") // Inverse πλευρά της σχέσης
    @JsonIgnore  // Δεν θα επιστρέφεται το JobAd όταν ζητάμε το Interview
    private JobAd jobAd;

    public Interview() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JobAd getJobAd() {
        return jobAd;
    }

    public void setJobAd(JobAd jobAd) {
        this.jobAd = jobAd;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", jobAd=" + (jobAd != null ? jobAd.getId() : "null") +
                '}';
    }
}

