package ru.polyan.onlinecart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ru.polyan.onlinecart.dto.ProductsRequestDTO;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.services.ProductService;

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

        Assertions.assertEquals(false,productService.findById(100L).isPresent());

    }

    @Test
    public void findAll(){

        Map<String, String> params = new HashMap<>();

        ProductsRequestDTO prodRecDTO = new ProductsRequestDTO();
        prodRecDTO.setRecordsOnPage(5);

        Page<Product> pg = productService.findAll(prodRecDTO);

        Assertions.assertEquals(10, pg.getTotalElements());
        Assertions.assertEquals(2, pg.getTotalPages());


        params.put("maxprice", "15");
        params.put("minprice", "10");

        prodRecDTO.setMinprice(BigDecimal.valueOf(10));
        prodRecDTO.setMaxprice(BigDecimal.valueOf(15));

        pg = productService.findAll(prodRecDTO);

        Assertions.assertEquals(3, pg.getTotalElements());


        prodRecDTO.setTitle("сыр");

        pg = productService.findAll(prodRecDTO);

        Assertions.assertEquals(1, pg.getTotalElements());


        prodRecDTO.setMinprice(BigDecimal.valueOf(-1));
        prodRecDTO.setMaxprice(BigDecimal.valueOf(-1));

        prodRecDTO.setTitle("сыр");

        pg = productService.findAll(prodRecDTO);

        Assertions.assertEquals(2, pg.getTotalElements());

    }

}
