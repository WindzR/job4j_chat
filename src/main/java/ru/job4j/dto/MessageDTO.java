package ru.job4j.dto;

import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;

import java.util.Objects;

public class MessageDTO {

    private int id;

    private String message;

    private PersonDto author;

    private RoomDTO roomDTO;

    public MessageDTO() {
    }

    public static MessageDTO fromMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        Person author = message.getAuthor();
        Room room = message.getRoom();
        messageDTO.setId(message.getId());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setAuthor(PersonDto.fromPerson(author));
        messageDTO.setRoomDTO(RoomDTO.fromRoom(room));
        return messageDTO;
    }

    public Message toMessage() {
        Message msg = new Message();
        msg.setId(id);
        msg.setMessage(message);
        msg.setAuthor(author.toPerson());
        msg.setRoom(roomDTO.toRoom());
        return msg;
    }

    public Message patchMessage(Message msg) {
        msg.setMessage(message);
        return msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PersonDto getAuthor() {
        return author;
    }

    public void setAuthor(PersonDto author) {
        this.author = author;
    }

    public RoomDTO getRoomDTO() {
        return roomDTO;
    }

    public void setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageDTO that = (MessageDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{"
                + "id=" + id
                + ", message='" + message + '\''
                + ", author=" + author
                + ", room=" + roomDTO
                + '}';
    }
}
