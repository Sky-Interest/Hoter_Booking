package org.example.hotel_booking.controller;

import org.example.hotel_booking.pojo.Order;
import org.example.hotel_booking.pojo.Payment;
import org.example.hotel_booking.service.OrderService;
import org.example.hotel_booking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentCtrl {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Autowired
    public PaymentCtrl(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        if (payment.getOrder() != null && payment.getOrder().getId() != null) {
            Order order = orderService.findOrderById(payment.getOrder().getId());
            if (order != null) {
                payment.setOrder(order);
                payment.setPaymentDate(LocalDateTime.now()); // 设置支付时间为当前时间
                Payment savedPayment = paymentService.savePayment(payment);
                // 假设支付成功，更新订单状态为COMPLETED，并更新房间状态为BOOKED
                orderService.updateOrderStatusToCompleted(order.getId());
                return savedPayment;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is required");
        }
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.findPaymentById(id);
    }

    // 如果需要更新支付状态或其他信息的方法
    @PutMapping("/{id}")
    public Payment updatePaymentStatus(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Payment payment = paymentService.findPaymentById(id);
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        payment.setStatus(paymentDetails.getStatus());
        // 更新其他字段...
        return paymentService.savePayment(payment);
    }
}
