package com.roroapi.userdept.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;

import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.exceptions.DepartmentAlreadyExistsException;
import com.roroapi.userdept.exceptions.DepartmentNotFoundException;
import com.roroapi.userdept.services.DepartmentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        return new ResponseEntity<>(departmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Department> findById(@PathVariable Long id) {
        return departmentService.findById(id)
                .map(department -> new ResponseEntity<>(department, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento buscado no se encuentra en la base de datos"));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Department department) {
        try {
            Department savedDepartment = departmentService.save(department);
            return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
        } catch (DepartmentAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Department department) {
        try {
            Department updatedDepartment = departmentService.update(id, department);
            return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
        } catch (DepartmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Department department = departmentService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento buscado no se encuentra en la base de datos"));
            
            departmentService.deleteById(id);
            
            String successMessage = "Departamento " + department.getName() + " foi deletado com sucesso.";
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
            
        } catch (DataIntegrityViolationException e) {
            return handleDataIntegrityViolation();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof DataIntegrityViolationException) {
                return handleDataIntegrityViolation();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

    private ResponseEntity<String> handleDataIntegrityViolation() {
        String message = "Não pode excluir este departamento porque está vinculado a um ou mais usuários. Certifique-se de desvincular ou reatribuir todos os usuários associados a este departamento antes de tentar excluí-lo.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<String> handleDepartmentAlreadyExistsException(DepartmentAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String> handleDepartmentNotFoundException(DepartmentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getReason());
        error.setStatus(ex.getStatusCode().value());
        error.setPath(request.getRequestURI());

        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}

class ErrorResponse {
    private String message;
    private int status;
    private String path;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
