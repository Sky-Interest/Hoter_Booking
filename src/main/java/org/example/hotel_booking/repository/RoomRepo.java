package org.example.hotel_booking.repository;

import org.example.hotel_booking.pojo.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    // 在RoomRepo中添加
    List<Room> findByStatus(Room.Status status);
}
