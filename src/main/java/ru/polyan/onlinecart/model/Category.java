package ru.polyan.onlinecart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="categories")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;

    @CreationTimestamp
    @Column(name="create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name="update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
