package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dao.PersonRepository;
import ru.job4j.dao.RoleRepository;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personDAO;

    private final RoleRepository roleDAO;

    public PersonService(final PersonRepository personDAO, final RoleRepository roleDAO) {
        this.personDAO = personDAO;
        this.roleDAO = roleDAO;
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

    public Optional<Person> findByUsername(String username) {
        return personDAO.findByUsername(username);
    }

    public void delete(int id) {
        Person person = new Person();
        person.setId(id);
        personDAO.delete(person);
    }

    public Optional<Person> updateRole(int id) {
        var person = personDAO.findById(id);
        List<Role> roles = roleDAO.findAll();
        if (person.isPresent()) {
            Person updatePerson = person.get();
            Role currentRole = updatePerson.getRole();
            Role alterRole = findAlternativeRole(roles, currentRole);
            updatePerson.setRole(alterRole);
            personDAO.save(updatePerson);
            return Optional.of(updatePerson);
        }
        return Optional.empty();
    }

    private Role findAlternativeRole(List<Role> roles, Role currentRole) {
        if ("ROLE_USER".equals(currentRole.getRole())) {
            return roles.stream()
                    .filter(role -> "ROLE_ADMIN".equals(role.getRole()))
                    .findFirst().orElse(null);
        }
        return roles.stream()
                .filter(role -> "ROLE_USER".equals(role.getRole()))
                .findFirst().orElse(null);
    }
}
