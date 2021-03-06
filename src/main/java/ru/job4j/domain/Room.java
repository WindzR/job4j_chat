package ru.job4j.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @NotBlank(message = "Name must be not empty")
    @Size(min = 5, message = "Name length must be more 4 symbols!")
    private String name;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "room_admins",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id"))
    private Set<Person> admins = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "room_person",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> members = new HashSet<>();

    public Room() {
    }

    public static Room of(String name) {
        Room room = new Room();
        room.name = name;
        return room;
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public void addAdmin(Person person) {
        members.remove(person);
        admins.add(person);
    }

    public boolean containsPerson(Person person) {
        return members.contains(person) || admins.contains(person);
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

    public Set<Person> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Person> admins) {
        this.admins = admins;
    }

    public Set<Person> getMembers() {
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Room{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", admins=" + admins
                + ", members=" + members
                + '}';
    }
}
