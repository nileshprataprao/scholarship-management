package edu.ua.scholarship.repository;

import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipApplicationDataRepository extends JpaRepository<ScholarshipApplication,Long> {

    List<ScholarshipApplication> findByStudentId(Long studentId);
    List<ScholarshipApplication> findByScholarshipId(Long id);

}
