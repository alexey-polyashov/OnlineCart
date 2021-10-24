package ru.polyan.onlinecart.controllers.cart;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.OrderItemDto;
import ru.polyan.onlinecart.services.CartService;
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
@Api(value = "cart", tags = "Контролер корзины", description = "Получает данные корзины, управляет ее содержимым")
public class CartApiV1 {

    private final CartService cartService;

    @GetMapping(value = "")
    @ApiOperation(
            value = "Постраничное получение списка продуктов с использованием фильтров"
    )
    public List<OrderItemDto> getDetail(HttpServletRequest request,Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        return cart.getDetails();
    }

    @PostMapping(value = "")
    @ApiOperation(
            value = "Постраничное получение списка продуктов с использованием фильтров",
            notes = "Если товар уже есть в корзине, то увеличивается количество товара в корзине"
    )
    @ApiImplicitParams(value={
            @ApiImplicitParam(name="id", value = "ID товара", required = true)
    })
    public void addToCart(HttpServletRequest request, Principal principal,
                          @RequestParam Long id) {
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.addToCart(cartService.getCurrentCartUuid(principal, uuid), id);
    }

    @GetMapping(value = "/getsummary")
    @PostMapping(value = "")
    @ApiOperation(
            value = "Получение итоговой стоимости корзины",
            notes = "В заголовке должен быть передан параметр CartUuid, иначе будут получены данные пустой корзины"
    )
    public BigDecimal getSummary(HttpServletRequest request, Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        return cart.getTotalPrice();
    }

    @GetMapping(value = "/generate")
    @ApiOperation(
            value = "Генерация корзины",
            notes = "Вызывается для получения UID корзины, когда пользователь не авторизован." +
                    "Полученный UID необходимо вставлять в заголовок (параметр CartUuid) при каждом запросе к корзине." +
                    "При отсутствии UID в заголовке запроса для не авторизованного пользователя, данные корзины не будут сохраняться."
    )
    public StringResponse generate(HttpServletRequest request, Principal principal) {
        String uuid = cartService.generateCart();
        return new StringResponse(uuid);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(
            value = "Удаление позиции из корзины, уменьшение количества выбранной позиции",
            notes = "В заголовке должен быть передан параметр CartUuid, иначе запрос не сможет быть применен к корзине"
    )
   @ApiImplicitParams(value={
           @ApiImplicitParam(name="id", value = "ID товара", required = true),
           @ApiImplicitParam(name="quantity", value = "Количество, на которое нужно изменить количество товара в корзине. Если передан 0, то удаляется весь товар", required = true)
    })
    public void delete(HttpServletRequest request, Principal principal,
                       @RequestParam Long id,
                       @RequestParam int quantity){
        String uuid = cartService.getCartUuidFromHeader(request);
        CartDetail cart = cartService.getCurrentCart(cartService.getCurrentCartUuid(principal, uuid));
        if(quantity==0){
            cartService.removeItemFromCart(cartService.getCurrentCartUuid(principal, uuid), id);
        }else{
            cartService.decrementItem(cartService.getCurrentCartUuid(principal, uuid), id);
        }
    }

    @GetMapping(value = "/clear")
    @ApiOperation(
            value = "Очистка всей корзины",
            notes = "В заголовке должен быть передан параметр CartUuid, иначе запрос не сможет быть применен к корзине"
    )
    public void clear(HttpServletRequest request, Principal principal){
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.clearCart(cartService.getCurrentCartUuid(principal, uuid));
    }

    @GetMapping("/merge")
    @ApiOperation(
            value = "Слияние корзины авторизованного пользователя с корзиной, которая была до авторизации",
            notes = "Данные метод нужно вызывать сразу после авторизации пользователя." +
                    "В заголовке должен быть передан параметр CartUuid, иначе запрос не сможет быть применен."
    )
    public void merge(HttpServletRequest request, Principal principal) {
        String uuid = cartService.getCartUuidFromHeader(request);
        cartService.merge(
                cartService.getCurrentCartUuid(principal, null),
                cartService.getCurrentCartUuid(null, uuid)
        );
    }

}
