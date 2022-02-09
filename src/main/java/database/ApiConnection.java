package database;
import com.mongodb.client.MongoCursor;
import database.MongoDBConnectionHandler;

import org.bson.Document;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.*;


public class ApiConnection {

    public static void runApi() {
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();

        get("/speeches/:id", (request, response) -> {
            Document doc = handler.getDBDocument(request.params(":id"), "aspeeches");
            String json = com.mongodb.util.JSON.serialize(doc);
            return json;
        });

        get("/comments/:id", (request, response) -> {
            String id = request.params(":id") + "-";
            Document match = Document.parse("{$match:{_id: /" + id + "/i}}");
            MongoCursor cursor = handler.getCollection("acomments").aggregate(Arrays.asList(match)).cursor();
            String json;
            StringBuilder finalJson = new StringBuilder();
            while (cursor.hasNext()) {
                Document doc = (Document) cursor.next();
                json = com.mongodb.util.JSON.serialize(doc);
                finalJson.append(json);
            }
            return finalJson.toString();
        });

        get("/speaker/:id", (request, response) -> {
            Document doc = handler.getDBDocument(request.params(":id"), "amembers");
            String json = com.mongodb.util.JSON.serialize(doc);
            return json;
        });

        get("/party", (request, response) -> {
            HashMap<String, String> partyMembers = new HashMap<>();
            String[] allParties = {"BÜNDNIS 90", "AfD", "CDU", "CSU", "FDP", "SPD", "Plos"};
            for (int i = 0; i < allParties.length; i++) {
                String currentParty = allParties[i];
                Document match = Document.parse("{$match:{party: /" + currentParty + "/i}}");
                MongoCursor cursor = handler.getCollection("amembers").aggregate(Arrays.asList(match)).cursor();
                int counter = 0;
                while (cursor.hasNext()) {
                    counter += 1;
                    cursor.next();
                }
                String stringCounter = String.valueOf(counter);
                partyMembers.put(currentParty, stringCounter);
            }
            JSONObject json = new JSONObject(partyMembers);
            return json.toJSONString();
        });


        get("/api/test", (request, response) -> {
            return "Hallo Welt !";
        });

        get("/api/close", (request, response) -> {
            stop();
            return "Shutdown Server";
        });
    }
}
