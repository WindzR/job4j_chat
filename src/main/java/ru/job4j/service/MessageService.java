package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dao.MessageRepository;
import ru.job4j.dao.PersonRepository;
import ru.job4j.dao.RoomRepository;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
public class MessageService {

    private final MessageRepository messageDAO;

    private final PersonRepository personDAO;

    private final RoomRepository roomDAO;

    public MessageService(final MessageRepository messageDAO,
                          final PersonRepository personDAO,
                          final RoomRepository roomDAO) {
        this.messageDAO = messageDAO;
        this.personDAO = personDAO;
        this.roomDAO = roomDAO;
    }

    public List<Message> findAll() {
        return messageDAO.findAll();
    }

    public Message save(Message message) {
        messageDAO.save(message);
        return message;
    }

    public Optional<Message> findById(int id) {
        return messageDAO.findById(id);
    }

    public void delete(int id) {
        Message message = new Message();
        message.setId(id);
        messageDAO.delete(message);
    }

    public Optional<Message> updateMessage(int roomId, int personId, Message message) {
        Optional<Room> room = findRoomById(roomId);
        Optional<Person> person = findPersonById(personId);
        return createOrUpdateMessage(message, room, person,
                                        msg -> {
                                                msg.setMessage(message.getMessage());
                                                return msg;
        });
    }

    public Optional<Message> addMessage(int roomId, int personId, Message message) {
        Optional<Room> room = findRoomById(roomId);
        Optional<Person> person = findPersonById(personId);
        return createOrUpdateMessage(message, room, person,
                operatorForAddMessage(room, person));
    }

    public List<Message> findMessageByRoomId(int id) {
        return messageDAO.findMessageByRoomId(id);
    }

    private Optional<Message> createOrUpdateMessage(Message message,
                                                   Optional<Room> room,
                                                   Optional<Person> person,
                                                   UnaryOperator<Message> action) {
        if (room.isPresent()
                && person.isPresent()
                && room.get().containsPerson(person.get())) {
            action.apply(message);
            messageDAO.save(message);
            return Optional.of(message);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Room> findRoomById(int roomId) {
        return roomDAO.findById(roomId);
    }

    private Optional<Person> findPersonById(int personId) {
        return personDAO.findById(personId);
    }

    private UnaryOperator<Message> operatorForAddMessage(Optional<Room> room,
                                                         Optional<Person> person) {
        return msg -> {
                        msg.setRoom(room.get());
                        msg.setAuthor(person.get());
                        return msg;
        };
    }
}
