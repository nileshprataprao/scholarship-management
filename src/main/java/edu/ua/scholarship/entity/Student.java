package edu.ua.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name ="STUDENT")
@Data
public class Student implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String pronoun;

    @Column
    private String studentId;

    @Column
    private String major;

    @Column
    private String minor;

    @Column
    private double gpa;

    private String currentYear;

    @Column
    private String ethnicity;

    @Column
    private String essay;

    @Column(name ="SCH_AVAIL")
    private Integer availableScholarships;

    @Column
    private String experience;


}
