package ru.polyan.onlinecart.paypal;

import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final OrderService orderService;
    private final ProductService productService;

    @Transactional
    public OrderRequest createOrderRequest(User user, Long orderId) {

        ru.polyan.onlinecart.model.Order order = orderService.findByUserAndId(user, orderId);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("OnlineCart Market")
                .landingPage("BILLING")
                .shippingPreference("SET_PROVIDED_ADDRESS");
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .referenceId(orderId.toString())
                .description("OnlineCart Order")
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("RUB").value(order.getTotalPrice().toString())
                        .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("RUB").value(order.getTotalPrice().toString()))))
                .items(order.getItems().stream()
                        .map(orderItem -> {
                            Long productId = orderItem.getProductId();
                            Product product = productService.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Exception: product id=" + productId + " not found."));
                            Item item = new Item()
                                            .name(product.getTitle())
                                            .unitAmount(new Money().currencyCode("RUB").value(orderItem.getPrice().toString()))
                                            .quantity(String.valueOf(orderItem.getQuantity()));
                            return item;
                        })
                        .collect(Collectors.toList()))
                .shippingDetail(new ShippingDetail().name(new Name().fullName(order.getUser().getUsername()))
                        .addressPortable(new AddressPortable()
                                .addressLine1(order.getAddress_line1())
                                .addressLine2(order.getAddress_line2())
                                .adminArea2(order.getAddress_area1())
                                .adminArea1(order.getAddress_area2())
                                .postalCode(order.getAddress_postcode())
                                .countryCode(order.getAddress_countrycode())));
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);
        return orderRequest;
    }
}
