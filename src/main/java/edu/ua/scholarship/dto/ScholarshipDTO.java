package edu.ua.scholarship.dto;

import edu.ua.scholarship.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScholarshipDTO implements Serializable {

    private Long id;
    private String donorEmail;
    private String name;
    private Double amount;
    private int availableScholarships;
    private List<String> majors;
    private List<String> minors;
    private Double  gpa;
    private Date deadline;
    private String additionalInfo;
}
