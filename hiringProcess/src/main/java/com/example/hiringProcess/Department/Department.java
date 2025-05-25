package com.example.hiringProcess.Department;

import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Organisation.Organisation;
import com.example.hiringProcess.StepResults.StepResults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table
public class Department {
    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"
    )
    private int id;  // ΤΟ ID ΠΡΕΠΕΙ ΝΑ ΔΗΛΩΘΕΙ ΠΡΩΤΟ!

    private String name;
    private String location;
    private String description;

    @ManyToMany(mappedBy = "departments")
    private Set<JobAd> jobAds;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    public Department(){}

    public Department(String name, String location, String description) {
        this.name = name;
        this.location=location;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
