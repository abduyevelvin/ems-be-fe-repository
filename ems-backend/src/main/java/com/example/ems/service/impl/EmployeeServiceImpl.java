package com.example.ems.service.impl;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.entity.EmployeeEntity;
import com.example.ems.exception.ResourceAlreadyExistsException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.ems.mapper.EmployeeMapper.EMPLOYEE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        checkEmailUniqueness(employeeDto.email(), null);

        var employeeEntity = EMPLOYEE_MAPPER.toEmployeeEntity(employeeDto);

        return EMPLOYEE_MAPPER.toEmployeeDto(employeeRepository.save(employeeEntity));
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        return EMPLOYEE_MAPPER.toEmployeeDto(findEmployeeByIdOrThrow(employeeId));
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return Optional.of(employeeRepository.findAll())
                       .filter(employeeList -> !employeeList.isEmpty())
                       .map(EMPLOYEE_MAPPER::toEmployeeDtoList)
                       .orElseGet(() -> {
                           log.warn("No employees found in the database.");
                           return List.of();
                       });
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        var existingEmployee = findEmployeeByIdOrThrow(employeeId);

        checkEmailUniqueness(employeeDto.email(), employeeId);

        existingEmployee.setFirstName(employeeDto.firstName());
        existingEmployee.setLastName(employeeDto.lastName());
        existingEmployee.setEmail(employeeDto.email());

        return EMPLOYEE_MAPPER.toEmployeeDto(employeeRepository.save(existingEmployee));
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        findEmployeeByIdOrThrow(employeeId);

        employeeRepository.deleteById(employeeId);
    }

    private void checkEmailUniqueness(String email, Long excludeEmployeeId) {
        employeeRepository.findByEmail(email)
                          .ifPresent(employee -> {
                              if (!employee.getId()
                                           .equals(excludeEmployeeId)) {
                                  log.error("Employee already exists with email: {}", email);
                                  throw new ResourceAlreadyExistsException(
                                          "Employee already exists with email: " + email);
                              }
                          });
    }

    private EmployeeEntity findEmployeeByIdOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                                 .orElseThrow(() -> {
                                     log.warn("Employee not found with ID: {}", employeeId);
                                     return new ResourceNotFoundException("Employee not found with ID: " + employeeId);
                                 });
    }
}
