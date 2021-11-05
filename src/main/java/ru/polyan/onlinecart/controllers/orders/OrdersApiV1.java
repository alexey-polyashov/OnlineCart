package ru.polyan.onlinecart.controllers.orders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.NewOrderDto;
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
import javax.validation.Valid;
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
    public ResponseEntity<?> createOrder(
            HttpServletRequest request,
            Principal principal,
            @Valid @RequestBody NewOrderDto newOrder
    ) {
        String uuid = cartService.getCartUuidFromHeader(request);
        orderService.createOrderForUser(principal, uuid,newOrder);
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
    }
}
