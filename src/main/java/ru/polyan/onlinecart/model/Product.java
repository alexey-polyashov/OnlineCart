package ru.polyan.onlinecart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.polyan.onlinecart.dto.ProductDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name="price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @CreationTimestamp
    @Column(name="create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name="update_time")
    private LocalDateTime updateTime;

    public Product(Long id, String title, BigDecimal price, Category category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
    }

}
