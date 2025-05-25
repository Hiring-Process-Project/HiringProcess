package com.example.hiringProcess.Candidate;


import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
@Table
public class Candidate {
    @Id
    @SequenceGenerator(
            name = "candidate_sequence",
            sequenceName = "candidate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "candidate_sequence"
    )
    private int id;  // ΤΟ ID ΠΡΕΠΕΙ ΝΑ ΔΗΛΩΘΕΙ ΠΡΩΤΟ!


    private String firstName;
    private  String lastName;
    private String email;
    private String info;
    private String decision;
    private String reasoning;

    @ManyToOne
    @JoinColumn(name = "job_ad_id") // Αυτό πρέπει να ταιριάζει με το mappedBy της JobAd
    @JsonIgnore
    private JobAd jobAd;

    // Σχέση inteviewreport με candidate (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // Σωστή πλευρά του OneToOne
    @JoinColumn(name = "interviewreport_id", referencedColumnName = "interviewreport_id") // Foreign key για τη σχέση με Step την οποία διαχειρίζεται το Questions
    InterviewReport interviewReport;

    public Candidate() {}

    public Candidate(String firstName,String lastName, String email, String info, String decision, String reasoning) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.info = info;
        this.decision = decision;
        this.reasoning = reasoning;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", First Name='" + firstName + '\'' +
                ", Last Name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", Info='" + info + '\'' +
                ", Decision='" + decision + '\'' +
                ", Reasoning='" + reasoning + '\'' +
                ", jobAdId=" + (jobAd != null ? jobAd.getId() : "null") +
                '}';
    }

    public void setJobAd(JobAd jobAd) {
        this.jobAd=jobAd;
    }

    public int getId() {
       return id;
    }

    public String getName() {
        return firstName;
    }
}




