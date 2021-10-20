package ru.polyan.onlinecart.controllers.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.OrderItemDto;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.services.UserService;
import ru.polyan.onlinecart.utils.CartDetail;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cart/")
@Slf4j
public class CartApiV1 {

    private final CartDetail cartDetail;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping(value = "")
    public List<OrderItemDto> getDetail() {
        return cartDetail.getDetail();
    }

    @PostMapping(value = "")
    public void addToCart(@RequestParam Long id) {
        cartDetail.addToCart(id);
    }

    @GetMapping(value = "/getsummary")
    public BigDecimal getSummary() {
        return cartDetail.getSummary();
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestParam Long id, @RequestParam int quantity){
        if(quantity==0){
            cartDetail.deleteItem(id);
        }else{
            cartDetail.decreaseItem(id, quantity);
        }
    }

    @GetMapping(value = "/clear")
    public void clear(){
        cartDetail.clear();
    }

//    @GetMapping(value = "/createorder")
//    public ResponseEntity<?> createOrder(Principal principal) throws ResourceNotFoundException {
//        //cartDetail.createOrder();
//        User user = userService.findByUsername(principal.getName()).get();
//        System.out.println(user.getEmail());
//        orderService.createOrderForUser(user);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}
