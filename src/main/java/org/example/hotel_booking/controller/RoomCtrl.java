package org.example.hotel_booking.controller;

import org.example.hotel_booking.pojo.Room;
import org.example.hotel_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/rooms")
public class RoomCtrl {
    private final RoomService roomService;

    @Autowired
    public RoomCtrl(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomService.saveRoom(room);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAllRooms();
    }

    // 在RoomCtrl中添加
    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.findAvailableRooms();
    }
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.findRoomById(id);
    }
}
