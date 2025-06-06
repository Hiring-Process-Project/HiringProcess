package com.example.hiringProcess.Occupation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.example.hiringProcess.JobAd.JobAd;

@Entity
@Table
public class Occupation {
    @Id
    @SequenceGenerator(
            name = "occupation_sequence",
            sequenceName = "occupation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "occupation_sequence"
    )

    private int id;

    private String title;
    private String escoId;

    // Σχέση Occupation με JobAd
    @OneToMany(mappedBy = "occupation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<JobAd> jobAds = new ArrayList<>();

    public Occupation() {}

    public Occupation(String title, String escoId) {
        this.title = title;
        this.escoId = escoId;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "Occupation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", escoId='" + escoId + '\'' +
                '}';
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }
}
