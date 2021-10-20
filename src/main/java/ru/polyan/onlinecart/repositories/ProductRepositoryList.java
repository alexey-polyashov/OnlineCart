package ru.polyan.onlinecart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.polyan.onlinecart.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepositoryList  extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(Long id);
//    Page<Product> findByPriceBetween(double minprice, double maxprice, PageRequest pageRequest);
//    Page<Product> findByPriceGreaterThanEqual(double minprice, PageRequest pageRequest);

}

