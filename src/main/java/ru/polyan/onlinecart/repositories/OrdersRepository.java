package ru.polyan.onlinecart.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.polyan.onlinecart.model.Order;
import ru.polyan.onlinecart.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends CrudRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByUserAndId(User user, Long Id);
}
