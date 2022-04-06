package ru.polyan.onlinecart;

import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.polyan.onlinecart.dto.NewOrderDto;
import ru.polyan.onlinecart.model.*;
import ru.polyan.onlinecart.services.CartService;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.utils.CartDetail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    private CartDetail cartDetail;

    @MockBean
    public ProductService productService;
    @MockBean
    public CartService cartService;

    class PrincipalImpl implements Principal {

        @Override
        public String getName() {

            return "user";
        }

    }

    @Test
    public void createAndFind(){

        PrincipalImpl principal = new PrincipalImpl();

        cartDetail = new CartDetail();
        Mockito.doReturn(cartDetail).when(cartService).getCurrentCart(principal,"test_uid");
        Mockito.doReturn("test_uid").when(cartService).getCartUuidFromSuffix(principal.getName());

        Product p1 = new Product();
        p1.setId(1L);
        p1.setPrice(new BigDecimal(100));
        p1.setTitle("test_product1");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setPrice(new BigDecimal(200));
        p2.setTitle("test_product2");

        Category cat1 = new Category();
        cat1.setTitle("test_category");

        p1.setCategory(cat1);
        p2.setCategory(cat1);

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setAddress("user");

        Mockito.doReturn(Optional.of(p1)).when(productService).getProductByID(1L);
        Mockito.doReturn(Optional.of(p2)).when(productService).getProductByID(2L);

        cartDetail.addToCart(productService, 1L);
        cartDetail.addToCart(productService, 2L);

        NewOrderDto newOrder = new NewOrderDto();
        newOrder.setAddressPostcode("123");
        newOrder.setAddressCountrycode("7");
        newOrder.setAddressPostcode("123");
        newOrder.setAddressArea1("123");
        newOrder.setAddressArea2("123");
        newOrder.setAddressLine1("123");
        newOrder.setAddressLine2("123");
        newOrder.setPhone("123");

        orderService.createOrderForUser(principal, "test_uid", newOrder);

        Order order = orderService.findByUserAndId(user, 1L);

        Assertions.assertEquals(1L, order.getId());
        Assertions.assertEquals(2, order.getItems().size());
        BigDecimal bd = new BigDecimal(300);
        Assertions.assertTrue(bd.compareTo(order.getTotalPrice())==0);

    }


}
