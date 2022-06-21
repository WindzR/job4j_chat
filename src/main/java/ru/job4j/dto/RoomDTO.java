package ru.job4j.dto;

import ru.job4j.domain.Room;

import java.util.Objects;

public class RoomDTO {

    private int id;

    private String name;

    public RoomDTO() {
    }

    public static RoomDTO fromRoom(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        return roomDTO;
    }

    public Room toRoom() {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        return room;
    }

    public Room patchRoom(Room room) {
        room.setName(name);
        return room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomDTO roomDTO = (RoomDTO) o;
        return id == roomDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RoomDTO{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
