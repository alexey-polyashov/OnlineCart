package ru.polyan.onlinecart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.utils.CartDetail;

import java.math.BigDecimal;
import java.util.Optional;


@SpringBootTest
public class CartDetailTest {

    private CartDetail cartDetail;

    @MockBean
    public ProductService productService;

    @MockBean
    public OrderService orderService;

    @Test
    public void addToCart(){

        cartDetail = new CartDetail();

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

        Mockito.doReturn(Optional.of(p1)).when(productService).getProductByID(1L);
        Mockito.doReturn(Optional.of(p2)).when(productService).getProductByID(2L);

        cartDetail.addToCart(productService, 1L);
        cartDetail.addToCart(productService, 1L);
        cartDetail.addToCart(productService, 1L);
        Mockito.verify(productService,Mockito.times(1)).getProductByID(1L);
        Assertions.assertEquals(1, cartDetail.getProducts().size());

        cartDetail.addToCart(productService,2L);
        Mockito.verify(productService,Mockito.times(1)).getProductByID(2L);
        Assertions.assertEquals(2, cartDetail.getProducts().size());
        Assertions.assertEquals(new BigDecimal(500), cartDetail.getTotalPrice());

    }

    @Test
    public void clearCart() {

        cartDetail = new CartDetail();

        Category cat1 = new Category();
        cat1.setTitle("test_category");

        Product p1 = new Product();
        p1.setId(1L);
        p1.setPrice(new BigDecimal(100));
        p1.setTitle("test_product1");
        p1.setCategory(cat1);

        Mockito.doReturn(Optional.of(p1)).when(productService).getProductByID(1L);

        cartDetail.addToCart(productService, 1L);
        Assertions.assertEquals(1, cartDetail.getProducts().size());

        cartDetail.clear();
        Assertions.assertEquals(0, cartDetail.getProducts().size());
        Assertions.assertEquals(new BigDecimal(0), cartDetail.getTotalPrice());

    }

}
