package com.example.demo.Dto.Request.Response;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class EmployeeDto {

//    @Null(groups = OnCreate.class,message ="id is not needed at the time of creation" )
//    @NotNull(groups = OnUpdate.class,message ="id needed at the time of update")
//    private long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Department is mandatory")
    @Size(max = 30, message = "Department name must not exceed 30 characters")
    private String department;
}
