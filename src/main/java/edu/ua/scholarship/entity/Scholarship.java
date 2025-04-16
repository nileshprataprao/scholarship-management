package edu.ua.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "SCHOLARSHIP")
public class Scholarship implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String name;
    @Column
    private Double amount;

    @Column
    @ElementCollection
    private List<String> majors;

    @Column
    @ElementCollection
    private List<String> minors;

    @Column
    private double  gpa;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    @Column
    private String additionalInfo;

}
