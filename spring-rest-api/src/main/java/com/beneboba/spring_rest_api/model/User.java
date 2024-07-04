package com.beneboba.spring_rest_api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    @Size(min = 2, max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Age is mandatory")
    @Min(18)
    @Max(99)
    @Column(nullable = false)
    private Integer age;


    @Pattern(regexp = "[A-Z]{2}d{4}")
    @Column(name= "employee_id")
    private String employeeId;
}