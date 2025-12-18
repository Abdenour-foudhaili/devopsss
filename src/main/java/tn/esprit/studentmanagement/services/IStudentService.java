package tn.esprit.studentmanagement.services;

import tn.esprit.studentmanagement.entities.Student;
import java.util.List;

public interface IStudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student saveStudent(Student student);
    void deleteStudent(Long id);
}