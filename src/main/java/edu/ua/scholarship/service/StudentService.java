package edu.ua.scholarship.service;

import edu.ua.scholarship.dto.ScholarshipAppDTO;
import edu.ua.scholarship.dto.ScholarshipDTO;
import edu.ua.scholarship.dto.Status;
import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipApplication;
import edu.ua.scholarship.entity.ScholarshipApplicationFile;
import edu.ua.scholarship.entity.Student;
import edu.ua.scholarship.exception.ScholarshipNotfoundException;
import edu.ua.scholarship.exception.StudentNotfoundException;
import edu.ua.scholarship.repository.ScholarshipApplicationDataRepository;
import edu.ua.scholarship.repository.StudentDataRepository;
import edu.ua.scholarship.utils.CustomMultipartFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final ScholarshipApplicationDataRepository scholarshipApplicationDataRepository;
    private final StudentDataRepository studentDataRepository;
    private ScholarshipService scholarshipService;

    public Student createStudent(Student  student) {
      return studentDataRepository.save(student);
   }

    public Student findById(Long  id) {
        return studentDataRepository.findById(id).orElse(null);
    }

    public ScholarshipApplication createScholarshipApplication(ScholarshipAppDTO appDTO) {

        Student std = studentDataRepository.findById(appDTO.getStudent().getId()).orElse(null);

        if (std == null) {
            throw new StudentNotfoundException("Student not found");
        }
        Scholarship scholarship = scholarshipService.findByScholarshipId(appDTO.getScholarship().getId());
        if (scholarship == null) {
            throw new ScholarshipNotfoundException("Scholarship not found");
        }

        return scholarshipApplicationDataRepository.save(buildScholarshipEntity(appDTO, std,scholarship));
    }

    public ScholarshipApplication updateScholarshipApplication(Long id, ScholarshipAppDTO appDTO) {

        ScholarshipApplication scholarshipApplication = scholarshipApplicationDataRepository.findById(id).orElse(null);
        if (scholarshipApplication == null) {
            throw new ScholarshipNotfoundException("Scholarship Application not found");
        }
        scholarshipApplication.setStatus(appDTO.getStatus());

         return scholarshipApplicationDataRepository.save(scholarshipApplication);
    }


    public ScholarshipApplication updateScholarshipApplicationStatus(Long id, ScholarshipAppDTO appDTO) {

        ScholarshipApplication scholarshipApplication = scholarshipApplicationDataRepository.findById(id).orElse(null);
        if (scholarshipApplication == null) {
            throw new ScholarshipNotfoundException("Scholarship Application not found");
        }
        scholarshipApplication.setStatus(appDTO.getStatus());

         return scholarshipApplicationDataRepository.save(scholarshipApplication);
    }

    public List<Student> getAllStudents() {
        return studentDataRepository.findAll();
    }


    public List<ScholarshipAppDTO> getAllScholarshipApplications() {
        return  this.scholarshipApplicationDataRepository
                .findAll()
                .stream()
                .map(this::convertEntityToDTO).toList();
    }

    public ScholarshipApplication findScholarshipById(Long id) {
        return scholarshipApplicationDataRepository.findById(id).orElse(null);
    }

    public List<ScholarshipAppDTO> getScholarshipsForStudent(Long id ) {
        Student std = studentDataRepository.findById(id).orElse(null);

        if (std == null) {
            throw new StudentNotfoundException("Student not found");
        }

        return getAllScholarships(std.getId());
    }

    public List<Student> getAllApplicantsByScholarshipId(Long id ) {

        Scholarship scholarship = scholarshipService.findByScholarshipId(id);

        if (scholarship == null) {
            throw new ScholarshipNotfoundException("Scholarship not found");
        }

        List<ScholarshipApplication> applications = scholarshipApplicationDataRepository.findByScholarshipId(id);

        return applications.stream()
                .map(this::convertEntityToDTO)
                .map(ScholarshipAppDTO::getStudent)
                .toList();
    }

    public List<ScholarshipAppDTO> getAllScholarships(Long studentId  ) {

        return  this.scholarshipApplicationDataRepository
                .findByStudentId(studentId)
                .stream()
                .map(this::convertEntityToDTO).toList();
    }


    private ScholarshipAppDTO convertEntityToDTO(ScholarshipApplication scholarship) {

        ScholarshipAppDTO appDTO = new ScholarshipAppDTO();

        appDTO.setId(scholarship.getId());
        appDTO.setStudent(scholarship.getStudent());
        appDTO.setFiles(scholarship.getFiles().stream().map(StudentService::getCustomMultipartFile).toList());
        appDTO.setScholarship(convertToSchDTO(scholarship.getScholarship()));
        return appDTO;

    }

    private static MultipartFile getCustomMultipartFile(ScholarshipApplicationFile scholarshipFile) {

        return new CustomMultipartFile(scholarshipFile.getFileContent(),
                scholarshipFile.getFileName(),
                scholarshipFile.getFileName(),
                scholarshipFile.getFileType()
         );
    }

    private static ScholarshipDTO convertToSchDTO(Scholarship scholarship) {
        final String email = (scholarship.getUser()!= null && scholarship.getUser().getEmail()!= null )  ?scholarship.getUser().getEmail() : "";
        return ScholarshipDTO.builder()
                .id(scholarship.getId())
                .deadline(scholarship.getDeadline())
                .amount(scholarship.getAmount())
                .name(scholarship.getName())
                .majors(scholarship.getMajors())
                .gpa(scholarship.getGpa())
                .donorEmail(email)
                .build();
    }

    private static ScholarshipApplication buildScholarshipEntity(ScholarshipAppDTO appDTO , Student student,Scholarship scholarship ) {
        ScholarshipApplication scholarshipApplication = new ScholarshipApplication();
        if(appDTO != null && (appDTO.getFiles() !=null && ! appDTO.getFiles().isEmpty())) {
            scholarshipApplication.addFiles(appDTO.getFiles().stream().map(StudentService::buildScholarshipFilesEntity).toList());
        }

        scholarshipApplication.setStudent(student);
        scholarshipApplication.setStatus(Status.PENDING);
        scholarshipApplication.setScholarship(scholarship);

        return scholarshipApplication;
    }


    private static ScholarshipApplicationFile buildScholarshipFilesEntity(MultipartFile file) {
        ScholarshipApplicationFile scholarshipFile = new ScholarshipApplicationFile();
        try {
            scholarshipFile.setFileContent(file.getBytes());
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        scholarshipFile.setFileName(file.getOriginalFilename());
        return scholarshipFile;

    }
}
