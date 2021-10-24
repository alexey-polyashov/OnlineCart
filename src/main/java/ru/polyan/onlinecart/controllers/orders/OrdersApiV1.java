package ru.polyan.onlinecart.controllers.orders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.OrderDetailsDto;
import ru.polyan.onlinecart.dto.OrderDto;
import ru.polyan.onlinecart.exception.MarketError;
import ru.polyan.onlinecart.model.Order;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.repositories.ProductRepositoryList;
import ru.polyan.onlinecart.services.CartService;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.UserService;
import ru.polyan.onlinecart.utils.CartDetail;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Api(value = "cart", tags = "Контролер заказов", description = "Управляет заказами")
public class OrdersApiV1 {

    private final OrderService orderService;
    private final ProductRepositoryList productRepository;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping(value = "/createorder")
    @ApiOperation(
            value = "Создание заказа",
            notes = "При вызове создает новый заказ и помещает в него все товары из корзины пользователя." +
                    "Только для авторизованных пользователей."
    )
    @ApiImplicitParams(value={
            @ApiImplicitParam(name="addressPostcode", value = "Адрес. Почтовый индекс", example = "123456", required = true),
            @ApiImplicitParam(name="addressCountrycode", value = "Адрес. Код страны", example = "RU", required = true),
            @ApiImplicitParam(name="addressArea1", value = "Адрес. Регион/область", example = "Московская обл.", required = true),
            @ApiImplicitParam(name="addressArea2", value = "Адрес. Регион/область", example = "г. Зеленоград", required = true),
            @ApiImplicitParam(name="addressLine1", value = "Адрес. Строка адреса", example = "корпус 1805", required = true),
            @ApiImplicitParam(name="addressLine2", value = "Адрес. Строка адреса", example = "кв. 100", required = true),
            @ApiImplicitParam(name="phone", value = "Адрес. Номер телефона", example = "928 764-12-12", required = true)
    })
    public ResponseEntity<?> createOrder(
            HttpServletRequest request,
            Principal principal,
            @RequestParam(required = false, defaultValue = "") String addressPostcode,
            @RequestParam(required = false, defaultValue = "") String addressCountrycode,
            @RequestParam(required = false, defaultValue = "") String addressArea1,
            @RequestParam(required = false, defaultValue = "") String addressArea2,
            @RequestParam(required = false, defaultValue = "") String addressLine1,
            @RequestParam(required = false, defaultValue = "") String addressLine2,
            @RequestParam(required = false, defaultValue = "") String phone
    ) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cartDetail = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        String errors = "";
        List<String> fields = new ArrayList<>();
        if(cartDetail.getDetails().isEmpty()){
            errors = errors.concat("В корзине нет товаров!");
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
        if(addressPostcode.isEmpty()){
            errors = errors.concat("Почтовый код не указан; ");
            fields.add("addressPostcode");
        }
        if(addressCountrycode.isEmpty()){
            errors = errors.concat("Код страны не указан; ");
            fields.add("addressCountrycode");
        }
        if(addressArea1.isEmpty()){
            errors = errors.concat("Страна не указана; ");
            fields.add("addressArea1");
        }
        if(addressArea2.isEmpty()){
            errors = errors.concat("Город не указан; ");
            fields.add("addressArea2");
        }
        if(addressLine1.isEmpty()){
            errors = errors.concat("Улица не указана; ");
            fields.add("addressLine1");
        }
        if(addressLine2.isEmpty()){
            errors = errors.concat("Дом не указан; ");
            fields.add("addressLine2");
        }
        if(phone.isEmpty()){
            errors = errors.concat("Телефон не указан; ");
            fields.add("phoneField");
        }
        if(!errors.isEmpty()){
            MarketError marketError = new MarketError(errors);
            marketError.setFieldErrors(fields);
            return new ResponseEntity(marketError, HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(principal.getName()).get();
        System.out.println(user.getEmail());
        orderService.createOrderForUser(user, uuid,
                addressPostcode,
                addressCountrycode,
                addressArea1,
                addressArea2,
                addressLine1,
                addressLine2,
                phone);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(
            value = "Получить список всех заказов текущего пользователя",
            notes = "Только для авторизованных пользователей."
    )
    public List<OrderDto> getAllOrders(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return orderService.findAll(user).stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @GetMapping(value="orderdetail/{id}")
    @ApiOperation(
            value = "Получить данные заказа",
            notes = "Только для авторизованных пользователей."
    )
    @ApiImplicitParams(value={
            @ApiImplicitParam(name="id", value = "Идентификатор заказа", required = true)
    })
    public OrderDetailsDto getOrderDetail(Principal principal,
                                          @PathVariable(name="id") Long orderId) {
        User user = userService.findByUsername(principal.getName()).get();
        Order order = orderService.findByUserAndId(user, orderId);
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(order, productRepository);
        return orderDetailsDto;
        //return order.getItems().stream().map(oi-> new OrderItemDto(oi, productRepository)).collect(Collectors.toList());
    }
}
