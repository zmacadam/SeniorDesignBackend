package tcu.edu.covidtracker.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tcu.edu.covidtracker.backend.model.State;

import java.util.Optional;

@Repository
public interface StateRepository extends MongoRepository<State, String> {
    @Query(value = " { 'name' : ?0, 'stats.date' : ?1 }")
    Optional<State> findByDate(String id, String date);
}
