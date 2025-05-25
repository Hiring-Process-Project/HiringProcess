package com.example.hiringProcess.Organisation;

import com.example.hiringProcess.Department.Department;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.Skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Organisation {
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
    private String description;

    // Σχέση JobAd με Skill (OneToMany)
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, orphanRemoval = true) // Σωστό mapping
    private List<Department> departments = new ArrayList<>();

    public Organisation(){}

    public Organisation(String name, String description) {
        this.name = name;
        this.description=description;
    }

}
