package edu.ua.scholarship.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto
{
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    private String netId;


    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    private String phoneNumber;

    private String securityQuestion1;

    private String answer1;

    private String securityQuestion2;

     private String answer2;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private String role;
}
