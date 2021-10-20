package ru.polyan.onlinecart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.polyan.onlinecart.model.*;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.utils.CartDetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartDetail cartDetail;

    @MockBean
    public ProductService productService;

//    @Test
//    public void createAndFind(){
//
//        Product p1 = new Product();
//        p1.setId(1L);
//        p1.setPrice(new BigDecimal(100));
//        p1.setTitle("test_product1");
//
//        Product p2 = new Product();
//        p2.setId(2L);
//        p2.setPrice(new BigDecimal(200));
//        p2.setTitle("test_product2");
//
//        Category cat1 = new Category();
//        cat1.setTitle("test_category");
//
//        p1.setCategory(cat1);
//        p2.setCategory(cat1);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("user");
//        user.setAddress("user");
//
//        Mockito.doReturn(Optional.of(p1)).when(productService).getProductByID(1L);
//        Mockito.doReturn(Optional.of(p2)).when(productService).getProductByID(2L);
//
//        cartDetail.addToCart(1L);
//        cartDetail.addToCart(2L);
//
//        orderService.createOrderForUser(user, "email", "12345");
//
//        Order order = orderService.findByUserAndId(user, 1L);
//
//        Assertions.assertEquals(1L, order.getId());
//        Assertions.assertEquals(2, order.getItems().size());
//        BigDecimal bd = new BigDecimal(300);
//        Assertions.assertTrue(bd.compareTo(order.getTotalPrice())==0);
//
//    }


}
