package com.example.hiringProcess.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartment(Integer departmentId) {
        return departmentRepository.findById(departmentId);
    }

    public void addNewDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void deleteDepartment(Integer departmentId) {
        boolean exists = departmentRepository.existsById(departmentId);
        if (!exists) {
            throw new IllegalStateException("Department with id " + departmentId + " does not exist");
        }
        departmentRepository.deleteById(departmentId);
    }

//    @Transactional
//    public void updateDepartment(Integer departmentId, String name) {
//        Department department = departmentRepository.findById(departmentId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Department with id " + departmentId + " does not exist"));
//
//        if (name != null && !name.isEmpty() && !Objects.equals(department.getName(), name)) {
//            department.setName(name);
//        }
//    }
}
