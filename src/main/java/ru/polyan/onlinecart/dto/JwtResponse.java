package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "Токен авторизации")
public class JwtResponse {
    private String token;
}
