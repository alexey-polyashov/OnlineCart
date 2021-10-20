package ru.polyan.onlinecart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.repositories.ProductRepositoryList;
import ru.polyan.onlinecart.services.ProductService;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void findById(){

        Assertions.assertEquals(1L, productService.findById(1L).get().getId());
        Assertions.assertEquals(false,productService.findById(10L).isPresent());

    }

    @Test
    public void findAll(){

        Map<String, String> params = new HashMap<>();

        Page<Product> pg = productService.findAll(0, 10, params);

        Assertions.assertEquals(3, pg.getTotalElements());
        Assertions.assertEquals(1, pg.getTotalPages());

        params.put("maxprice", "15");
        params.put("minprice", "10");

        pg = productService.findAll(0, 10, params);

        Assertions.assertEquals(2, pg.getTotalElements());
        Assertions.assertEquals(1, pg.getTotalPages());


        params.put("title", "2");

        pg = productService.findAll(0, 10, params);

        Assertions.assertEquals(1, pg.getTotalElements());
        Assertions.assertEquals(1, pg.getTotalPages());

    }

}
