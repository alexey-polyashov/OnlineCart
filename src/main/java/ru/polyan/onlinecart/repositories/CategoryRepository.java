package ru.polyan.onlinecart.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
