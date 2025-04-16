package edu.ua.scholarship.repository;

import edu.ua.scholarship.entity.ScholarshipApplicationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<ScholarshipApplicationFile,Long> {

    Optional<ScholarshipApplicationFile> findByFileName(String fileName);

}
