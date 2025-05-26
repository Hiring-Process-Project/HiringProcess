package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Occupation.Occupation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private String description;
    private LocalDate publishDate;
    private String status;

    // Σχέση JobAd με Candidate (OneToMany)
    @OneToMany(mappedBy = "jobAd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Candidate> candidates = new ArrayList<>();

    // Σχέση JobAd με Interview (OneToOne)
    // Το Interview αποθηκεύεται, ενημερώνεται ή διαγράφεται αυτόματα μαζί με το JobAd
    // μέσω του CascadeType.ALL. Δηλαδή, ό,τι κάνουμε στο JobAd επηρεάζει και το Interview.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    @JsonIgnore
    private Interview interview;

    // Σχέση JobAd με Occupation (OneToMany)
    @ManyToOne
    @JoinColumn(name = "occupation_id", referencedColumnName = "id")
    @JsonIgnore
    private Occupation occupation;

    public JobAd() {}

    public JobAd(String title, String description, LocalDate publishDate, String status, Interview interview) {
        this.description = description;
        this.publishDate = publishDate;
        this.status = status;
        //προσθήκη του interview στον constructor γιατί θέλω να δημιουργώ ένα interview με το που δημιουργώ ένα jobAd
        this.interview = interview;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "JobAd{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + publishDate +
                ", status='" + status + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                ", candidates="+candidatesToString()+
                ", occcupation='" + occupation + '\'' +
                '}';
    }

    // Προσθήκη candidate στη λίστα (βοηθητική μέθοδος)
    public void addCandidate(Candidate candidate) {
        if (candidate != null) {
            candidates.add(candidate);
            candidate.setJobAd(this);
        }
    }

    // Βοηθητική μέθοδος για την αναπαράσταση των candidates
    private String candidatesToString() {
        if ( candidates == null || candidates.isEmpty()) {
            return "[]";
        }
        return candidates.stream()
                .map(candidate -> "{id=" + candidate.getId() + ", name=" + candidate.getName() + "}")
                .toList()
                .toString();
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }


}

