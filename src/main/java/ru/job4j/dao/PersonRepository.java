package ru.job4j.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
}
