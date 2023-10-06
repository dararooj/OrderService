package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.PaymentService;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.external.request.PaymentRequest;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
import com.example.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
@Autowired
    OrderRepository orderRepository;
@Autowired
    ProductService productService;
@Autowired
    PaymentService paymentService;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
       // Order Entity->save the data with status order created
        //ProductService->Block products(Reduce the Quantity)
        //PaymentService->Payments->Success->COMPLETE,Else
        //CANCELLED
        log.info("Placing Order Request: {}",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Creating Order with Status CREATED");
        Order order= Order.builder().amount(orderRequest.getTotalAmount()).orderStatus("CREATED")
                .productId(orderRequest.getProductId()).
        orderDate(Instant.now()).quantity(orderRequest.getQuantity()).build();
        order =orderRepository.save(order);
        log.info("Calling Payment Service to Complete the Payment");
        PaymentRequest paymentRequest= PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus=null;
        try {
           paymentService.doPayment(paymentRequest);
           log.info("Payment done Successfully.Changing the Order Status to Placed");
           orderStatus="PLACED";
        }
        catch (Exception e){
          log.error("Error occured in Payment.Changing Order Status to PAYMENT_FAILED");
          orderStatus="PAYMENT_FAiled";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order Placed Successfully with order ID: {}",order.getProductId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get Order Details for order Id: {}",orderId);
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("Order not found for the Order Id: {}"+ orderId,
                        "NOT_FOUND",404));
        OrderResponse orderResponse= OrderResponse.builder()
                .orderID(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .build();
        return orderResponse;
    }
}
