package tcu.edu.covidtracker.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tcu.edu.covidtracker.backend.model.UnitedStates;

@Repository
public interface USRepository extends MongoRepository<UnitedStates, String> {
}
