package com.roroapi.userdept.services;

import com.roroapi.userdept.entities.Department;
import com.roroapi.userdept.repositories.DepartmentRepository;  // Importa DepartmentRepository en lugar de DepartmentRepositoryTests
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;  // Cambiado a DepartmentRepository

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void findAll_ShouldReturnAllDepartments() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

        assertThat(departmentService.findAll()).containsExactly(department);
    }

    @Test
    public void findById_ShouldReturnDepartment() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));

        assertThat(departmentService.findById(1L)).contains(department);
    }

    @Test
    public void save_ShouldReturnSavedDepartment() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Test Department");

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        assertThat(departmentService.save(department)).isEqualTo(department);
    }

    @Test
    public void deleteById_ShouldCallRepositoryDeleteById() {
        departmentService.deleteById(1L);

        verify(departmentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void update_ShouldReturnUpdatedDepartment() {
        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("Existing Department");

        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setName("Updated Department");

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(Department.class))).thenReturn(updatedDepartment);

        assertThat(departmentService.update(1L, updatedDepartment)).isEqualTo(updatedDepartment);
    }

    @Test
    public void handleException_ShouldThrowRuntimeException() {
        when(departmentRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> departmentService.findAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error fetching departments from database")
                .hasCause(new RuntimeException("Database error"));
    }
}
