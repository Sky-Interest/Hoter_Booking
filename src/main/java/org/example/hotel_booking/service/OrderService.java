package org.example.hotel_booking.service;

import org.example.hotel_booking.pojo.Order;
import org.example.hotel_booking.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    // 新增方法更新订单状态为COMPLETED
    public void updateOrderStatusToCompleted(Long orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(Order.Status.COMPLETED);
            order.setUpdatedAt(LocalDateTime.now()); // 更新订单的updatedAt字段为当前时间
            saveOrder(order); // 保存更新
        }
    }
    public void updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(order);
        } else {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
    }


}
