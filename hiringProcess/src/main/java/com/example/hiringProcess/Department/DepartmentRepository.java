package com.example.hiringProcess.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Μπορείς να προσθέσεις custom queries αν χρειαστεί
}

