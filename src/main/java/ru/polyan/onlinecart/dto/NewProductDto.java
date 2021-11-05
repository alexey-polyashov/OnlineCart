package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel(description = "Описание нового товара")
public class NewProductDto {

    @ApiModelProperty(value = "Наименование товара")
    @NotBlank
    private String title;
    @ApiModelProperty(value = "Цена товара")
    @Min(value = 0)
    private BigDecimal price;
    @ApiModelProperty(value = "ID категории")
    @NotBlank
    @Min(value = 0)
    private Long categoryId;

}
