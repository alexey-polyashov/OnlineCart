package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Класс используется для представления входящих параметров авторизации")
public class JwtRequest {
    @ApiModelProperty(value = "Логин пользователя")
    private String username;
    @ApiModelProperty(value = "Пароль пользователя")
    private String password;
}
