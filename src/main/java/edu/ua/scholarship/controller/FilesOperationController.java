package edu.ua.scholarship.controller;

import edu.ua.scholarship.exception.FileOperationException;
import edu.ua.scholarship.service.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
public class FilesOperationController {

   private final FileStorageService storageService;

    @PostMapping("/scm/scholarship/student/{scholarshipId}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable(value = "scholarshipId") Long id, @RequestParam("file") MultipartFile file) {
        try {
            storageService.save(file,id);
            return ResponseEntity.status(HttpStatus.OK).body("File Uploaded Successfully");
        } catch (Exception e) {
            throw new FileOperationException("Failed to upload the file: " + file.getOriginalFilename());
        }
    }

    @GetMapping("/scm/scholarship/student/name/{fileName}")
    public ResponseEntity<byte[]> getFileByName(@PathVariable(value = "fileName") String fileName) {
        return storageService.getFileByName(fileName);
    }

    @GetMapping("/scm/scholarship/student/id/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable("id") Long id) {
        return storageService.getFileById(id);
    }

    @DeleteMapping("/scm/scholarship/student/name/{fileName}")
    public ResponseEntity<String> deleteFileByName(@PathVariable("fileName") String fileName) {
         storageService.deleteByName(fileName);
        return ResponseEntity.status(HttpStatus.OK).body("File Deleted Successfully");
    }

    @DeleteMapping("/scm/scholarship/student/id/{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable("id") Long id) {
        storageService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("File Deleted Successfully");
    }
}
