package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Room savedRoom = rooms.save(room);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("CreateRoomHeader", "chat room")
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedRoom);
    }

    /**
     * Изменить конкретный Room
     */
    @PutMapping("/")
    public ResponseEntity<Room> updateRoom(@RequestBody Room room) {
        validRoom(room);
        Room updatedRoom = rooms.save(room);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header("UpdateRoomHeader", "chat room")
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedRoom);
    }

    /**
     * Удалить Room по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable int id) {
        rooms.delete(id);
        String message = "Room with id = " + id + " was deleted!";
        Map<String, String> body = new HashMap<>() {{
            put("DELETED", message);
        }};
        return new ResponseEntity<>(
                body,
                new MultiValueMapAdapter<String, String>(
                        Map.of("DeleteRoom", List.of("delete room in chat"))
                ),
                HttpStatus.OK
        );
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
