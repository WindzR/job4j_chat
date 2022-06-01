package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.UserDetailsServiceImpl;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService persons;

    private final UserDetailsServiceImpl auth;

    private BCryptPasswordEncoder encoder;

    public PersonController(final PersonService persons,
                            final UserDetailsServiceImpl auth,
                            BCryptPasswordEncoder encoder) {
        this.persons = persons;
        this.auth = auth;
        this.encoder = encoder;
    }

    /**
     * Получить всех Person с ролями
     */
    @GetMapping("/all")
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
     * Регистрация нового пользователя
     * @param person Принимаемая модель пользователя
     */
    @PostMapping("/sign-up")
    public Person signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return persons.save(person);
    }

    /**
     * Аутентификация пользователя существующего пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<Person> loginPerson(@RequestBody Person person) {
        if (auth.loadUserByUsername(person.getUsername()) != null) {
            var personRepository = persons.findByUsername(person.getUsername());
            return new ResponseEntity<Person>(
                    personRepository.orElse(new Person()),
                    personRepository.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
     * Изменяет Role У Person, если роль "ROLE_USER", то меняется на "ROLE_ADMIN",
     * иначе - в обратном порядке!
     */
    @PutMapping("/{personId}/role")
    public ResponseEntity<Person> updateRoleOfPerson(@PathVariable int personId) {
        Optional<Person> person = persons.updateRole(personId);
        return new ResponseEntity<Person>(
            person.orElse(new Person()),
            person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
