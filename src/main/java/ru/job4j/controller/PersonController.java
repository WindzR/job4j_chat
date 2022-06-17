package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.UserDetailsServiceImpl;
import ru.job4j.domain.Person;
import ru.job4j.dto.PersonDto;
import ru.job4j.dto.ResponseDto;
import ru.job4j.service.JwtTokenService;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PersonController.class.getSimpleName()
    );

    private final ObjectMapper objectMapper;

    private final PersonService persons;

    private final UserDetailsServiceImpl auth;

    private BCryptPasswordEncoder encoder;

    private final JwtTokenService jwtToken;

    public PersonController(final ObjectMapper objectMapper,
                            final PersonService persons,
                            final UserDetailsServiceImpl auth,
                            BCryptPasswordEncoder encoder,
                            final JwtTokenService jwtToken) {
        this.objectMapper = objectMapper;
        this.persons = persons;
        this.auth = auth;
        this.encoder = encoder;
        this.jwtToken = jwtToken;
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
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person is not found. Please, check required id."
                        )
                ),
                HttpStatus.OK
        );
    }

    /**
     * Регистрация нового пользователя
     * @param person Принимаемая модель пользователя
     */
    @PostMapping("/sign-up")
    public Person signUp(@RequestBody Person person) {
        validPerson(person);
        if (person.getPassword().length() < 5) {
            throw new IllegalArgumentException("Password length must be more 4 symbols!");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return persons.save(person);
    }

    /**
     * Аутентификация пользователя существующего пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginPerson(@RequestBody PersonDto personDto) {
        if (personDto.getUsername() == null
                || personDto.getPassword() == null
                || personDto.getLogin() == null
        ) {
            throw new NullPointerException("Username or password or login mustn't be empty!");
        }
        if (auth.loadUserByUsername(personDto.getUsername()) != null) {
            var personRepository = persons.findByUsername(personDto.getUsername());
            String token = jwtToken.createToken(personRepository.get().getUsername());
            ResponseDto dto = ResponseDto.fromPerson(personRepository.get(), token);
            Optional<ResponseDto> result = Optional.of(dto);
            return new ResponseEntity<ResponseDto>(
                    result.orElse(new ResponseDto()),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Изменить конкретный Person
     */
    @PutMapping("/")
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        validPerson(person);
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
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person is not found. Please, check require id.")
                ),
                HttpStatus.OK
        );
    }

    private void validPerson(Person person) {
        if (person.getUsername() == null
                || person.getPassword() == null
                || person.getLogin() == null
        ) {
            throw new NullPointerException("Username or password or login mustn't be empty!");
        }
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception exception,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", exception.getMessage());
            put("type", exception.getClass());
        }}));
        LOGGER.error(exception.getLocalizedMessage());
    }
}
