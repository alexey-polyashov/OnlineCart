package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
@ApiModel(description = "Сущность 'Новый заказ'")
public class NewOrderDto {
    @ApiModelProperty(value = "Адрес доставки - индекс", example = "134241")
    @NotBlank
    private String addressPostcode;
    @ApiModelProperty(value = "Адрес доставки - код страны", example = "RU")
    @NotBlank
    private String addressCountrycode;
    @ApiModelProperty(value = "Адрес доставки - область/район строка 1", example = "Moscow, Zelenograd")
    @NotBlank
    private String addressArea1;
    @ApiModelProperty(value = "Адрес доставки - область/район строка 2", example = "")
    private String addressArea2;
    @ApiModelProperty(value = "Адрес доставки - строка 1", example = "Panfilovskiy street")
    @NotBlank
    private String addressLine1;
    @ApiModelProperty(value = "Адрес доставки - строка 2", example = "flat 21")
    private String addressLine2;
    @ApiModelProperty(value = "Номер телефона клиента", example = "+7(926)123-23-23")
    @NotBlank
    @Pattern(
            regexp = "^\\+\\d{1}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$",
            message = "Не корректный номер телефона"
    )
    private String phone;
}
