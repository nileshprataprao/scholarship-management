package edu.ua.scholarship.repository;

import edu.ua.scholarship.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDataRepository extends JpaRepository<Student, Long> {


}
