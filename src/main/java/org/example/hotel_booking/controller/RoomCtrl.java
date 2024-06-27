package org.example.hotel_booking.controller;

import org.example.hotel_booking.pojo.Room;
import org.example.hotel_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Page<Room>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Room> rooms = roomService.findAllRooms(pageable);
        return ResponseEntity.ok(rooms);
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
    // 更新房间信息
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable(value = "id") Long roomId, @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(roomId, roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    // 删除房间
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteRoom(@PathVariable(value = "id") Long roomId) {
        roomService.deleteRoom(roomId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
