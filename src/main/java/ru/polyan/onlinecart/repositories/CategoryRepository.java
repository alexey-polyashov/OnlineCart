package ru.polyan.onlinecart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.polyan.onlinecart.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
