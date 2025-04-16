package edu.ua.scholarship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ua.scholarship.dto.ScholarshipDTO;
import edu.ua.scholarship.entity.ScholarshipScore;
import edu.ua.scholarship.entity.Student;
import edu.ua.scholarship.service.MatchingRuleService;
import edu.ua.scholarship.utils.JsonCsvConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class MatchingEngineAPIController {

    private final MatchingRuleService matchingRuleService;
    private final ObjectMapper objectMapper;


    @PostMapping("/scm/matchingengine/scholarship/report")
    public ResponseEntity<Void> buildMatchingScholarshipApplications() throws IOException {

        matchingRuleService.createMatchingRulesDataSet();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/scm/matchingengine/scholarship/report")
    public ResponseEntity<?> getAllScholarshipAndApplicants(@RequestParam(value = "export" ,required = false) boolean export) throws IOException {

        List<ScholarshipScore> scores = matchingRuleService.getAllMatchingRulesData();

        if (export) {
            final String fileName = "ScholarshipData_scores_all.csv";
            String responseStr = objectMapper.writeValueAsString(scores);
            return JsonCsvConverter.responseEntityAsCSV(responseStr, fileName);
        }
        return ResponseEntity.ok(scores);

    }

    @GetMapping("/scm/matchingengine/scholarship/{studentId}")
    public ResponseEntity<List<ScholarshipDTO>> getAllScholarshipScoreForStudent(@PathVariable Long studentId) {

        return ResponseEntity.ok(matchingRuleService.eligibleScholarships(studentId));

    }

    @PostMapping("/scm/matchingengine/override/{scholarshipId}")
    public ResponseEntity<Void> overrideScholarshipMatch(@PathVariable(value = "scholarshipId") Long scholarshipId, @RequestBody List<Student> students) throws IOException {

        matchingRuleService.overrideApplicantsForScholarship(scholarshipId,students);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
