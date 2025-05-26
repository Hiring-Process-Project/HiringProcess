package com.example.hiringProcess.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/Department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path = "/departments")
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    @GetMapping(path = "/department")
    public Optional<Department> getDepartment(Integer departmentId) {
        return departmentService.getDepartment(departmentId);
    }

    @PostMapping(path = "/newDepartment")
    public void addNewDepartment(Department department) {
        departmentService.addNewDepartment(department);
    }

//    @DeleteMapping(path = "{departmentId}")
//    public void deleteDepartment(@PathVariable("departmentId") Integer id) {
//        departmentService.deleteDepartment(id);
//    }

//    @PutMapping(path = "{departmentId}")
//    public void updateDepartment(@PathVariable("departmentId") Integer departmentId,
//                                 @RequestParam(required = false) String name) {
//        departmentService.updateDepartment(departmentId, name);
//    }
}
