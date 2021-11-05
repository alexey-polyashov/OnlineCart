package ru.polyan.onlinecart.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.polyan.onlinecart.dto.NewOrderDto;
import ru.polyan.onlinecart.dto.OrderItemDto;
import ru.polyan.onlinecart.exception.InvalidInputDataException;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.Order;
import ru.polyan.onlinecart.model.OrderItem;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.repositories.OrdersRepository;
import ru.polyan.onlinecart.utils.CartDetail;
import ru.polyan.onlinecart.utils.OrderMapper;
import ru.polyan.onlinecart.utils.OrderStatus;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OrderMapper orderMapper;


    @Transactional
    public boolean saveOrder(List<OrderItemDto> itemsList, BigDecimal totalPrice){
        Order order= new Order();
        order.setTotalPrice(totalPrice);
        List<OrderItem>  oi = itemsList.stream().map(o->new OrderItem(o)).collect(Collectors.toList());
        oi.stream().forEach(o->o.setOrder(order));
        order.setOrderItemList(oi);
        if(ordersRepository.save(order).getId()!=0){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public void createOrderForUser(Principal principal, String cartUuid, NewOrderDto newOrderDto) {
        User user = userService.findByUsername(principal.getName()).get();
        CartDetail cartDetail = cartService.getCurrentCart(principal, cartUuid);
        List<String> fields = new ArrayList<>();
        if(cartDetail.getDetails().isEmpty()){
            throw new InvalidInputDataException("В корзине нет товаров!");
        }
        Order order = orderMapper.convertNewOrderDto(newOrderDto);
        CartDetail cart = cartService.getCurrentCart(principal, cartUuid);
        order.setTotalPrice(cart.getTotalPrice());
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED.ordinal());
        order.setItems(new ArrayList<>());
        for (OrderItemDto o : cart.getDetails()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(o.getQuantity());
            Product product = productService.getProductByID(o.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())));
            orderItem.setPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            order.getItems().add(orderItem);
        }
        ordersRepository.save(order);
        cartService.clearCart(cartService.getCartUuidFromSuffix(user.getUsername()));
    }

    public void setStatus(Long id, OrderStatus newStatus){
        Order order = ordersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(newStatus.ordinal());
        ordersRepository.save(order);
    }

    public List<Order> findAll(User user) {
        return ordersRepository.findByUser(user);
    }

    public Order findByUserAndId(User user, Long Id) {
        return ordersRepository.findByUserAndId(user, Id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
}
