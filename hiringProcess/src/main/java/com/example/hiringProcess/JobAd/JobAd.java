package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Interview.Interview;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class JobAd {
    @Id
    @SequenceGenerator(
            name = "jobAd_sequence",
            sequenceName = "jobAd_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "jobAd_sequence"
    )
    private int id;

    private String title;
    private String description;
    private LocalDate date;
    private String status;

    @OneToOne(cascade = CascadeType.ALL) // Η owning πλευρά
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    private Interview interview;

    public JobAd() {}

    public JobAd(String title, String description, LocalDate date, String status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    // Getters και Setters
    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }
    public int getId() {
        return id;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "JobAd{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                '}';
    }


    public String getTitle() {
        return title;
    }
}
