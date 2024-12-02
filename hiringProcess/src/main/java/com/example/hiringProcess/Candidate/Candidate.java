package com.example.hiringProcess.Candidate;

//import com.example.hiringProcess.JobAd.JobAd;
//import jakarta.persistence.*;
import jakarta.persistence.*;
//import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


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

    private int id;
    private String name;
  //  private List<String> uploadedFiles;
    public Candidate(int id ,String name) {
        this.name = name;
        this.id = id;
       // this.uploadedFiles = getUploadedFiles();
   }
    public Candidate(String name) {
        this.name = name;
        // this.uploadedFiles = getUploadedFiles();
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

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
    }


