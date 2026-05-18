package com.example.due_test_4.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
