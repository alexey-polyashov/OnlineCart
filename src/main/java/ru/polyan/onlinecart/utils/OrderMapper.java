package ru.polyan.onlinecart.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.polyan.onlinecart.dto.NewOrderDto;
import ru.polyan.onlinecart.model.Order;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Component
public class OrderMapper {
    private final ModelMapper newOrderDtoMapper;

    public OrderMapper() {
        this.newOrderDtoMapper = new ModelMapper();
        newOrderDtoMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        newOrderDtoMapper.createTypeMap(NewOrderDto.class, Order.class)
                .addMapping(NewOrderDto::getAddressPostcode, Order::setAddress_postcode)
                .addMapping(NewOrderDto::getAddressCountrycode, Order::setAddress_countrycode)
                .addMapping(NewOrderDto::getAddressArea1, Order::setAddress_area1)
                .addMapping(NewOrderDto::getAddressArea2, Order::setAddress_area2)
                .addMapping(NewOrderDto::getAddressLine1, Order::setAddress_line1)
                .addMapping(NewOrderDto::getAddressLine2, Order::setAddress_line2)
                .addMapping(NewOrderDto::getPhone, Order::setPhone);
    }

    public Order convertNewOrderDto(NewOrderDto newOrderDto){
        return newOrderDtoMapper.map(newOrderDto, Order.class);
    }
}
