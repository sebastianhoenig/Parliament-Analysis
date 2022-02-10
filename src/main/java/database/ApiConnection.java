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

    public static void main (String[] args) {
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

        get("/api/close", (request, response) -> {
            stop();
            return "Shutdown Server";
        });

        // TODO: beginDaten and endDate
        get("/token", (request, response) -> {
            response.type("application/json;charset=UTF-8");

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            Document doc;
            MongoCursor cursor = null;
            HashMap<String, Integer> allToken = new HashMap<>();
            if (! request.queryMap().hasKeys()) {
                Document project = Document.parse("{$project: {\"token\": \"$token\"}}");
                cursor = handler.getCollection("aspeeches").aggregate(
                        Arrays.asList(project)).cursor();
            } else if (speakerID != null && party == null) {
                doc = handler.getDBDocument(speakerID, "amembers");
                if (doc == null) {
                    JSONObject json = new JSONObject();
                    json.put("message", String.format("No user with id '%s' found", speakerID));
                    response.status(400);
                    return json.toJSONString();
                }
                Document match = Document.parse("{$match : {\"speaker\" : \"" + speakerID +"\" }}");
                Document group = Document.parse("{$group: {_id : \"$speaker\", allToken : {$push : \"$token\"}}}");
                Document project = Document.parse("{$project : {" +
                        "_id : \"$_id\"," +
                        "token : {" +
                        "$reduce : {" +
                        "input:\"$allToken\"," +
                        "initialValue: [ ]," +
                        "in: { $concatArrays: [ \"$$value\", \"$$this\" ] }" +
                        "}}}}");
                cursor = handler.getCollection("aspeeches").aggregate(
                        Arrays.asList(
                                match,
                                group,
                                project
                        )
                ).cursor();
            } else if (speakerID == null && party != null) {
                doc = (Document) handler.getCollection("amembers").aggregate(
                        Arrays.asList(
                                Document.parse("{$match: {\"party\" : \"" + party + "\"}}"),
                                Document.parse("{$group:{_id: \"$party\", ids: { $push:  \"$_id\" }}}")
                        )
                ).first();
                if (doc != null) {
                    List<String> idList = (ArrayList<String>) doc.get("ids");
                    idList = idList.stream().map((String id) -> id = "\"" + id + "\"").collect(Collectors.toList());
                    cursor = handler.getCollection("aspeeches").aggregate(
                            Arrays.asList(
                                    Document.parse("{$match: {\"speaker\" : {$in : " + idList + "}}}"),
                                    Document.parse("{$project : {_id: \"all\", token : \"$token\"}}")
                            )
                    ).cursor();
                } else {
                    JSONObject json = new JSONObject();
                    json.put("message", String.format("No party with name '%s' found", party));
                    response.status(400);
                    return json.toJSONString();
                }
            } else {
                JSONObject json = new JSONObject();
                json.put("message", "URL is wrong!");
                response.status(400);
                return json.toJSONString();
            }
            while (cursor.hasNext()) {
                doc = (Document) cursor.next();
                ArrayList<String> tokenList = (ArrayList<String>) doc.get("token");
                for (String token : tokenList) {
                    if (allToken.containsKey(token)) {
                        allToken.put(token, allToken.get(token) + 1);
                    } else {
                        allToken.put(token, 1);
                    }
                }
            }
            LinkedHashMap<String, Integer> allTokenSorted = sortMap(allToken);
            ArrayList<JSONObject> resultList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : allTokenSorted.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                JSONObject json = new JSONObject();
                json.put("token", key);
                json.put("count", value);
                resultList.add(json);
            }
            JSONObject result = new JSONObject();
            result.put("result", resultList);
            return result.toJSONString();
        });

    }
    /**
     * sorting a HashMap<String, Integer>
     * @param hashMap
     * @return
     */
    private static LinkedHashMap<String, Integer> sortMap(HashMap<String, Integer> hashMap) {

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> hashMapList = new LinkedList<>(hashMap.entrySet());

        hashMapList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<String, Integer> entry : hashMapList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
//db.amembers.aggregate( [
//        {
//        $match: {"party" : "CDU"}
//        },
//        {
//        $group:
//        {
//        _id: "$party",
//        ids: { $push:  "$_id" }
//        }
//        }
//        ] )

//db.speeches.aggregate( [
//        {
//        $match: {"speaker" : "11001478"}
//        },
//        {
//        $group:
//        {
//        _id: "$speaker",
//        allToken: { $push:  "$token" }
//        }
//        },
//        {
//        $project: {
//        _id: "$_id",
//        "token": {
//        $reduce: {
//        input: "$allToken",
//        initialValue: [ ],
//        in: { $concatArrays: [ "$$value", "$$this" ] }
//        }
//        }
//        }
//        }
//        ] )
