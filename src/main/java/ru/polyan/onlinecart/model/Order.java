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

    @Column(name = "status")
    private int status;

    @Column(name = "address_postcode")
    private String address_postcode;
    @Column(name = "address_countrycode")
    private String address_countrycode;
    @Column(name = "address_line1")
    private String address_line1;
    @Column(name = "address_line2")
    private String address_line2;
    @Column(name = "address_area1")
    private String address_area1;
    @Column(name = "address_area2")
    private String address_area2;

    public String getFullAddress(){
        return address_countrycode+
                ", " + address_postcode+
                ", " + address_line1+
                ", " + address_line2+
                ", " + address_area1+
                ", " + address_area2;
    }

}
