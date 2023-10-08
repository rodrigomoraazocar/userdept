package com.roroapi.userdept.dto;

import com.roroapi.userdept.entities.Department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "O nome do departamento não pode estar vazío")
    @Size(min = 2, max = 50, message = "O nome do departamento debe ter entre 2 e 50 caracteres")
    private String name;

    public DepartmentDTO() {
    }

    public DepartmentDTO(Department department) {
        this.id = department.getId();
        this.name = department.getName();
    }

    // Getters and Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department toEntity() {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        return department;
    }
}
