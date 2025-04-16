package edu.ua.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Table(name = "SCH_STUDENT_FILES")
@Entity
@Data
public class ScholarshipApplicationFile implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

   private String fileName;

   @Lob
   @Column(columnDefinition = "BYTEA")
   private byte[] fileContent;

   @Column
   private String fileType;

   @ManyToOne
   @JoinColumn(name = "scholarship_id" ,referencedColumnName = "id")
   private ScholarshipApplication scholarship;

}
