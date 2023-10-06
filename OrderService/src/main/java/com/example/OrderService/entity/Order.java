package com.example.OrderService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="ORDER_DETAILS")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private  long id;
    @Column(name ="PRODUCT_ID")
    private long productId;
    @Column(name = "QUANTITY")
    private long quantity;
    @Column(name = "ORDER_DATE")
    private Instant orderDate;
    @Column(name = "STATUS")
    private String orderStatus;
    @Column(name = "TOTAL_AMOUNT")
    private long amount;
}
