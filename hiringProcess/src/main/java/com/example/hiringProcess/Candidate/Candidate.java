package com.example.hiringProcess.Candidate;

//import com.example.hiringProcess.JobAd.JobAd;
//import jakarta.persistence.*;
import com.example.hiringProcess.Interview.Interview;
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

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobAdId=" + (jobAd != null ? jobAd.getId() : "null") +
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


