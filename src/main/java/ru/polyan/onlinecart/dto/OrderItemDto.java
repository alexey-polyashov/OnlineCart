package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.OrderItem;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.repositories.ProductRepositoryList;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel(description = "Сущность 'Позиция заказа'")
public class OrderItemDto {

    @ApiModelProperty(value = "ID продукта")
    private Long productId;
    @ApiModelProperty(value = "Описание продукта")
    private ProductDto product;
    @ApiModelProperty(value = "Цена товара")
    private BigDecimal price;
    @ApiModelProperty(value = "Количество товара в заказе")
    private int quantity;
    @ApiModelProperty(value = "Общая стоимость позиции")
    private BigDecimal totalPrice;

    public OrderItemDto(ProductDto product){
        this.quantity=1;
        this.product = product;
        this.price = product.getPrice();
        this.productId = product.getId();
        this.totalPrice = price;
    }

    public OrderItemDto(OrderItem oi, ProductRepositoryList productRepository){
        this.quantity = oi.getQuantity();
        this.price = oi.getPrice();
        this.productId = oi.getId();
        Product prod = productRepository.findById(this.productId).orElseThrow(()->new ResourceNotFoundException("Exception: product id=" + this.productId + " not found."));
        this.product = new ProductDto(prod);
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    public void changeQuantity(int amount){
        quantity += amount;
        totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

}
