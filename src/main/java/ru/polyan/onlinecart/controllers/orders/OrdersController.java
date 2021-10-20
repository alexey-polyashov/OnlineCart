package ru.polyan.onlinecart.controllers.orders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrdersController {
    @GetMapping(value = "/orders")
    public String cart(){
        return "orders";
    }
}
