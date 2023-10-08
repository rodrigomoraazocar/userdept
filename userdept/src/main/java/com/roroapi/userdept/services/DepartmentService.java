package com.roroapi.userdept.services;

import java.util.List;
import java.util.Optional;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.exceptions.DepartmentAlreadyExistsException;
import com.roroapi.userdept.exceptions.DepartmentNotFoundException;
import com.roroapi.userdept.repositories.DepartmentRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> findAll() {
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching departments from database", e);
        }
    }

    public Optional<Department> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching department by id from database", e);
        }
    }

    public Department save(Department department) {
        try {
            if (repository.findByName(department.getName()).isPresent()) {
                throw new DepartmentAlreadyExistsException("Departamento ya agregado/existente");
            }
            return repository.save(department);
        } catch (DataAccessException e) {
            if(e.getRootCause() instanceof PSQLException && 
               ((PSQLException) e.getRootCause()).getSQLState().equals("23505")) {
                throw new DepartmentAlreadyExistsException("Departamento ya agregado/existente");
            } else {
                throw new RuntimeException("Error saving department to database", e);
            }
        }
    }

    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting department by id from database", e);
        }
    }

    @Transactional
    public Department update(Long id, Department department) {
        try {
            Optional<Department> existingDepartmentOpt = repository.findById(id);
            if (existingDepartmentOpt.isEmpty()) {
                throw new DepartmentNotFoundException("Department with ID " + id + " not found");
            }

            Department existingDepartment = existingDepartmentOpt.get();
            existingDepartment.setName(department.getName());

            return repository.save(existingDepartment);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating department in database", e);
        }
    }
}
