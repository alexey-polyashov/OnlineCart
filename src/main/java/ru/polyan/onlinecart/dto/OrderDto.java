package ru.polyan.onlinecart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Order;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor
@Data
public class OrderDto {
    private Long id;
    private BigDecimal price;
    private String createDate;
    private String address;
    private String phone;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.address = order.getAddress();
        this.phone = order.getPhone();
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("dd MMMM yyyy - HH:mm");
        this.createDate = formatter.format(order.getCreateTime());
    }
}
