package ru.polyan.onlinecart.paypal;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.polyan.onlinecart.exception.InvalidInputDataException;
import ru.polyan.onlinecart.exception.MarketError;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.UserService;
import ru.polyan.onlinecart.utils.OrderStatus;


import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/paypal")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PayPalController {
    private final PayPalHttpClient payPalClient;
    private final OrderService orderService;
    private final PayPalService payPalService;
    private final UserService userService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createOrder(Principal principal, @PathVariable Long orderId) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        User user = userService.findByUsername(principal.getName()).get();
        ru.polyan.onlinecart.model.Order cartOrder = orderService.findByUserAndId(user, orderId);
        if(cartOrder.getStatus()==OrderStatus.PAID.ordinal()){
            return new ResponseEntity<>("Заказ уже оплачен", HttpStatus.BAD_REQUEST);
        }
        request.prefer("return=representation");
        request.requestBody(payPalService.createOrderRequest(user, orderId));
        HttpResponse<Order> response = payPalClient.execute(request);
        return new ResponseEntity<>(response.result().id(), HttpStatus.valueOf(response.statusCode()));
    }

    @PostMapping("/capture/{payPalId}")
    public ResponseEntity<?> captureOrder(Principal principal, @PathVariable String payPalId) throws IOException {

        OrdersCaptureRequest request = new OrdersCaptureRequest(payPalId);
        request.requestBody(new OrderRequest());

        HttpResponse<Order> response = payPalClient.execute(request);
        Order payPalOrder = response.result();


        if ("COMPLETED".equals(payPalOrder.status())) {
            User user = userService.findByUsername(principal.getName()).get();
            Long cartOrderId = Long.parseLong(payPalOrder.purchaseUnits().get(0).referenceId());
            ru.polyan.onlinecart.model.Order cartOrder = orderService.findByUserAndId(user, cartOrderId);
            if(cartOrder.getStatus()==OrderStatus.PAID.ordinal()){
                return new ResponseEntity<>("Заказ уже оплачен", HttpStatus.BAD_REQUEST);
            }
            orderService.setStatus(cartOrderId, OrderStatus.PAID);
            return new ResponseEntity<>("Order completed!", HttpStatus.valueOf(response.statusCode()));
        }

        return new ResponseEntity<>(payPalOrder, HttpStatus.valueOf(response.statusCode()));
    }
}
