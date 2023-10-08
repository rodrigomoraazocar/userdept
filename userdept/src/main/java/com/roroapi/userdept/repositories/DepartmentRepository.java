package com.roroapi.userdept.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roroapi.userdept.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	 Optional<Department> findByName(String name);
}
