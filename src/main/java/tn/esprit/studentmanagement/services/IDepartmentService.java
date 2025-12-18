package tn.esprit.studentmanagement.services;

import tn.esprit.studentmanagement.entities.Department;
import java.util.List;

public interface IDepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department saveDepartment(Department department);
    void deleteDepartment(Long id);
}