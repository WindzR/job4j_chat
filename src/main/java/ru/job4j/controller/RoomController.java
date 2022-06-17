package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService rooms;

    public RoomController(final RoomService rooms) {
        this.rooms = rooms;
    }

    /**
     * Получить все Room со всеми участниками
     */
    @GetMapping("/")
    public List<Room> findAll() {
        return rooms.findAll();
    }

    /**
     * Найти Room по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = rooms.findById(id);
        return new ResponseEntity<>(
                room.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room is not found. Please, check required id."
                )),
                HttpStatus.OK
        );
    }

    /**
     * Создать новый Room
     */
    @PostMapping("/")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        validRoom(room);
        return new ResponseEntity<Room>(
                rooms.save(room),
                HttpStatus.CREATED
        );
    }

    /**
     * Изменить конкретный Room
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateRoom(@RequestBody Room room) {
        validRoom(room);
        rooms.save(room);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить Room по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        rooms.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Добавить уже существующего Person в Room
     */
    @PostMapping("/{roomId}/{personId}")
    public ResponseEntity<Room> addPersonToRoom(@PathVariable int roomId,
                                                @PathVariable int personId) {
        Optional<Room> room = rooms.addPersonToRoom(roomId, personId);
        return new ResponseEntity<>(
                room.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room or person is not found. Please, check required room id or person id."
                )),
                HttpStatus.OK
        );
    }

    /**
     * Сделать Person админом в Room
     */
    @PutMapping("/{roomId}/{personId}")
    public ResponseEntity<Room> makePersonAdmin(@PathVariable int roomId,
                                                @PathVariable int personId) {
        var room = rooms.makePersonAdmin(roomId, personId);
        return new ResponseEntity<>(
                room.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room or person is not found. Please, check required room id or person id."
                )),
                HttpStatus.OK
        );
    }

    private void validRoom(Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Name mustn't be empty!");
        }
    }

}
