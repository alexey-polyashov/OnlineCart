package ru.polyan.onlinecart.services;

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

//    @Autowired
//    private final RedisTemplate<String, Object> redisTemplate;
//    @Autowired
//    private  ProductService productService;
//    @Autowired
//    private  OrderService orderService;
//
//
//    private static final String CART_PREFIX = "OnlineCart_Cart_";
//    private static final String USER_MAP_PREFIX = "OnlineCart_UserMap_";
//
//    public String generateCart() {
//        String uuid = UUID.randomUUID().toString();
//        CartDetail cart = new CartDetail();
//        redisTemplate.opsForValue().set(CART_PREFIX + uuid, cart);
//        return uuid;
//    }
//
//    public String getUuidForUser(Principal principal, String uuid) {
//
//        String userName = principal!=null ? principal.getName() : "";
//        boolean hasKey = false;
//        if (!userName.isBlank() && redisTemplate.hasKey(USER_MAP_PREFIX + userName)) {
//            String rUuid = (String)redisTemplate.opsForValue().get(USER_MAP_PREFIX + userName);
//            if(rUuid!=uuid){
//                CartDetail guestCart = (CartDetail)redisTemplate.opsForValue().get(CART_PREFIX + uuid);
//                CartDetail cart = (CartDetail)redisTemplate.opsForValue().get(CART_PREFIX + rUuid);
//                if(cart==null){
//                    cart = new CartDetail();
//                }
//                cart.merge(guestCart);
//                redisTemplate.opsForValue().set(CART_PREFIX + rUuid, cart);
//                redisTemplate.delete(CART_PREFIX + uuid);
//            }
//            uuid = rUuid;
//            hasKey = true;
//        }
//        else if(uuid.isBlank()){
//            uuid = generateCart();
//        }
//
//        if(!userName.isBlank() && !hasKey) {
//            redisTemplate.opsForValue().set(USER_MAP_PREFIX + userName, uuid);
//            redisTemplate.opsForValue().set(USER_MAP_PREFIX + uuid, userName);
//        }else if(userName.isBlank()){
//            if(redisTemplate.hasKey(USER_MAP_PREFIX + uuid)){
//                redisTemplate.delete(CART_PREFIX + uuid);
//                redisTemplate.delete(USER_MAP_PREFIX + uuid);
//            }
//        }
//        return uuid;
//    }
//
//    public CartDetail getCurrentCart(Principal principal, String uuid) {
//        uuid = getUuidForUser(principal, uuid);
//        if (!redisTemplate.hasKey(CART_PREFIX + uuid)) {
//            CartDetail cart = new CartDetail();
//            redisTemplate.opsForValue().set(CART_PREFIX + uuid, cart);
//        }
//        CartDetail cart = (CartDetail)redisTemplate.opsForValue().get(CART_PREFIX + uuid);
//        return cart;
//    }
//
//    public void deleteCart(Principal principal, String uuid) {
//        uuid = getUuidForUser(principal, uuid);
//        redisTemplate.opsForValue().set(CART_PREFIX + uuid, null);
//    }
//
//    public void updateCart(Principal principal, String uuid, CartDetail cart) {
//        uuid = getUuidForUser(principal, uuid);
//        redisTemplate.opsForValue().set(CART_PREFIX + uuid, cart);
//    }
//
//    public String getCartUuidFromHeader(HttpServletRequest request){
//        String cartUuid = request.getHeader("CartUuid");
//        return cartUuid;
//    }
//
//    public void clearCart(Principal principal, String uuid){
//        uuid = getUuidForUser(principal, uuid);
//        redisTemplate.opsForValue().set(CART_PREFIX + uuid, new CartDetail());
//    }



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

    public void merge(String userCartKey, String guestCartKey) {
        CartDetail guestCart = getCurrentCart(guestCartKey);
        CartDetail userCart = getCurrentCart(userCartKey);
        userCart.merge(guestCart);
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
        if (principal != null) {
            return getCartUuidFromSuffix(principal.getName());
        }
        return getCartUuidFromSuffix(uuid);
    }
}
