package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Product;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@ApiModel(description = "Сущность 'Товар'")
public class ProductDto {

    @ApiModelProperty(value = "ID товара")
    private Long id;
    @ApiModelProperty(value = "Наименование товара")
    private String title;
    @ApiModelProperty(value = "Цена товара")
    private BigDecimal price;
    @ApiModelProperty(value = "ID категории")
    private Long categoryId;
    @ApiModelProperty(value = "Наименование категории")
    private String categoryTitle;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.categoryTitle = product.getCategory().getTitle();
    }

}
