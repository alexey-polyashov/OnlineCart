package ru.polyan.onlinecart.controllers.payments;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.services.UserService;
import ru.polyan.onlinecart.utils.PayPalClient;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(value = "/paypal")
public class PayPalController {

    private final PayPalClient payPalClient;
    private final UserService userService;

    @Autowired
    public PayPalController(PayPalClient payPalClient, UserService userService){
        this.payPalClient = payPalClient;
        this.userService = userService;
    }

    @PostMapping(value = "/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
        return payPalClient.createPayment(sum, "");
    }

    @PostMapping(value = "/make/payorder")
    public Map<String, Object> payForOrder(Principal principal, @RequestParam("orderId") Long id){
        User user = userService.findByUsername(principal.getName()).get();
        return payPalClient.payForOrder(user, id);
    }

    @PostMapping(value = "/complete/payment")
    public Map<String, Object> completePayment(Principal principal, @RequestParam("orderId") String orderId, @RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
        User user = userService.findByUsername(principal.getName()).get();
        return payPalClient.completePayment(user, orderId, paymentId, payerId);
    }

}
