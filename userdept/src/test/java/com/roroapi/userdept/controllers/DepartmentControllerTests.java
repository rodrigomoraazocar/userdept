package com.roroapi.userdept.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.services.DepartmentService;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void findAll_ShouldReturnDepartments() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");
        
        given(departmentService.findAll()).willReturn(Arrays.asList(department));
        
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Department"));
    }

    @Test
    public void findById_ShouldReturnDepartment() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");
        
        given(departmentService.findById(1L)).willReturn(Optional.of(department));
        
        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Department"));
    }

    @Test
    public void insert_ShouldReturnDepartment() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");
        
        given(departmentService.save(department)).willReturn(department);
        
        mockMvc.perform(post("/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Department"));
    }

    @Test
    public void update_ShouldReturnUpdatedDepartment() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Updated Name");
        
        given(departmentService.update(1L, department)).willReturn(department);
        
        mockMvc.perform(put("/departments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isNoContent());
    }
}
