package com.example.hiringProcess.Candidate;

//import com.example.hiringProcess.JobAd.JobAd;
//import jakarta.persistence.*;
import com.example.hiringProcess.Cand_Score.Cand_Score;
import com.example.hiringProcess.JobAd.JobAd;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
//import org.springframework.data.annotation.Id;


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

    private String name;

    @ManyToOne
    @JoinColumn(name = "job_ad_id") // Αυτό πρέπει να ταιριάζει με το mappedBy της JobAd
    @JsonIgnore
    private JobAd jobAd;

    // Σχέση candidate με cand_score (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // Σωστή πλευρά του OneToOne
    @JoinColumn(name = "cand_score_id", referencedColumnName = "cand_score_id")
    private Cand_Score cand_score;


    public Candidate() {}

    public Candidate(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobAd getJobAd() {
        return jobAd;
    }

    public void setJobAd(JobAd jobAd) {
        this.jobAd = jobAd;
    }

    // Μέθοδος που ρυθμίζει τη σχέση με το cand score
    public void addscore(Cand_Score cand_score) {
        this.cand_score = cand_score;
        cand_score.setCandidate(this);  // Ορίζει το score στο candidate (αντίστροφη σχέση)
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobAdId=" + (jobAd != null ? jobAd.getId() : "null") +
                ", cand_score=" + (cand_score != null ? cand_score.getScore() : "null") +
                '}';
    }

}





//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String name;
//
//    @ElementCollection
//    private List<String> uploadedFiles = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "job_ad_id")
//    private JobAd jobAd;
//
//    // Constructor
//    public Candidate() {}
//
//    public Candidate(String name) {
//        this.name = name;
//    }
//
//    // Methods
//    public void uploadFile(String filePath) {
//        uploadedFiles.add(filePath);
//    }
//
//    public void removeFile(String filePath) {
//        uploadedFiles.remove(filePath);
//    }
//
//    // Methods
//        public void uploadFile(String filePath) {
//            uploadedFiles.add(filePath);
//        }
//
//        public void removeFile(String filePath) {
//            uploadedFiles.remove(filePath);
//        }

        // Getters and Setters
       // public Long getId() { return id; }
        //public String getName() { return name; }
       // public void setName(String name) { this.name = name; }
       // public List<String> getUploadedFiles() { return uploadedFiles; }
       // public int getJobAdId() { return jobAdId; }
       // public void setJobAdId(int jobAdId) { this.jobAdId = jobAdId; }
    //}


