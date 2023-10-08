package com.roroapi.userdept.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roroapi.userdept.entities.User;
import com.roroapi.userdept.repositories.DepartmentRepository;
import com.roroapi.userdept.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User save(User user) {
        Optional<User> existingUser = userRepository.findByName(user.getName());

        if (existingUser.isPresent() && 
            !existingUser.get().getDepartment().getId().equals(user.getDepartment().getId())) {
            throw new RuntimeException("El usuario ya está asignado a otro departamento.");
        }

        if (user.getDepartment() != null && user.getDepartment().getId() != null) {
            user.setDepartment(departmentRepository.findById(user.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found")));
        }

        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            Optional<User> userWithName = userRepository.findByName(user.getName());

            if (userWithName.isPresent() && !userWithName.get().getId().equals(id) && 
                !userWithName.get().getDepartment().getId().equals(user.getDepartment().getId())) {
                throw new RuntimeException("El usuario ya está asignado a otro departamento.");
            }

            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());

            if (user.getDepartment() != null && user.getDepartment().getId() != null) {
                existingUser.setDepartment(departmentRepository.findById(user.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found")));
            } else {
                existingUser.setDepartment(null); // If the department is not provided, set it to null
            }

            return userRepository.save(existingUser);
        } else {
            user.setId(id);
            return userRepository.save(user);
        }
    }
}
