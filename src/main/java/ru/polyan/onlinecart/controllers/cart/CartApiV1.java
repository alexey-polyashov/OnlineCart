package ru.polyan.onlinecart.controllers.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.OrderItemDto;
import ru.polyan.onlinecart.services.CartService;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.services.UserService;
import ru.polyan.onlinecart.utils.CartDetail;
import ru.polyan.onlinecart.utils.StringResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cart/")
@Slf4j
public class CartApiV1 {

    private final CartService cartService;

    @GetMapping(value = "")
    public List<OrderItemDto> getDetail(HttpServletRequest request, Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        return cart.getDetails();
    }

    @PostMapping(value = "")
    public void addToCart(HttpServletRequest request, Principal principal, @RequestParam Long id) {
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.addToCart(cartService.getCurrentCartUuid(principal, uuid), id);
    }

    @GetMapping(value = "/getsummary")
    public BigDecimal getSummary(HttpServletRequest request, Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        return cart.getTotalPrice();
    }

    @GetMapping(value = "/generate")
    public StringResponse generate(HttpServletRequest request, Principal principal) {
        String uuid = cartService.generateCart();
        return new StringResponse(uuid);
    }

    @PostMapping(value = "/delete")
    public void delete(HttpServletRequest request, Principal principal, @RequestParam Long id, @RequestParam int quantity){
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        if(quantity==0){
            cartService.removeItemFromCart(cartService.getCurrentCartUuid(principal, uuid), id);
        }else{
            cartService.decrementItem(cartService.getCurrentCartUuid(principal, uuid), id);
        }
    }

    @GetMapping(value = "/clear")
    public void clear(HttpServletRequest request, Principal principal){
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.clearCart(cartService.getCurrentCartUuid(principal, uuid));
    }

    @GetMapping("/merge")
    public void merge(HttpServletRequest request, Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.merge(
                cartService.getCurrentCartUuid(principal, null),
                cartService.getCurrentCartUuid(null, uuid)
        );
    }

}
