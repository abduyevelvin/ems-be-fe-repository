package com.example.ems.controller;

import com.example.ems.config.SecurityConfig;
import com.example.ems.dto.EmployeeDto;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "CREATE")
    void createEmployee_success() throws Exception {
        // given
        var request = new EmployeeDto(null, "John", "Doe", "john.doe@example.com");
        var response = new EmployeeDto(1L, "John", "Doe", "john.doe@example.com");

        when(employeeService.createEmployee(request))
                .thenReturn(response);

        // when
        var result = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.employeeId").value(1L))
              .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(authorities = "LIST")
    void getEmployee_success() throws Exception {
        // given
        var response = new EmployeeDto(1L, "John", "Doe", "john.doe@example.com");

        when(employeeService.getEmployeeById(1L))
                .thenReturn(response);

        // when
        var result = mockMvc.perform(get("/api/v1/employees/1"));

        // then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.employeeId").value(1L))
              .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(authorities = "LIST")
    void getEmployee_notFound() throws Exception {
        // when
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException("Not found"));

        // then
        mockMvc.perform(get("/api/v1/employees/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "LIST")
    void getEmployees_success() throws Exception {
        // given
        var employees = List.of(
                new EmployeeDto(1L, "John", "Doe", "john.doe@example.com"),
                new EmployeeDto(2L, "Alice", "Smith", "alice.smith@example.com")
        );

        when(employeeService.getAllEmployees())
                .thenReturn(employees);

        // when
        var result = mockMvc.perform(get("/api/v1/employees"));

        // then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.length()").value(2))
              .andExpect(jsonPath("$[0].employeeId").value(1L))
              .andExpect(jsonPath("$[0].firstName").value("John"))
              .andExpect(jsonPath("$[0].lastName").value("Doe"))
              .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
              .andExpect(jsonPath("$[1].employeeId").value(2L))
              .andExpect(jsonPath("$[1].firstName").value("Alice"))
              .andExpect(jsonPath("$[1].lastName").value("Smith"))
              .andExpect(jsonPath("$[1].email").value("alice.smith@example.com"));
    }

    @Test
    @WithMockUser(authorities = "EDIT")
    void updateEmployee_success() throws Exception {
        // given
        var request = new EmployeeDto(null, "John", "Doe", "jane.doe@example.com");
        var response = new EmployeeDto(1L, "Jane", "Doe", "jane.doe@example.com");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class)))
                .thenReturn(response);

        // when
        var result = mockMvc.perform(put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @WithMockUser(authorities = "DELETE")
    void deleteEmployee_success() throws Exception {
        // when - then
        mockMvc.perform(delete("/api/v1/employees/1"))
               .andExpect(status().isNoContent());
    }
}