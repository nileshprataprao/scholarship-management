package edu.ua.scholarship.controller;

import edu.ua.scholarship.ScholarshipManagementSystemApplication;
import edu.ua.scholarship.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenServiceAPIController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ScholarshipManagementSystemApplication.class})
public class TokenServiceAPIControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;


    @Test
    void when_getToken_thenReturn_token() throws Exception {

        //payload
        String json = """
                {
                "email": "std1@ua.edu",
                "password": "12345"
}""";

        // when
        when(userDetailsService.loadUserByUsername("std1@ua.edu")).thenReturn(Mockito.mock(UserDetails.class));

        mockMvc.perform(post("/scm/user/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("std1@ua.edu").roles("STUDENT")
                                .password("12345")
                        ))
                .andExpect(status().isCreated());
    }
}
