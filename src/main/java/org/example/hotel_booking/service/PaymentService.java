package org.example.hotel_booking.service;

import org.example.hotel_booking.pojo.Payment;
import org.example.hotel_booking.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;

    @Autowired
    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public Payment savePayment(Payment payment) {
        return paymentRepo.save(payment);
    }

    public List<Payment> findAllPayments() {
        return paymentRepo.findAll();
    }

    public Payment findPaymentById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }
}
