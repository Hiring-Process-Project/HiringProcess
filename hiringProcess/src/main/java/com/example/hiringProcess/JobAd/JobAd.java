package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Skill;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JobAd {
    @Id
    @SequenceGenerator(
            name = "interviewGuide_sequence",
            sequenceName = "interviewGuide_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interviewGuide_sequence"
    )

    private int id;
    private String name;

    //  private List<String> uploadedFiles;
    public JobAd(int id, String name) {
        this.name = name;
        this.id = id;
        // this.uploadedFiles = getUploadedFiles();
    }

    public JobAd(String name) {
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
    }