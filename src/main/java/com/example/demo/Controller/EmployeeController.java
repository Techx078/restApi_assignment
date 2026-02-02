package com.example.demo.Controller;
import com.example.demo.Dto.Request.Response.EmployeeDto;
import com.example.demo.Entity.Employee;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@RestController
@RequestMapping("v1/api/Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value="/test",headers = "x-api-version=1")
    @Tag(name = "test", description = "test test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping(value="/test",headers = "x-api-version=2")
    @Tag(name = "test", description = "test test")
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok("hello2");
    }

    @GetMapping(value="/{id}",params = "version=1")
    @Tag(name = "Employee by id", description = "get employee by id")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.findById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping(value = "/{id}",params = "version=2")
    @Tag(name = "Employee by id", description = "get employee by id")
    public ResponseEntity<EmployeeDto> getEmployeeById2(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.findById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping("v1/all")
    @Tag(name = "All Employee", description = "Return all employee!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cookies set successfully!"),
    })
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("v2/all")
    @Tag(name = "All Employee", description = "Return all employee!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "cookies set successfully!"),
    })
    public List<Employee> getAllEmployees2() {
        return employeeService.findAll();
    }

    @PostMapping
    @Tag(name = "Create employee", description = "Create Employeee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeDto employeeDTO) {
        Employee savedEmployee = employeeService.save(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Tag(name = "update employee", description = "Update whole employee!")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDTO) {
        Employee updatedEmployee = employeeService.update(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/{id}")
    @Tag(name = "update specific field!", description = "Update specific field!")
    public ResponseEntity<Employee> partialUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeDto partialUpdates) {
        Employee updatedEmployee = employeeService.partialUpdate(id, partialUpdates);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @Tag(name = "delete employee!", description = "Delete employees!")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    @Tag(name = "upload image!", description = "upload image in png format!")
    public ResponseEntity<String> uploadImage(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            Path uploadDir = Paths.get("Uploads");
            Path targetPath = uploadDir.resolve("save_" + id + ".png");
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("File saved as save_" + id + ".png");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error saving file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<UrlResource> getImage(@PathVariable String id) {
        try {
            Path uploadDir = Paths.get("uploads");

            // Find file starting with save_{id}
            try (var files = Files.list(uploadDir)) {
                Path filePath = files
                        .filter(path -> path.getFileName().toString().startsWith("save_" + id))
                        .findFirst()
                        .orElse(null);

                if (filePath == null) {
                    return ResponseEntity.notFound().build();
                }

                // Load file as Resource
                UrlResource resource = new UrlResource(filePath.toUri());
                if (!resource.exists()) {
                    return ResponseEntity.notFound().build();
                }

                // Detect content type
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

