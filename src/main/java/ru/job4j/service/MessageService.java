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

    private Optional<Room> room = Optional.empty();

    private Optional<Person> person = Optional.empty();

    public MessageService(final MessageRepository messageDAO,
                          final PersonRepository personDAO,
                          final RoomRepository roomDAO) {
        this.messageDAO = messageDAO;
        this.personDAO = personDAO;
        this.roomDAO = roomDAO;
    }

    public List<Message> findAll() {
        return (List<Message>) messageDAO.findAll();
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

//    public Optional<Message> addMessage(int roomId, int personId, Message message) {
//        Optional<Room> room = roomDAO.findById(roomId);
//        Optional<Person> person = personDAO.findById(personId);
//        if (room.isPresent()
//                && person.isPresent()
//                && room.get().containsPerson(person.get())) {
//            message.setRoom(room.get());
//            message.setAuthor(person.get());
//            messageDAO.save(message);
//            return Optional.of(message);
//        } else {
//            return Optional.empty();
//        }
//    }

//    public Optional<Message> updateMessage(int roomId, int personId, Message message) {
//        Optional<Room> room = roomDAO.findById(roomId);
//        Optional<Person> person = personDAO.findById(personId);
//        if (room.isPresent()
//                && person.isPresent()
//                && room.get().containsPerson(person.get())) {
//            message.setMessage(message.getMessage());
//            messageDAO.save(message);
//            return Optional.of(message);
//        } else {
//            return Optional.empty();
//        }
//    }

    public Optional<Message> updateMessage(int roomId, int personId, Message message) {
        return createOrUpdateMessage(roomId, personId, message, msg -> {
                                                msg.setMessage(message.getMessage());
                                                return msg;
        });
    }

    public Optional<Message> addMessage(int roomId, int personId, Message message) {
        return createOrUpdateMessage(roomId, personId, message, msg -> {
                                                msg.setRoom(room.get());
                                                msg.setAuthor(person.get());
                                                return msg;
        });
    }

    public Optional<Message> createOrUpdateMessage(int roomId,
                                                   int personId,
                                                   Message message,
                                                   UnaryOperator<Message> action) {
        room = roomDAO.findById(roomId);
        person = personDAO.findById(personId);
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
}
