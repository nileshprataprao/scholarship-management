package edu.ua.scholarship.service;

import edu.ua.scholarship.dto.ScholarshipDTO;
import edu.ua.scholarship.entity.Scholarship;
import edu.ua.scholarship.entity.ScholarshipScore;
import edu.ua.scholarship.entity.Student;
import edu.ua.scholarship.exception.ScholarshipNotfoundException;
import edu.ua.scholarship.exception.StudentNotfoundException;
import edu.ua.scholarship.matchengine.ScholarshipScoreCalculator;
import edu.ua.scholarship.repository.MatchingRuleEngineDataRepository;
import edu.ua.scholarship.repository.ScholarshipApplicationDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MatchingRuleService {

    private final StudentService studentService;
    private final ScholarshipService scholarshipService;
    private final MatchingRuleEngineDataRepository matchingRuleEngineDataRepository;


    public void createMatchingRulesDataSet() {

        List<ScholarshipScore> scholarshipScores = buildScholarshipScore();
        matchingRuleEngineDataRepository.saveAll(scholarshipScores);
    }

  public List<ScholarshipScore> getAllMatchingRulesData() {
        return matchingRuleEngineDataRepository.findAll();
  }

    private List<ScholarshipScore> buildScholarshipScore(){

        List<ScholarshipScore> scores = new ArrayList<>();
        List<Student> students = studentService.getAllStudents();
        List<Scholarship>  scholarships = scholarshipService.getAllScholarshipsEntity();

         students.forEach(student -> {
           scores.addAll(scholarships.stream().filter(sch->ScholarshipScoreCalculator.isEligible(student,sch))
                    .map(scp->{
                            ScholarshipScore scholarshipScore = new ScholarshipScore();
                            double score = ScholarshipScoreCalculator.calculateScore(student,scp);
                            scholarshipScore.setScore(score);
                            scholarshipScore.setStudent(student);
                            scholarshipScore.setScholarship(scp);
                            return scholarshipScore;
                        }).toList());

        });

     return scores;
    }


    public List<ScholarshipDTO> eligibleScholarships(Long studentId){

        Student student = studentService.findById(studentId);
        if(student == null) {
            throw new StudentNotfoundException("Student not found");
        }

        List<Scholarship>  scholarships = scholarshipService.getAllScholarshipsEntity();

       return scholarships.stream().filter(sch-> ScholarshipScoreCalculator.isEligible(student,sch))
                .map(ScholarshipService::convertToDTO)
                .toList();
    }


    public void overrideApplicantsForScholarship(Long scholarshipId, List<Student> students) {

        List<ScholarshipScore> scholarshipScores = matchingRuleEngineDataRepository.findByScholarship_Id(scholarshipId);

        if(scholarshipScores == null || scholarshipScores.isEmpty()) {
            throw new ScholarshipNotfoundException("No Match Found for Scholarship Id: " + scholarshipId);
        }
         Scholarship scholarship = scholarshipScores.getFirst().getScholarship();
         matchingRuleEngineDataRepository.deleteByScholarship(scholarship);

        List<ScholarshipScore> scores = new ArrayList<>();

        // build the Student Entity
        List<Student> newStudents = new ArrayList<>();
        students.stream().map(Student::getId).forEach(id -> {
            Student std = studentService.findById(id);
            newStudents.add(std);
        });



        newStudents.forEach(student -> {
                        ScholarshipScore scholarshipScore = new ScholarshipScore();
                        double score = ScholarshipScoreCalculator.calculateScore(student,scholarship);
                        scholarshipScore.setScore(score);
                        scholarshipScore.setStudent(student);
                        scholarshipScore.setScholarship(scholarship);
                        scores.add(scholarshipScore);

                    });
        matchingRuleEngineDataRepository.saveAll(scores);

    }

}
