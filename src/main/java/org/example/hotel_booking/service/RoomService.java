package org.example.hotel_booking.service;

import org.example.hotel_booking.pojo.Room;
import org.example.hotel_booking.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepo roomRepo;

    @Autowired
    public RoomService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }
    public Room saveRoom(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> findAllRooms() {
        return roomRepo.findAll();
    }

    public Room findRoomById(Long id) {
        return roomRepo.findById(id).orElse(null);
    }
}
