package edu.ua.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ua.scholarship.dto.ScholarshipAppDTO;
import edu.ua.scholarship.entity.ScholarshipApplication;
import edu.ua.scholarship.entity.Student;
import edu.ua.scholarship.service.StudentService;
import edu.ua.scholarship.utils.JsonCsvConverter;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class StudentAPIController {

private final StudentService studentService;
private final ObjectMapper objectMapper;

    @PostMapping("scm/student")
    @Observed(name = "student.registration", contextualName = "create-student-registration")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {

        return new ResponseEntity<>(studentService.createStudent(student),HttpStatus.CREATED);
    }

    @GetMapping("scm/student")
    @Observed(name = "student.registration", contextualName = "get-all-registered-students")
    public ResponseEntity<?> getAllStudents(@RequestParam(value = "export" ,required = false) boolean export) throws IOException {
        final List<Student> students = studentService.getAllStudents();

        if (export) {
            final String fileName = "active_students_all.csv";
            String studentsStr = objectMapper.writeValueAsString(students);
            return JsonCsvConverter.responseEntityAsCSV(studentsStr, fileName);
        }

        return new ResponseEntity<>(students,HttpStatus.OK);
    }

    @PostMapping(value = "/scm/student/scholarship",consumes = "application/json",produces = "application/json")
    @Observed(name = "student.scholarship", contextualName = "create-student-scholarship")
    public ResponseEntity<ScholarshipApplication> createScholarship(@RequestBody ScholarshipAppDTO scholarshipAppDTO ) {
        final ScholarshipApplication scholarshipApplication = studentService.createScholarshipApplication(scholarshipAppDTO);

        return new ResponseEntity<>(scholarshipApplication,HttpStatus.CREATED);
    }

    /**
     * update the scholarship application by setting one of @{Status}
     * @param id
     * @param scholarshipAppDTO
     * @return
     */
    @PutMapping(value = "/scm/student/scholarship/{id}",consumes = "application/json",produces = "application/json")
    @Observed(name = "student.scholarship", contextualName = "update-student-registration")
    public ResponseEntity<ScholarshipApplication> updateScholarship(@PathVariable("id") Long id, @RequestBody ScholarshipAppDTO scholarshipAppDTO ) {

        return new ResponseEntity<>( studentService.updateScholarshipApplication(id,scholarshipAppDTO),HttpStatus.OK);
    }

    @GetMapping("/scm/student/scholarship")
    @Observed(name = "student.scholarship", contextualName = "get-all-student-scholarship")
    public ResponseEntity<?> getAllScholarshipApplications(@RequestParam(value = "export" ,required = false) boolean export) throws IOException {

        final List<ScholarshipAppDTO> scholarshipAppDTOList = studentService.getAllScholarshipApplications();
        if (export) {
            final String fileName = "ScholarshipData_all.csv";
            String responseStr = objectMapper.writeValueAsString(scholarshipAppDTOList);
            return JsonCsvConverter.responseEntityAsCSV(responseStr, fileName);
        }

        return ResponseEntity.ok().body(scholarshipAppDTOList);
    }

    @GetMapping("/scm/student/scholarship/studentid/{studentId}")
    @Observed(name = "student.scholarship", contextualName = "get-all-scholarship-by-student")
    public ResponseEntity<?> getAllScholarships(@PathVariable("studentId") Long id,@RequestParam(value = "export" ,required = false) boolean export) throws IOException {

        final List<ScholarshipAppDTO> scholarshipAppDTOList = studentService.getScholarshipsForStudent(id);
        if (export) {
            final String fileName = "ScholarshipData_by_studentId.csv";
            String responseStr = objectMapper.writeValueAsString(scholarshipAppDTOList);
            return JsonCsvConverter.responseEntityAsCSV(responseStr, fileName);
        }

        return ResponseEntity.ok().body(scholarshipAppDTOList);
    }

    @GetMapping("/scm/student/scholarship/scholarshipid/{scholarshipId}")
    @Observed(name = "student.scholarship", contextualName = "get-all-scholarship-by-scholarship-id")
    public ResponseEntity<?> getAllScholarshipsByScholarshipId(@PathVariable("scholarshipId") Long id,@RequestParam(value = "export" ,required = false) boolean export) throws IOException {

        final List<Student> students = studentService.getAllApplicantsByScholarshipId(id);

         if (export) {
             final String fileName = "ScholarshipData_by_scholarshipId.csv";
             String responseStr = objectMapper.writeValueAsString(students);
             return JsonCsvConverter.responseEntityAsCSV(responseStr, fileName);
         }
        return ResponseEntity.ok().body(students);
    }

    /**
     * update the scholarship application by setting one of @{Status}
     * @param id
     * @param scholarshipAppDTO
     * @return
     */
    @PutMapping(value = "/scm/student/scholarship/status/{id}",consumes = "application/json",produces = "application/json")
    @Observed(name = "student.scholarship", contextualName = "update-scholarship-status")

    public ResponseEntity<ScholarshipApplication> updateScholarshipStatus(@PathVariable("id") Long id, @RequestBody ScholarshipAppDTO scholarshipAppDTO ) {

        return new ResponseEntity<>(studentService.updateScholarshipApplicationStatus(id,scholarshipAppDTO),HttpStatus.OK);
    }


}
