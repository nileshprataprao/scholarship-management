package edu.ua.scholarship.repository;

import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipApplication;
import edu.ua.scholarship.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipDataRepository extends JpaRepository<Scholarship,Long> {

    List<Scholarship> findByUserId(Long userId);

}
