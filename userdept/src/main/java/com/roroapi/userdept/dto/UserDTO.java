package com.roroapi.userdept.dto;

import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotNull(message = "ID Naõ pode ser nulo")
    private Long id;
    
    @NotBlank(message = "Name é obrigatorio")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String name;
    
    @NotBlank(message = "Email é obrigatorio")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotNull(message = "ID do departamento é obrigatório")
    private Long departmentId;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.departmentId = user.getDepartment() != null ? user.getDepartment().getId() : null;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        if (departmentId != null) {
            Department department = new Department();
            department.setId(departmentId);
            user.setDepartment(department);
        }
        return user;
    }

}
