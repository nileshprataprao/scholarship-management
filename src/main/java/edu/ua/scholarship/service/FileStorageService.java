package edu.ua.scholarship.service;

import java.io.IOException;

import edu.ua.scholarship.entity.ScholarshipApplication;
import edu.ua.scholarship.entity.ScholarshipApplicationFile;
import edu.ua.scholarship.exception.FileOperationException;
import edu.ua.scholarship.exception.ScholarshipNotfoundException;
import edu.ua.scholarship.repository.FileUploadRepository;
import edu.ua.scholarship.repository.ScholarshipApplicationDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class  FileStorageService {

 private final FileUploadRepository uploadRepository;
 private final ScholarshipApplicationDataRepository scholarshipApplicationDataRepository;

    public void save(MultipartFile file,Long scholarshipId) throws IOException {

        ScholarshipApplication application = scholarshipApplicationDataRepository.findById(scholarshipId).orElse(null);
        if (application == null) {
            throw new ScholarshipNotfoundException("Scholarship with id " + scholarshipId + " not found");
        }

        ScholarshipApplicationFile scholarshipApplicationFile = new ScholarshipApplicationFile();
        scholarshipApplicationFile.setFileName(file.getOriginalFilename());
        scholarshipApplicationFile.setFileContent(file.getBytes());
        scholarshipApplicationFile.setScholarship(application);
        scholarshipApplicationFile.setFileType(file.getContentType());

         uploadRepository.save(scholarshipApplicationFile);

        }


    public ResponseEntity<byte[]> getFileByName(String fileName) {
        return uploadRepository.findByFileName(fileName)
                .map(file -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
                        .body(file.getFileContent()))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<byte[]> getFileById(Long id) {
        return uploadRepository.findById(id)
                .map(file -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
                        .body(file.getFileContent()))
                .orElse(ResponseEntity.notFound().build());
    }

    public void deleteByName(String filename) {

        ScholarshipApplicationFile applicationFile = uploadRepository.findByFileName(filename).orElse(null);

        if (applicationFile == null) {
             throw new FileOperationException("File not found");
        }
        uploadRepository.delete(applicationFile);
    }

    public void deleteById(Long id) {

        ScholarshipApplicationFile applicationFile = uploadRepository.findById(id).orElse(null);

        if (applicationFile == null) {
            throw new FileOperationException("File not found");
        }
        uploadRepository.delete(applicationFile);
    }

    }
