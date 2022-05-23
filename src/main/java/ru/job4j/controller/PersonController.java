package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService persons;

    public PersonController(final PersonService persons) {
        this.persons = persons;
    }

    /**
     * Получить всех Person с ролями
     */
    @GetMapping("/")
    public List<Person> findAll() {
        return persons.findAll();
    }

    /**
     * Найти Person по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = persons.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Создать новый Person
     */
    @PostMapping("/")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return new ResponseEntity<Person>(
            persons.save(person),
            HttpStatus.CREATED
        );
    }

    /**
     * Изменить конкретный Person
     */
    @PutMapping("/")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        persons.save(person);
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить Person по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        persons.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Изменить Role У Person
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Person> updateRoleOfPerson(@PathVariable int id, Role role) {
        Optional<Person> person = persons.updateRole(id, role);
        return new ResponseEntity<Person>(
            person.orElse(new Person()),
            person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
