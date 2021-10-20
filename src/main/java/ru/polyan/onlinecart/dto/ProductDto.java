package ru.polyan.onlinecart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Product;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private Long categoryId;
    private String categoryTitle;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.categoryTitle = product.getCategory().getTitle();
    }

}
