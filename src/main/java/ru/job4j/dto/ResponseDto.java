package ru.job4j.dto;

import ru.job4j.domain.Person;

import java.util.Objects;

public class ResponseDto {

    private int id;

    private String username;

    private String token;

    public ResponseDto() {
    }

    public static ResponseDto fromPerson(Person person, String token) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(person.getId());
        responseDto.setUsername(person.getUsername());
        responseDto.setToken(token);
        return responseDto;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseDto that = (ResponseDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ResponseDto{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", token='" + token + '\''
                + '}';
    }
}
