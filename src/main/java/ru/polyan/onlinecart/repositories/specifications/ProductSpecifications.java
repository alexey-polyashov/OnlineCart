package ru.polyan.onlinecart.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> priceGreaterOrEqualsThan(BigDecimal minPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessOrEqualsThan(BigDecimal maxPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
    public static Specification<Product> titleLike(String title) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")), "%" + title + "%");
    }

    public static Specification<Product> category(Category category) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }
}
