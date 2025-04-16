package edu.ua.scholarship.controller;

import edu.ua.scholarship.ScholarshipManagementSystemApplication;
import edu.ua.scholarship.dto.UserDto;
import edu.ua.scholarship.entity.User;
import edu.ua.scholarship.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegistrationAPIController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ScholarshipManagementSystemApplication.class})
public class UserRegistrationAPIControllerTests {

    @Autowired
   private MockMvc mockMvc;

    @MockBean
    private  UserService userService;


    @Test
    void whenRegisterUser_thenReturnUser() throws Exception {

        //payload
        String json = """
                {
                "firstName": "f1",
                "lastName": "l1",
                "email": "test2@test.com",
                "phoneNumber": "1234567890",
                "securityQuestion1": "q1",
                "answer1": "a1",
                "securityQuestion2": "q2",
                "answer2": "a2",
                "password": "12345",
                "role": "ROLE_SCH_ADMIN"
}""";

       // when
       when(userService.findByEmail("email@email.com")).thenReturn(Mockito.mock(User.class));
       doNothing().when(userService).createUser(Mockito.mock(UserDto.class));

        mockMvc.perform(post("/scm/user")
                         .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user("std1@ua.edu").roles("STUDENT")
                                .password("12345")
                        ))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
   }




}
