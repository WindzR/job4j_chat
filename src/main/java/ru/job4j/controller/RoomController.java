package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
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
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Создать новый Room
     */
    @PostMapping("/")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
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
     * Добавить Person в Room
     */
    @PostMapping("/{id}/person")
    public ResponseEntity<Room> addPersonToRoom(@PathVariable int id, Person person) {
        Optional<Room> room = rooms.addPersonToRoom(id, person);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Сделать Person админом в Room
     */
    @PutMapping("/{id}/{personId}")
    public ResponseEntity<Room> makePersonAdmin(@PathVariable int id, @PathVariable int personId) {
        var room = rooms.makePersonAdmin(id, personId);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

}
