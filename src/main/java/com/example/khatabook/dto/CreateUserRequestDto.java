package com.example.khatabook.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String username;
    private String password;
}
