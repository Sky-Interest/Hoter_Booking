package org.example.hotel_booking.controller;

import org.example.hotel_booking.pojo.Payment;
import org.example.hotel_booking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/payments")
public class PaymentCtrl {
    private final PaymentService paymentService;

    @Autowired
    public PaymentCtrl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.findPaymentById(id);
    }
}
