package ru.polyan.onlinecart.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.polyan.onlinecart.dto.OrderItemDto;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.services.OrderService;
import ru.polyan.onlinecart.services.ProductService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class CartDetail {

    private List<OrderItemDto> products;
    private BigDecimal totalPrice;

    private void recalculate() {
        totalPrice = BigDecimal.ZERO;
        for (OrderItemDto oid : products) {
            totalPrice = totalPrice.add(oid.getTotalPrice());
        }
    }

    public CartDetail(){
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public void clear() {
        products.clear();
        totalPrice = BigDecimal.ZERO;
    }

    public boolean add(Long productId) {
        for (OrderItemDto oi : products) {
            if (oi.getProductId().equals(productId)) {
                oi.changeQuantity(1);
                recalculate();
                return true;
            }
        }
        return false;
    }

    public void add(ProductDto product) {
        products.add(new OrderItemDto(product));
        recalculate();
    }

    public void addToCart(ProductService productService, Long id) {
        if (!add(id)) {
            add(new ProductDto(productService.getProductByID(id).orElseThrow(() -> new ResourceNotFoundException("Exception: product id=" + id + " not found."))));
        }
    }

    public void deleteItem(Long id) throws ResourceNotFoundException {
        OrderItemDto oi = products.stream().filter(el->el.getProductId().equals(id)).findFirst().orElseThrow(()-> new ResourceNotFoundException("Product with id - " + id + " is absent in cart"));
        products.remove(oi);
        recalculate();
    }

    public void decreaseItem(Long id, int quantity) throws ResourceNotFoundException {
        OrderItemDto oi = products.stream().filter(el->el.getProductId().equals(id)).findFirst().orElseThrow(()-> new ResourceNotFoundException("Product with id - " + id + " is absent in cart"));
        if(oi.getQuantity()==1){
            products.remove(oi);
        }else{
            oi.changeQuantity(-quantity);
        }
        recalculate();
    }

    public void merge(CartDetail another) {
        if(another==null){
            return;
        }
        for (OrderItemDto anotherItem : another.products) {
            boolean merged = false;
            for (OrderItemDto myItem : products) {
                if (myItem.getProductId().equals(anotherItem.getProductId())) {
                    myItem.changeQuantity(anotherItem.getQuantity());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                products.add(anotherItem);
            }
        }
        recalculate();
        another.clear();
    }

    @JsonIgnore
    public List<OrderItemDto> getDetails(){
        return Collections.unmodifiableList(products);
    }
}

