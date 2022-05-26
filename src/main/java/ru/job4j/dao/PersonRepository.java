package ru.job4j.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();

    @Query("SELECT DISTINCT person FROM Person person "
            + "JOIN FETCH person.role role "
            + "WHERE person.id = :idParam")
    Optional<Person> findById(@Param("idParam") Integer id);
}
