package ru.job4j.dto;

import ru.job4j.domain.Person;
import ru.job4j.handlers.Operation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class PersonDto {

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

    public PersonDto() {
    }

    public static PersonDto fromPerson(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setUsername(person.getUsername());
        personDto.setLogin(person.getLogin());
        personDto.setPassword(person.getPassword());
        return personDto;
    }

    public Person toPerson() {
        Person person = new Person();
        person.setId(id);
        person.setUsername(username);
        person.setLogin(login);
        person.setPassword(password);
        return person;
    }

    public Person patchPerson(Person patchPerson) {
        patchPerson.setUsername(username);
        patchPerson.setLogin(login);
        patchPerson.setPassword(password);
        return patchPerson;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonDto personDto = (PersonDto) o;
        return id == personDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PersonDto{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", login='" + login + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
