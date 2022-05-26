package ru.job4j.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

    @Query("SELECT DISTINCT room FROM Room room "
            + "JOIN FETCH room.admins admins "
            + "JOIN FETCH room.members members")
    List<Room> findAll();

    @Query("SELECT DISTINCT room FROM Room room "
            + "JOIN FETCH room.admins admins "
            + "JOIN FETCH room.members members "
            + "WHERE room.id = :idParam")
    Optional<Room> findById(@Param("idParam") Integer id);
}
