package org.example.hotel_booking.service;

import org.example.hotel_booking.error.ResourceNotFoundException;
import org.example.hotel_booking.pojo.Room;
import org.example.hotel_booking.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Page<Room> findAllRooms(Pageable pageable) {
        return roomRepo.findAll(pageable);
    }
    // 在RoomService中添加
    public List<Room> findAvailableRooms() {
        return roomRepo.findByStatus(Room.Status.AVAILABLE);
    }

    public Room findRoomById(Long id) {
        return roomRepo.findById(id).orElse(null);
    }

    // 更新房间信息
    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setType(roomDetails.getType());
        room.setPrice(roomDetails.getPrice());
        room.setStatus(roomDetails.getStatus());
        room.setDescription(roomDetails.getDescription());
        room.setUpdatedAt(LocalDateTime.now());
        final Room updatedRoom = roomRepo.save(room);
        return updatedRoom;
    }

    // 删除房间
    public void deleteRoom(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id :: " + id));
        room.setStatus(Room.Status.MAINTENANCE); // 将房间状态改为维护中
        roomRepo.save(room);
    }
}
