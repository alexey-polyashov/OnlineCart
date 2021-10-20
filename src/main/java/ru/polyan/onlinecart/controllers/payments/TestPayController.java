package ru.polyan.onlinecart.controllers.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestPayController {
    @GetMapping(value = "/paypal")
    public String pay(){
        return "pay";
    }
}
