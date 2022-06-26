package ru.job4j.dto;

import ru.job4j.domain.Room;
import ru.job4j.handlers.Operation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RoomDTO {

    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @NotBlank(message = "Name must be not empty")
    @Size(min = 5, message = "Name length must be more 4 symbols!")
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
