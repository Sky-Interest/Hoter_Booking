package org.example.hotel_booking.repository;

import org.example.hotel_booking.pojo.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PaymentRepo extends JpaRepository<Payment, Long> {
}
