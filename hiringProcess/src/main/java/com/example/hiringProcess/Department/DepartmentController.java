package com.example.hiringProcess.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    //  GET all
    @GetMapping
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    //  GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable("id") Integer id) {
        return departmentService.getDepartment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  POST: Create new department
    @PostMapping
    public ResponseEntity<Department> addNewDepartment(@RequestBody Department department) {
        Department saved = departmentService.addNewDepartment(department);
        URI location = URI.create("/api/v1/departments/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    //  PUT: Update department (all editable fields)
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable("id") Integer id,
            @RequestBody Department updatedFields) {

        Department updated = departmentService.updateDepartment(id, updatedFields);
        return ResponseEntity.ok(updated);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
