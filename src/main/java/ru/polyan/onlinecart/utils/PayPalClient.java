//package ru.polyan.onlinecart.utils;
//
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import ru.polyan.onlinecart.model.Order;
//import ru.polyan.onlinecart.model.User;
//import ru.polyan.onlinecart.services.OrderService;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class PayPalClient {
//
//    private final OrderService orderService;
//
//    @Value("${paypal.client-id}")
//    private String clientId;
//    @Value("${paypal.client-secret}")
//    private String clientSecret;
//    @Value("${paypal.mode}")
//    private String clientMode;
//
//    public Map<String, Object> createPayment(String sum, String orderId){
//
//        Map<String, Object> response = new HashMap<String, Object>();
//
//        Amount amount = new Amount();
//        amount.setCurrency("RUB");
//        amount.setTotal(sum);
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        List<Transaction> transactions = new ArrayList<Transaction>();
//        transactions.add(transaction);
//
//        Payer payer = new Payer();
//        payer.setPaymentMethod("paypal");
//
//        Payment payment = new Payment();
//        payment.setIntent("sale");
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
//
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl("http://localhost:8189/onlinecart/paycancel");
//        redirectUrls.setReturnUrl("http://localhost:8189/onlinecart/orders?orderId=" + orderId);
//        payment.setRedirectUrls(redirectUrls);
//        Payment createdPayment;
//        try {
//            String redirectUrl = "";
//            APIContext context = new APIContext(clientId, clientSecret, clientMode);
//            createdPayment = payment.create(context);
//            if(createdPayment!=null){
//                List<Links> links = createdPayment.getLinks();
//                for (Links link:links) {
//                    if(link.getRel().equals("approval_url")){
//                        redirectUrl = link.getHref();
//                        break;
//                    }
//                }
//                response.put("status", "success");
//                response.put("redirect_url", redirectUrl);
//            }
//        } catch (PayPalRESTException e) {
//            System.out.println("Error happened during payment creation!");
//        }
//        return response;
//    }
//
//    public Map<String, Object> payForOrder(User user, Long orderId){
//
//        Map<String, Object> response = new HashMap<String, Object>();
//
//        ru.polyan.onlinecart.model.Order order = orderService.findByUserAndId(user, orderId);
//
//        return createPayment(order.getTotalPrice().toString(), orderId.toString());
//    }
//
//    public Map<String, Object> completePayment(User user, String orderId, String paymentId, String payerId){
//        Map<String, Object> response = new HashMap();
//        Payment payment = new Payment();
//        payment.setId(paymentId);
//
//        PaymentExecution paymentExecution = new PaymentExecution();
//        paymentExecution.setPayerId(payerId);
//        try {
//            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
//            Payment createdPayment = payment.execute(context, paymentExecution);
//            if(createdPayment!=null){
//
//                orderService.setStatus(Long.valueOf(orderId), "Оплачен");
//
//                response.put("status", "success");
//                response.put("payment", createdPayment);
//            }
//        } catch (PayPalRESTException e) {
//            System.err.println(e.getDetails());
//        }
//        return response;
//    }
//
//}
