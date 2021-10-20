package ru.polyan.onlinecart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="address")
    private String address;

    @Column(name="phone")
    private String phone;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @CreationTimestamp
    @Column(name="create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name="update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "order")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    List<OrderItem> orderItemList;

}
