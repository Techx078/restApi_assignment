package com.example.demo.Service;

import com.example.demo.Dto.Request.Response.EmployeeDto;
import com.example.demo.Entity.Employee;
import com.example.demo.Repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public Employee save(EmployeeDto employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, EmployeeDto employeeDetails) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        modelMapper.map(employeeDetails, existingEmployee);
        return employeeRepository.save(existingEmployee);
    }

    public Employee partialUpdate(Long id, EmployeeDto partialUpdates) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        modelMapper.map(partialUpdates, existingEmployee);
        return employeeRepository.save(existingEmployee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

}
