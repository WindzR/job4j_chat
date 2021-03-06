package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dao.PersonRepository;
import ru.job4j.dao.RoomRepository;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomDAO;

    private final PersonRepository personDAO;

    public RoomService(final RoomRepository roomDAO, final PersonRepository personDAO) {
        this.roomDAO = roomDAO;
        this.personDAO = personDAO;
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    public Room save(Room room) {
        roomDAO.save(room);
        return room;
    }

    public Optional<Room> findById(int id) {
        return roomDAO.findById(id);
    }

    public void delete(int id) {
        Room room = new Room();
        room.setId(id);
        roomDAO.delete(room);
    }

    public Optional<Room> addPersonToRoom(int id, int personId) {
        var room = roomDAO.findById(id);
        var person = personDAO.findById(personId);
        if (room.isPresent() && person.isPresent()) {
            Room updateRoom = room.get();
            if (!updateRoom.containsPerson(person.get())) {
                updateRoom.addMember(person.get());
            }
            roomDAO.save(updateRoom);
            return Optional.of(updateRoom);
        }
        return Optional.empty();
    }

    public Optional<Room> makePersonAdmin(int id, int personId) {
        var room = roomDAO.findById(id);
        if (room.isPresent()) {
            Room updateRoom = room.get();
            Person updatePerson = updateRoom.getMembers().stream()
                    .filter(pers -> pers.getId() == personId)
                    .findFirst().orElse(null);
            if (updatePerson != null) {
                updateRoom.addAdmin(updatePerson);
            } else {
                return Optional.empty();
            }
            roomDAO.save(updateRoom);
            return Optional.of(updateRoom);
        } else {
            return Optional.empty();
        }
    }
}
