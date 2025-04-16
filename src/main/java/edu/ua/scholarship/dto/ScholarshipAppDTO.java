package edu.ua.scholarship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.ua.scholarship.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScholarshipAppDTO  {

  private Long id;
  private Student student;
  private ScholarshipDTO scholarship;
  private List<MultipartFile> files;
  private Status status;



}
