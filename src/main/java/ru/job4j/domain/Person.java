package ru.job4j.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String login;

    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "room_person",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms = new ArrayList<>();

    private boolean enabled;

    public Person() {
    }

    public static Person of(String username, String login) {
        Person person = new Person();
        person.username = username;
        person.login = login;
        person.enabled = true;
        person.role = Role.of("ROLE_USER");
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id && enabled == person.enabled && username.equals(person.username)
                && login.equals(person.login) && Objects.equals(password, person.password)
                && Objects.equals(role, person.role) && Objects.equals(rooms, person.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, login, password, role, enabled);
    }

    @Override
    public String toString() {
        return "Person{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", login='" + login + '\''
                + ", password='" + password + '\''
                + ", role=" + role
                + ", rooms=" + rooms
                + ", enabled=" + enabled
                + '}';
    }
}
