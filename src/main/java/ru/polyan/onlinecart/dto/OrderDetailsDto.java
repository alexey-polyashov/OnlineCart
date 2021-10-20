package ru.polyan.onlinecart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Order;
import ru.polyan.onlinecart.model.OrderItem;
import ru.polyan.onlinecart.repositories.ProductRepositoryList;
import ru.polyan.onlinecart.utils.OrderStatus;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderDetailsDto {
    private Long id;
    private BigDecimal price;
    private String createDate;
    private String address;
    private String phone;
    private OrderStatus status;
    private List<OrderItemDto> items = new ArrayList<>();

    public OrderDetailsDto(Order order, ProductRepositoryList productRepository) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.address = order.getAddress();
        this.phone = order.getPhone();
        this.status = OrderStatus.values()[order.getStatus()];
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("dd MMMM yyyy - HH:mm");
        this.createDate = formatter.format(order.getCreateTime());
        for(OrderItem oi : order.getItems()){
            items.add(new OrderItemDto(oi, productRepository));
        }
    }
}
