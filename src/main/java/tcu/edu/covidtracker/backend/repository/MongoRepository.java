package tcu.edu.covidtracker.backend.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRepository {

    @Autowired
    private MongoInit mongoInit;

    public String USByDate(String date) {
        DBCollection collection = mongoInit.getCollection("United States");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(collection.find(new BasicDBObject("date", date)).next().toString());
        return gson.toJson(je);
    }

    public String USByDateRange(String startDate, String endDate) {
        DBCollection collection = mongoInit.getCollection("United States");
        return getDateRange(startDate, endDate, collection);
    }

    public String allStatesByDate(String date){
        DBCollection collection = mongoInit.getCollection("All States");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(collection.find(new BasicDBObject("date", date)).next().toString());
        return gson.toJson(je);
    }

    public String oneStateByDate(String date, String state){
        DBCollection collection = mongoInit.getCollection(state);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(collection.find(new BasicDBObject("date", date)).next().toString());
        return gson.toJson(je);
    }

    public String stateWithByDateRange(String startDate, String endDate, String state) {
        DBCollection collection = mongoInit.getCollection(state);
        return getDateRange(startDate, endDate, collection);
    }

    public String stateWithoutByDateRange(String startDate, String endDate, String state) {
        DBCollection collection = mongoInit.getCollection(state);
        DBObject dateRange = new BasicDBObject("$gte", startDate);
        dateRange.put("$lte", endDate);
        BasicDBObject query = new BasicDBObject("date", dateRange);
        BasicDBObject fields = new BasicDBObject("state.counties", 0);
        return getJSONString(collection, query, fields);
    }

    public String countyByDate(String date, String state, String county) {
        DBCollection collection = mongoInit.getCollection(state);
        BasicDBObject query = new BasicDBObject("date", date);
        query.put("state.counties.name", county);
        BasicDBObject projection = new BasicDBObject("state.counties.$", 1);
        projection.put("date", 1);
        return getJSONString(collection, query, projection);
    }

    public String countyByDateRange(String startDate, String endDate, String state, String county) {
        DBCollection collection = mongoInit.getCollection(state);
        DBObject dateRange = new BasicDBObject("$gte", startDate);
        dateRange.put("$lte", endDate);
        BasicDBObject query = new BasicDBObject("date", dateRange);
        query.put("state.counties.name", county);
        BasicDBObject projection = new BasicDBObject("state.counties.$", 1);
        projection.put("date", 1);
        return getJSONString(collection, query, projection);
    }

    private String getDateRange(String startDate, String endDate, DBCollection collection) {
        DBObject dateRange = new BasicDBObject("$gte", startDate);
        dateRange.put("$lte", endDate);
        BasicDBObject query = new BasicDBObject("date", dateRange);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        DBCursor cursor = collection.find(query);
        String result = cursor.toArray().toString();
        JsonElement je = jp.parse(result);
        return gson.toJson(je);
    }

    private String getJSONString(DBCollection collection, BasicDBObject query, BasicDBObject projection) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        DBCursor cursor = collection.find(query, projection);
        String result = cursor.toArray().toString();
        JsonElement je = jp.parse(result);
        return gson.toJson(je);
    }

}
