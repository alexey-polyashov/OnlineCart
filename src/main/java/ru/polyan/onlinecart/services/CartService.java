package ru.polyan.onlinecart.services;

import jdk.nashorn.internal.runtime.options.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.utils.CartDetail;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private  ProductService productService;
    @Autowired
    private  OrderService orderService;

    @Value("${utils.cart.prefix}")
    private String cartPrefix;

    public String getCartUuidFromSuffix(String suffix) {
        return cartPrefix + suffix;
    }

    public String generateCart() {
        return UUID.randomUUID().toString();
    }

    public CartDetail getCurrentCart(Principal principal, String uuid) {
        String cartKey = getCurrentCartUuid(principal, uuid);
        if (!redisTemplate.hasKey(cartKey)) {
            redisTemplate.opsForValue().set(cartKey, new CartDetail());
        }
        return (CartDetail) redisTemplate.opsForValue().get(cartKey);
    }

    public CartDetail getCurrentCart(String cartKey) {
        if (!redisTemplate.hasKey(cartKey)) {
            redisTemplate.opsForValue().set(cartKey, new CartDetail());
        }
        return (CartDetail) redisTemplate.opsForValue().get(cartKey);
    }

    public void addToCart(String cartKey, Long productId) {
        execute(cartKey, c -> {
            c.addToCart(productService, productId);
        });
    }

    public void clearCart(String cartKey) {
        execute(cartKey, c -> c.clear());
    }

    public void removeItemFromCart(String cartKey, Long productId) {
        execute(cartKey, c -> c.deleteItem(productId));
    }

    public void decrementItem(String cartKey, Long productId) {
        execute(cartKey, c -> c.decreaseItem(productId, 1));
    }

    public void merge(Principal principal, String uuid) {
        CartDetail guestCart = getCurrentCart(null, uuid);
        CartDetail userCart = getCurrentCart(principal, null);
        userCart.merge(guestCart);
        String guestCartKey = getCurrentCartUuid(null, uuid);
        String userCartKey = getCurrentCartUuid(principal, null);
        updateCart(guestCartKey, guestCart);
        updateCart(userCartKey, userCart);
    }

    private void execute(String cartKey, Consumer<CartDetail> action) {
        CartDetail cart = getCurrentCart(cartKey);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartKey, cart);
    }

    public void updateCart(String cartKey, CartDetail cart) {
        redisTemplate.opsForValue().set(cartKey, cart);
    }


    public String getCartUuidFromHeader(HttpServletRequest request){
        String cartUuid = request.getHeader("CartUuid");
        return cartUuid;
    }

    public String getCurrentCartUuid(Principal principal, String uuid) {
        if (principal!=null) {
            return getCartUuidFromSuffix(principal.getName());
        }
        return getCartUuidFromSuffix(uuid);
    }

    public void changeItem(String cartKey, Long id, int quantity) {
        if(quantity==0){
            removeItemFromCart(cartKey, id);
        }else{
            decrementItem(cartKey, id);
        }
    }
}
