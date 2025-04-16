package edu.ua.scholarship.service;

import edu.ua.scholarship.dto.ScholarshipDTO;
import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.User;
import edu.ua.scholarship.repository.ScholarshipDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ScholarshipService {

private final ScholarshipDataRepository scholarshipDataRepository;
private final UserService userService;

    public ScholarshipService(ScholarshipDataRepository scholarshipDataRepository, UserService userService) {
        this.scholarshipDataRepository = scholarshipDataRepository;
        this.userService = userService;
    }

    public Scholarship createScholarship(ScholarshipDTO scholarshipDTO) {
        Scholarship scholarship = convertFromDTO(new Scholarship(), scholarshipDTO);
        return scholarshipDataRepository.save(scholarship);
    }

    public Scholarship updateScholarshipById(Long id,ScholarshipDTO scholarshipDTO) {
        Scholarship scholarship =scholarshipDataRepository.findById(id).orElseGet(Scholarship::new);
        return  scholarshipDataRepository.save(convertFromDTO(scholarship, scholarshipDTO));
    }

    public List<Scholarship> getAllScholarshipsEntity() {
        return scholarshipDataRepository.findAll();
    }


    public List<ScholarshipDTO> getAllScholarships() {
        return convertToDTO(scholarshipDataRepository.findAll());
    }

    public Scholarship findByScholarshipId(Long id) {
        return  scholarshipDataRepository.findById(id).orElse(null);
    }

    public List<ScholarshipDTO> getAllScholarshipsByDonor(String email) {
       User  user = userService.findByEmail(email);
       return convertToDTO(scholarshipDataRepository.findByUserId(user.getId()));
    }


    public  void deleteScholarship(Long id) {
        scholarshipDataRepository.deleteById(id);
    }

    public static  List<ScholarshipDTO> convertToDTO(List<Scholarship> scholarships) {

        return  scholarships.stream().map(ScholarshipService::convertToDTO).toList();
    }

    public static ScholarshipDTO convertToDTO(Scholarship scholarship) {
       final String email = (scholarship.getUser()!= null && scholarship.getUser().getEmail()!= null )  ?scholarship.getUser().getEmail() : "";
        return ScholarshipDTO.builder()
                 .id(scholarship.getId())
                .deadline(scholarship.getDeadline())
                .amount(scholarship.getAmount())
                .name(scholarship.getName())
                .majors(scholarship.getMajors())
                .minors(scholarship.getMinors())
                .gpa(scholarship.getGpa())
                .donorEmail(email)
                .build();
    }

private Scholarship convertFromDTO(Scholarship scholarship,ScholarshipDTO scholarshipDTO) {
        scholarship.setDeadline(scholarshipDTO.getDeadline());
        scholarship.setAmount(scholarshipDTO.getAmount());
        scholarship.setName(scholarshipDTO.getName());
        scholarship.setMajors(scholarshipDTO.getMajors());
        scholarship.setGpa(scholarshipDTO.getGpa());
        scholarship.setUser(userService.findByEmail(scholarshipDTO.getDonorEmail()));
        scholarship.setAdditionalInfo(scholarshipDTO.getAdditionalInfo());

        return scholarship;
}



}
