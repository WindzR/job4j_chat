package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Room;
import ru.job4j.dto.RoomDTO;
import ru.job4j.handlers.Operation;
import ru.job4j.service.RoomService;

import javax.validation.Valid;
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
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
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Room> updateRoom(@Valid @RequestBody Room room) {
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

    /**
     * Принимает RoomDTO, находит нужный Room по id и изменяет параметры Room согласно DTO
     * @param roomDTO входящие параметры
     * "id" - @NotNull
     * @return Room с новыми параметрами
     */
    @PatchMapping("/patch")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Room> patchRoom(@Valid @RequestBody RoomDTO roomDTO) {
        var roomRepository = rooms.findById(roomDTO.getId());
        if (roomRepository.isPresent()) {
            Room patchRoom = roomDTO.patchRoom(roomRepository.get());
            rooms.save(patchRoom);
            return new ResponseEntity<Room>(
                    patchRoom, HttpStatus.CREATED
            );
        }
        return new ResponseEntity<Room>(
                new Room(),
                new MultiValueMapAdapter<String, String>(
                        Map.of("NOT FOUND", List.of(
                                "Person is not found. Please, check require id.")
                        )),
                HttpStatus.NOT_FOUND
        );
    }

}
