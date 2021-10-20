package ru.polyan.onlinecart.controllers.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.services.ProductService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductsController {

    private final ProductService productService;

    @GetMapping(value = "/products")
    public String products(){
        return "products";
    }

}
