package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Order;
import ru.polyan.onlinecart.utils.OrderStatus;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor
@Data
@ApiModel(description = "Сущность 'Заказ'")
public class OrderDto {
    @ApiModelProperty(value = "ID заказа")
    private Long id;
    @ApiModelProperty(value = "Общая стоимость заказа")
    private BigDecimal price;
    @ApiModelProperty(value = "Дата создания заказа")
    private String createDate;
    @ApiModelProperty(value = "Адрес доставки")
    private String address;
    @ApiModelProperty(value = "Номер телефона клиента")
    private String phone;
    @ApiModelProperty(value = "Статус заказа")
    private OrderStatus status;
    @ApiModelProperty(value = "Представление статуса заказа")
    private String statusView;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.address =  order.getFullAddress();
        this.phone = order.getPhone();
        this.status = OrderStatus.values()[order.getStatus()];
        this.statusView = this.status.getView();
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("dd MMMM yyyy - HH:mm");
        this.createDate = formatter.format(order.getCreateTime());
    }



}
