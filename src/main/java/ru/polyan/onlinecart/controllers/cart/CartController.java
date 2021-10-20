package ru.polyan.onlinecart.controllers.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {
    @GetMapping(value = "/cart")
    public String cart(){
        return "cart";
    }
}