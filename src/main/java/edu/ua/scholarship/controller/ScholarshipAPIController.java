package edu.ua.scholarship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ua.scholarship.dto.ScholarshipAppDTO;
import edu.ua.scholarship.dto.ScholarshipDTO;
import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipApplication;
import edu.ua.scholarship.service.ScholarshipService;
import edu.ua.scholarship.utils.JsonCsvConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class ScholarshipAPIController {

    private final ScholarshipService scholarshipService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/scm/scholarship", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Scholarship>createScholarship(@RequestBody ScholarshipDTO scholarshipDTO ) {
        return new ResponseEntity<>(scholarshipService.createScholarship(scholarshipDTO), HttpStatus.CREATED);
    }

    @GetMapping("scm/scholarship")
    public ResponseEntity<?> getAllScholarship(@RequestParam(value = "export" ,required = false) boolean export) throws IOException {

         final List<ScholarshipDTO> scholarships = scholarshipService.getAllScholarships();

        if (export) {
            final String fileName = "AllAvailableScholarshipData_all.csv";
            String responseStr = objectMapper.writeValueAsString(scholarships);
            return JsonCsvConverter.responseEntityAsCSV(responseStr, fileName);
        }

        return new ResponseEntity<>(scholarships, HttpStatus.OK);
    }

    @GetMapping("scm/scholarship/{donorEmail}")
    public ResponseEntity<List<ScholarshipDTO>> getScholarshipByDonorId(@PathVariable("donorEmail") String email) {
        return new ResponseEntity<>(this.scholarshipService.getAllScholarshipsByDonor(email), HttpStatus.OK);
    }

    @PutMapping("/scm/scholarship/{id}")
    public ResponseEntity<Scholarship> updateScholarship(@PathVariable("id") Long id, @RequestBody ScholarshipDTO scholarshipDTO) {

        return new ResponseEntity<>(this.scholarshipService.updateScholarshipById(id,scholarshipDTO), HttpStatus.OK);
    }


    @DeleteMapping("/scm/scholarship/{id}")
    public ResponseEntity<String> deleteScholarship(@PathVariable("id") Long id) {
        this.scholarshipService.deleteScholarship(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }



}
