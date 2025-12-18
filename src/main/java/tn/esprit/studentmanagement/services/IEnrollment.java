package tn.esprit.studentmanagement.services;

import tn.esprit.studentmanagement.entities.Enrollment;
import java.util.List;

public interface IEnrollment {
    List<Enrollment> getAllEnrollments();
    Enrollment getEnrollmentById(Long id);
    Enrollment saveEnrollment(Enrollment enrollment);
    void deleteEnrollment(Long id);
}