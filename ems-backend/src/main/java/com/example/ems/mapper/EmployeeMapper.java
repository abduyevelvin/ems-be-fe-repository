package com.example.ems.mapper;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper EMPLOYEE_MAPPER = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "id", target = "employeeId")
    EmployeeDto toEmployeeDto(EmployeeEntity employeeEntity);

    @Mapping(source = "employeeId", target = "id")
    EmployeeEntity toEmployeeEntity(EmployeeDto employeeDto);

    @Mapping(source = "id", target = "employeeId")
    List<EmployeeDto> toEmployeeDtoList(List<EmployeeEntity> employeeEntities);
}
