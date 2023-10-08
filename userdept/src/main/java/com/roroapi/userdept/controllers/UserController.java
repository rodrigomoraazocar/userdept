package com.roroapi.userdept.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.roroapi.userdept.dto.UserDTO;
import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.entities.User;
import com.roroapi.userdept.repositories.DepartmentRepository;
import com.roroapi.userdept.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public UserDTO findById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no se encuentra en la base de datos"));
        return new UserDTO(user);
    }

    @PostMapping
    public UserDTO insert(@RequestBody UserDTO userDto) {
        User user = userDto.toEntity();
        if(userDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(userDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(department);
        }
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @PutMapping(value = "/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        
        if(userDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(userDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            existingUser.setDepartment(department);
        }
        
        existingUser = userRepository.save(existingUser);

        return new UserDTO(existingUser);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        // Buscamos el usuario por el ID proporcionado.
        User user = userRepository.findById(id).orElse(null);

        // Si el usuario no existe, retornamos un mensaje apropiado.
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe este usuario para eliminar.");
        }

        // Si el usuario existe, procedemos a eliminarlo.
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso.");
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

}
