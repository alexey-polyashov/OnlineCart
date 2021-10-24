package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Полное описание заказа (заказ с позициями)")
public class OrderDetailsDto {
    @ApiModelProperty(value = "ID заказа")
    private Long id;
    @ApiModelProperty(value = "Общая стоимость заказа")
    private BigDecimal price;
    @ApiModelProperty(value = "Дата создания заказа")
    private String createDate;
    @ApiModelProperty(value = "Адрес доставки заказа")
    private String address;
    @ApiModelProperty(value = "Номер телефона клиента")
    private String phone;
    @ApiModelProperty(value = "Статус заказа")
    private OrderStatus status;
    @ApiModelProperty(value = "Представление статуса заказа")
    private String statusView;
    @ApiModelProperty(value = "Позиции заказа")
    private List<OrderItemDto> items = new ArrayList<>();

    public OrderDetailsDto(Order order, ProductRepositoryList productRepository) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.address = order.getFullAddress();
        this.phone = order.getPhone();
        this.status = OrderStatus.values()[order.getStatus()];
        this.statusView = status.getView();
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("dd MMMM yyyy - HH:mm");
        this.createDate = formatter.format(order.getCreateTime());
        for(OrderItem oi : order.getItems()){
            items.add(new OrderItemDto(oi, productRepository));
        }
    }
}
