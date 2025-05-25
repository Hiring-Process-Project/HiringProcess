package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Occupation.Occupation;

import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
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

    private String title;
    private String description;
    private LocalDate publishDate;
    private String status;

    // Σχέση JobAd με Candidate (OneToMany)
    @OneToMany(mappedBy = "jobAd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Candidate> candidates = new ArrayList<>();

    // Σχέση JobAd με Interview (OneToOne)
    // Το Interview αποθηκεύεται, ενημερώνεται ή διαγράφεται αυτόματα μαζί με το JobAd
    // μέσω του CascadeType.ALL. Δηλαδή, ό,τι κάνουμε στο JobAd επηρεάζει και το Interview.
    @OneToOne(cascade = CascadeType.ALL) // Η owning πλευρά
    @JoinColumn(name = "interview_id", referencedColumnName = "id")
    private Interview interview;

    // Σχέση JobAd με Occupation (OneToMany)
    @ManyToOne
    @JoinColumn(name = "occupation_id", referencedColumnName = "id")
    private Occupation occupation;

    public JobAd() {}

    public JobAd(String title, String description, LocalDate date, String status, Interview interview) {
        this.title = title;
        this.description = description;
        this.publishDate = date;
        this.status = status;
        //προσθήκη του interview στον constructor γιατί θέλω να δημιουργώ ένα interview
        this.interview = interview;
    }

    // ToString για debugging
    @Override
    public String toString() {
        return "JobAd{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + publishDate +
                ", status='" + status + '\'' +
                ", interview=" + (interview != null ? interview.getId() : "null") +
                ", candidates="+candidatesToString()+
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }
}

