package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.dto.MessageDTO;
import ru.job4j.service.MessageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        return messages.findMessageByRoomId(roomId);
    }

    /**
     * Найти Message по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = messages.findById(id);
        return new ResponseEntity<>(
                message.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message is not found. Please, check required id.")
                ),
                HttpStatus.OK
        );
    }

    /**
     * Создать новый Message от пользователя в комнате
     */
    @PostMapping("/{roomId}/{personId}")
    public ResponseEntity<Message> createMessage(@PathVariable int roomId,
                                                 @PathVariable int personId,
                                                 @RequestBody Message message) {
        validMessage(message);
        Optional<Message> newMessage = messages.addMessage(roomId, personId, message);
        return new ResponseEntity<Message>(
                newMessage.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room or Person is not found. Please, check required room id or person id.")
                ),
                HttpStatus.CREATED
        );
    }

    /**
     * Изменить Message от пользователя в комнате
     */
    @PutMapping("/{roomId}/{personId}")
    public ResponseEntity<Message> updateMessage(@PathVariable int roomId,
                                              @PathVariable int personId,
                                              @RequestBody Message message) {
        validMessage(message);
        Optional<Message> updateMessage = messages.updateMessage(roomId, personId, message);
        return new ResponseEntity<Message>(
                updateMessage.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room or Person is not found. Please, check required room id or person id.")
                ),
                HttpStatus.OK
        );
    }

    /**
     * Принимает messageDTO, находит нужный Message по id и изменяет параметры Message согласно DTO
     * @param messageDTO входящие параметры
     * "id" - @NotNull
     * @return Message с новыми параметрами
     */
    @PatchMapping("/patch")
    public ResponseEntity<Message> patchRoom(@RequestBody MessageDTO messageDTO) {
        validDtoMessage(messageDTO);
        var messageRepository = messages.findById(messageDTO.getId());
        if (messageRepository.isPresent()) {
            Message patchMessage = messageDTO.patchMessage(messageRepository.get());
            messages.save(patchMessage);
            return new ResponseEntity<Message>(
                    patchMessage, HttpStatus.CREATED
            );
        }
        return new ResponseEntity<Message>(
                new Message(),
                new MultiValueMapAdapter<String, String>(
                        Map.of("NOT FOUND", List.of(
                                "Message is not found. Please, check required id.")
                        )),
                HttpStatus.NOT_FOUND
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

    private void validMessage(Message message) {
        if (message.getMessage() == null) {
            throw new NullPointerException("Message mustn't be empty!");
        }
    }

    private void validDtoMessage(MessageDTO messageDTO) {
        if (messageDTO.getId() == 0) {
            throw new NullPointerException("Id mustn't be empty!");
        }
        if (messageDTO.getMessage() == null
                || messageDTO.getAuthor() == null
                || messageDTO.getRoomDTO() == null
        ) {
            throw new NullPointerException("Message or Author or Room mustn't be empty!");
        }
    }
}
