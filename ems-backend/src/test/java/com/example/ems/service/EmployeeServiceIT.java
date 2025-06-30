package com.example.ems.service;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.exception.ResourceAlreadyExistsException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void createEmployee_success() {
        // given
        var employeeDto = createEmployeeMock();

        // when
        var employee = employeeService.createEmployee(employeeDto);

        // then
        assertSoftly(as -> {
            as.assertThat(employee)
              .isNotNull();
            as.assertThat(employee.employeeId())
              .isNotNull();
            as.assertThat(employee.firstName())
              .isEqualTo(employeeDto.firstName());
            as.assertThat(employee.lastName())
              .isEqualTo(employeeDto.lastName());
            as.assertThat(employee.email())
              .isEqualTo(employeeDto.email());
        });
    }

    @Test
    void createEmployee_duplicateEmail_throwsException() {
        // given
        var employeeDto = createEmployeeMock();

        // when
        employeeService.createEmployee(employeeDto);

        // then
        assertThatThrownBy(() -> employeeService.createEmployee(employeeDto))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void getEmployeeById_success() {
        // given
        var employeeDto = createEmployeeMock();
        var saved = employeeService.createEmployee(employeeDto);

        // when
        var employee = employeeService.getEmployeeById(saved.employeeId());

        // then
        assertSoftly(as -> {
            as.assertThat(employee)
              .isNotNull();
            as.assertThat(employee.employeeId())
              .isEqualTo(saved.employeeId());
            as.assertThat(employee.firstName())
              .isEqualTo(employeeDto.firstName());
            as.assertThat(employee.lastName())
              .isEqualTo(employeeDto.lastName());
            as.assertThat(employee.email())
              .isEqualTo(employeeDto.email());
        });
    }

    @Test
    void getEmployeeById_notFound_throwsException() {
        // when - then
        assertThatThrownBy(() -> employeeService.getEmployeeById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateEmployee_success() {
        // given
        var employeeDto = createEmployeeMock();
        var saved = employeeService.createEmployee(employeeDto);
        var updateDto = new EmployeeDto(null, "Jane", "Smith", "jane.smith@example.com");

        // when
        var updated = employeeService.updateEmployee(saved.employeeId(), updateDto);

        // then
        assertSoftly(as -> {
            as.assertThat(updated)
              .isNotNull();
            as.assertThat(updated.employeeId())
              .isEqualTo(saved.employeeId());
            as.assertThat(updated.firstName())
              .isEqualTo(updateDto.firstName());
            as.assertThat(updated.lastName())
              .isEqualTo(updateDto.lastName());
            as.assertThat(updated.email())
              .isEqualTo(updateDto.email());
        });
    }

    @Test
    void updateEmployee_duplicateEmail_throwsException() {
        // given
        var employeeDto = createEmployeeMock();
        employeeService.createEmployee(employeeDto);
        var saved2 = employeeService.createEmployee(
                new EmployeeDto(null, "Alice", "Brown", "alice.brown@example.com"));

        var updateDto = new EmployeeDto(null, "Alice", "Brown", "john.doe@example.com");

        // when - then
        assertThatThrownBy(() -> employeeService.updateEmployee(saved2.employeeId(), updateDto))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void deleteEmployee_success() {
        // given
        var employeeDto = createEmployeeMock();
        var saved = employeeService.createEmployee(employeeDto);

        // when
        employeeService.deleteEmployee(saved.employeeId());

        // then
        assertThat(employeeRepository.findById(saved.employeeId())).isNotPresent();
    }

    @Test
    void deleteEmployee_notFound_throwsException() {
        // when - then
        assertThatThrownBy(() -> employeeService.deleteEmployee(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllEmployees_returnsList() {
        // given
        var employeeDto1 = createEmployeeMock();
        var employeeDto2 = new EmployeeDto(null, "Alice", "Brown", "alice.brown@example.com");
        employeeService.createEmployee(employeeDto1);
        employeeService.createEmployee(employeeDto2);

        // when
        var employees = employeeService.getAllEmployees();

        // then
        assertSoftly(as -> {
            as.assertThat(employees)
              .isNotNull()
              .hasSize(2);
            as.assertThat(employees.get(0)
                                   .firstName())
              .isEqualTo(employeeDto1.firstName());
            as.assertThat(employees.get(1)
                                   .firstName())
              .isEqualTo(employeeDto2.firstName());
        });
    }

    private EmployeeDto createEmployeeMock() {
        return new EmployeeDto(null, "John", "Doe", "john.doe@example.com");
    }
}
