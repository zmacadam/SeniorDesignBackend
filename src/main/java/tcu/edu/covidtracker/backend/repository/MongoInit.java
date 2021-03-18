package tcu.edu.covidtracker.backend.repository;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.springframework.stereotype.Component;

@Component
public class MongoInit {

    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private DB db = mongoClient.getDB("CovidTracker");

    public DBCollection getCollection(String name) {
        return db.getCollection(name);
    }
}
