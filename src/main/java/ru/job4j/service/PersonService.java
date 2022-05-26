package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dao.PersonRepository;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personDAO;

    public PersonService(final PersonRepository personDAO) {
        this.personDAO = personDAO;
    }

    public List<Person> findAll() {
        return personDAO.findAll();
    }

    public Person save(Person person) {
        personDAO.save(person);
        return person;
    }

    public Optional<Person> findById(int id) {
        return personDAO.findById(id);
    }

    public void delete(int id) {
        Person person = new Person();
        person.setId(id);
        personDAO.delete(person);
    }

    public Optional<Person> updateRole(int id, Role role) {
        var person = personDAO.findById(id);
        if (person.isPresent()) {
            Person updatePerson = person.get();
            updatePerson.setRole(role);
            personDAO.save(updatePerson);
            return Optional.of(updatePerson);
        }
        return Optional.empty();
    }
}
