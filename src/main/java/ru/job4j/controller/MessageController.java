package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messages;

    public MessageController(final MessageService messages) {
        this.messages = messages;
    }

    /**
     * Получить все Message всех участников
     */
    @GetMapping("/")
    public List<Message> findAll() {
        return messages.findAll();
    }

    /**
     * Получить все Message всех участников в определенной комнате
     */
    @GetMapping("/room/{roomId}")
    public List<Message> findAllMessagesInRoom(@PathVariable int roomId) {
        return messages.findAll().stream()
                .filter(message -> message.getRoom().getId() == roomId)
                .collect(Collectors.toList());
    }

    /**
     * Найти Message по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var room = messages.findById(id);
        return new ResponseEntity<>(
                room.orElse(new Message()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Создать новый Message от пользователя в комнате
     */
    @PostMapping("/{roomId}/{personId}")
    public ResponseEntity<Message> createMessage(@PathVariable int roomId,
                                                 @PathVariable int personId,
                                                 @RequestBody Message message) {
        Optional<Message> newMessage = messages.addMessage(roomId, personId, message);
        return new ResponseEntity<Message>(
                newMessage.orElse(new Message()),
                newMessage.isPresent() ? HttpStatus.CREATED : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Изменить Message от пользователя в комнате
     */
    @PutMapping("/{roomId}/{personId}")
    public ResponseEntity<Message> updateMessage(@PathVariable int roomId,
                                              @PathVariable int personId,
                                              @RequestBody Message message) {
        Optional<Message> updateMessage = messages.updateMessage(roomId, personId, message);
        return new ResponseEntity<Message>(
                updateMessage.orElse(new Message()),
                updateMessage.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Удалить Message по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        messages.delete(id);
        return ResponseEntity.ok().build();
    }
}