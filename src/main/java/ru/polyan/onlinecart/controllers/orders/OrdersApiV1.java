package ru.polyan.onlinecart.controllers.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.OrderDto;
import ru.polyan.onlinecart.dto.OrderItemDto;
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
public class OrdersApiV1 {

    private final OrderService orderService;
    private final ProductRepositoryList productRepository;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping(value = "/createorder")
    public ResponseEntity<?> createOrder(HttpServletRequest request, Principal principal, @RequestParam(required = false, defaultValue = "") String address, @RequestParam(required = false, defaultValue = "") String phone) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cartDetail = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        List<String> errors = new ArrayList<>();
        if(cartDetail.getDetails().isEmpty()){
            errors.add("В корзине нет товаров!");
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
        if(address.isEmpty()){
            errors.add("АДРЕС не указан");
        }
        if(phone.isEmpty()){
            errors.add("ТЕЛЕФОН не указан");
        }
        if(!errors.isEmpty()){
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(principal.getName()).get();
        System.out.println(user.getEmail());
        orderService.createOrderForUser(user, uuid, address, phone);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public List<OrderDto> getAllOrders(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return orderService.findAll(user).stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @GetMapping(value="orderdetail/{id}")
    public List<OrderItemDto> getOrderDetail(Principal principal, @PathVariable(name="id") Long orderId) {
        User user = userService.findByUsername(principal.getName()).get();
        Order order = orderService.findByUserAndId(user, orderId);
        return order.getItems().stream().map(oi-> new OrderItemDto(oi, productRepository)).collect(Collectors.toList());
    }
}
