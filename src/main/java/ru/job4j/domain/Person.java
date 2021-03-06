package ru.job4j.domain;

import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @NotBlank(message = "Username must be not empty")
    private String username;

    @NotBlank(message = "Login must be not empty")
    private String login;

    @NotBlank(message = "Password must be not empty")
    @Size(min = 5, message = "Password length must be more 4 symbols!")
    private String password;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role role;

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
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", login='" + login + '\''
                + ", password='" + password + '\''
                + ", role=" + role
                + ", enabled=" + enabled
                + '}';
    }
}
