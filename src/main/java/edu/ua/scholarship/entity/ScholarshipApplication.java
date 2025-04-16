package edu.ua.scholarship.entity;

import edu.ua.scholarship.dto.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "STUDENT_SCHOLARSHIP")
public class ScholarshipApplication implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name= "scholarship_id" ,unique = false)
    private Scholarship scholarship;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "scholarship" ,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<ScholarshipApplicationFile> files = new ArrayList<>();

    public void addFiles(List<ScholarshipApplicationFile> file) {
        this.files = file;
        file.forEach(f -> f.setScholarship(this));
    }

   public void removeFile(ScholarshipApplicationFile file) {
        if ((this.files != null && !this.files.isEmpty())) {
            this.files.remove(file);
        }

   }

}
