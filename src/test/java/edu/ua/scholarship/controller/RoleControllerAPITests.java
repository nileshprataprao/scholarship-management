package edu.ua.scholarship.controller;

import edu.ua.scholarship.ScholarshipManagementSystemApplication;
import edu.ua.scholarship.entity.Role;
import edu.ua.scholarship.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RolesController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ScholarshipManagementSystemApplication.class)

public class RoleControllerAPITests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Test
     void whenGetAllRoles_thenReturnJsonArray() throws Exception {

        when(roleService.getAllRoles()).thenReturn(List.of(new Role(1,"ROLE_STUDENT"),new Role(2,"ROLE_SCH_ADMIN")));

        mockMvc.perform(get("/scm/roles")
                .with(user("std1@ua.edu").roles("STUDENT")
                        .password("12345")
                ))
                .andExpect(status().isOk());



    }



}
