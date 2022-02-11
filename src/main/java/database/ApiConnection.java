package database;
import com.mongodb.client.MongoCursor;
import database.MongoDBConnectionHandler;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.texttechnologylab.uimadb.databases.mongo.Mongo;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.*;


public class ApiConnection {

    private static MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
    private static List<String> allParties = Arrays.asList(new String[] {"BÜNDNIS 90/DIE GRÜNEN", "AfD", "CDU", "CSU", "FDP", "SPD", "Plos", "LKR", "DIE LINKE.", "Die PARTEI", "SSW"});

    public static void main (String[] args) {

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
            response.type("application/json;charset=UTF-8");
            HashMap<String, String> partyMembers = new HashMap<>();
            for (int i = 0; i < allParties.size(); i++) {
                String currentParty = allParties.get(i);
                Document match = Document.parse("{$match:{party: \"" + currentParty + "\"}}");
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
            String attribute = "token";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response);
            if (resultList == null) {
                return response;
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/pos", (request, response) -> {
            response.type("application/json;charset=UTF-8");
            String attribute = "pos";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response);
            if (resultList == null) {
                return response;
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/lemma", (request, response) -> {
            response.type("application/json;charset=UTF-8");
            String attribute = "lemma";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response);
            if (resultList == null) {
                return response;
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/namedEntities", (request, response) -> {
            response.type("application/json;charset=UTF-8");

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String entities = request.queryMap().get("entities").value();

            ArrayList resultList;
            if (entities == null) {
                return errorMessage(response, "Use Attribute entities");
            } else if (entities.equals("persons")) {
                System.out.println("test");
                resultList = prozessQuery("persons", speakerID, party, request, response);
            } else if (entities.equals("locations")) {
                resultList = prozessQuery("locations", speakerID, party, request, response);
            } else if (entities.equals("organisations")) {
                resultList = prozessQuery("organisations", speakerID, party, request, response);
            } else {
                return errorMessage(response, String.format("No namedEntities with name '%s' found", entities));
            }

            if (resultList == null) {
                return response.body();
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });


        get("/sentiment", (request, response) -> {
            response.type("application/json;charset=UTF-8");

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            MongoCursor cursor;
            Document match;
            Document group = Document.parse("{$group: {_id:\"$speaker\", sentiment: {$push: \"$sentiment\"}}}");
            Document project = Document.parse("{$project: {_id:\"$_id\", sentiment:\"$sentiment\"}}");

            if (! request.queryMap().hasKeys()) {
                // Alle pos
                cursor = handler.getCollection("aspeeches").aggregate(Arrays.asList(group, project)).cursor();
            } else if (speakerID != null && party == null) {
                // pos pro Speaker
                if (handler.getDBDocument(speakerID, "amembers") != null) {
                    match = Document.parse("{$match: {speaker:\"" + speakerID + "\"}}");
                    cursor = handler.getCollection("aspeeches").aggregate(Arrays.asList(match, group, project)).cursor();
                } else {
                    return errorMessage(response, String.format("No user with id '%s' found", speakerID));
                }
            } else if (speakerID == null && party != null) {
                // pos pro Party
                if (allParties.contains(party)) {
                    Document doc = (Document) handler.getCollection("amembers").aggregate(Arrays.asList(
                            Document.parse("{$match: {\"party\" : \"" + party + "\"}}"),
                            Document.parse("{$group:{_id: \"$party\", ids: { $push:  \"$_id\" }}}"))).first();

                    List<String> idList = (ArrayList<String>) doc.get("ids");

                    idList = idList.stream().map((String id) -> id = "\"" + id + "\"").collect(Collectors.toList());

                    cursor = handler.getCollection("aspeeches").aggregate(Arrays.asList(
                            Document.parse("{$match: {\"speaker\" : {$in : " + idList + "}}}"),
                            group, project)).cursor();
                } else {
                    return errorMessage(response, String.format("No party with name '%s' found", party));
                }
            } else {
                return errorMessage(response, "URL is wrong!");
            }

            JSONObject result = new JSONObject();
            ArrayList<Document> resultList = new ArrayList<>();
            while (cursor.hasNext()) {
                Document doc = (Document) cursor.next();
                resultList.add(doc);
            }
            result.put("result", resultList);
            return result.toJSONString();
        });



    }

    /**
     * The Methode do the logik stuff for the get request token, pos, lemma, namedEntities.
     * @param attribute
     * @param speakerID
     * @param party
     * @param request
     * @param response
     * @return
     */
    private static ArrayList prozessQuery(String attribute, String speakerID, String party, Request request, Response response) {
        MongoCursor cursor;
        if (! request.queryMap().hasKeys() || (request.queryMap().get("entities").value() != null && speakerID == null && party == null)) {
            // Alle pos
            Document project = Document.parse("{$project: {\"" + attribute + "\": \"$" + attribute + "\"}}");
            cursor = handler.getCollection("aspeeches").aggregate(Arrays.asList(project)).cursor();
        } else if (speakerID != null && party == null) {
            // pos pro Speaker
            if (handler.getDBDocument(speakerID, "amembers") != null) {
                cursor = getSpeechAttributeSpeaker(attribute, speakerID);
            } else {
                response.body(errorMessage(response, String.format("No user with id '%s' found", speakerID)));
                return null;
            }
        } else if (speakerID == null && party != null) {
            // pos pro Party
            if (allParties.contains(party)) {
                cursor = getSpeechAttributeParty(attribute, party);
            } else {
                response.body(errorMessage(response, String.format("No party with name '%s' found", party)));
                return null;
            }
        } else {
            response.body(errorMessage(response, "URL is wrong!"));
            return null;
        }

        HashMap<String, Integer> allAttribute = new HashMap<>();
        while (cursor.hasNext()) {
            Document doc = (Document) cursor.next();
            ArrayList<String> attributeList = (ArrayList<String>) doc.get(attribute);
            for (String att : attributeList) {
                if (allAttribute.containsKey(att)) {
                    allAttribute.put(att, allAttribute.get(att) + 1);
                } else {
                    allAttribute.put(att, 1);
                }
            }
        }
        LinkedHashMap<String, Integer> allAttributeSorted = sortMap(allAttribute);
        ArrayList<JSONObject> resultList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : allAttributeSorted.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            JSONObject json = new JSONObject();
            json.put(attribute, key);
            json.put("count", value);
            resultList.add(json);
        }
        return resultList;
    }


    /**
     * This methode is for the get request token, pos, lemma, namedEntities.
     * Its returns a MongoCursor over a grouped attribute, by a speaker, of the Collection speeches.
     * @param attribute
     * @param speakerID
     * @return
     */
    private static MongoCursor getSpeechAttributeSpeaker(String attribute, String speakerID) {

        Document match = Document.parse("{$match : {\"speaker\" : \"" + speakerID +"\" }}");
        Document group = Document.parse("{$group: {_id : \"$speaker\", all" + attribute + " : {$push : \"$" + attribute + "\"}}}");
        Document project = Document.parse("{$project : {" +
                "_id : \"$_id\"," +
                attribute + " : {" +
                "$reduce : {" +
                "input:\"$all" + attribute + "\"," +
                "initialValue: [ ]," +
                "in: { $concatArrays: [ \"$$value\", \"$$this\" ] }" +
                "}}}}");
        return handler.getCollection("aspeeches").aggregate(Arrays.asList(match, group, project)).cursor();
    }

    /**
     * This methode is for the get request token, pos, lemma, namedEntities.
     * Its returns a MongoCursor over a grouped attribute, by a party, of the Collection speeches.
     * @param attribute
     * @param party
     * @return
     */
    private static MongoCursor getSpeechAttributeParty(String attribute, String party) {
        Document doc = (Document) handler.getCollection("amembers").aggregate(Arrays.asList(
                        Document.parse("{$match: {\"party\" : \"" + party + "\"}}"),
                        Document.parse("{$group:{_id: \"$party\", ids: { $push:  \"$_id\" }}}"))).first();

        List<String> idList = (ArrayList<String>) doc.get("ids");

        idList = idList.stream().map((String id) -> id = "\"" + id + "\"").collect(Collectors.toList());

        return handler.getCollection("aspeeches").aggregate(Arrays.asList(
                        Document.parse("{$match: {\"speaker\" : {$in : " + idList + "}}}"),
                        Document.parse("{$project : {_id: \"all\", " + attribute + " : \"$" + attribute + "\"}}"))).cursor();
    }


    /**
     * This methode set the status to error and return a message as JSONObject
     * @param response
     * @param message
     * @return
     */
    private static String errorMessage(Response response, String message) {
        JSONObject json = new JSONObject();
        json.put("message", message);
        response.status(400);
        return json.toJSONString();
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
