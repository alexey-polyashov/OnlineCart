package ru.polyan.onlinecart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.dto.OrderItemDto;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="order_items")
@Data
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem(Long productId, BigDecimal price, int quantity, BigDecimal totalPrice) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public OrderItem(OrderItemDto orderDto) {
        this.productId = orderDto.getProductId();
        this.price = orderDto.getPrice();
        this.quantity = orderDto.getQuantity();
        this.totalPrice = orderDto.getTotalPrice();
    }
}
