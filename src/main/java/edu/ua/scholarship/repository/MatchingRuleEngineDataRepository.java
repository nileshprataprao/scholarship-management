package edu.ua.scholarship.repository;

import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRuleEngineDataRepository extends JpaRepository<ScholarshipScore,Long> {

        void deleteByScholarship(Scholarship scholarship);
        List<ScholarshipScore> findByScholarship_Id(long scholarshipId);
}
