package database;

import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.json.simple.JSONObject;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.stop;


/**
 * Main class to run Api separated.
 * @author benwerner, sebastian
 * @date
 */
public class ApiConnection {

    /**
     * The handler param is saving the connection object to the DB.
     */
    private static MongoDBConnectionHandler handler = new MongoDBConnectionHandler();

    /**
     * The allParties param is saving the party name list to compare with given names.
     */
    private static List<String> allParties = Arrays.asList(new String[] {"BÜNDNIS 90/DIE GRÜNEN", "AfD", "CDU", "CSU", "FDP", "SPD", "Plos", "LKR", "DIE LINKE.", "Die PARTEI", "SSW"});

    /**
     * The main methode is running all Api Routes.
     * @param args
     */
    public static void main (String[] args) {

        get("/speeches", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            String id = request.queryMap().get("id").value();
            Document doc = handler.getDBDocument(id, "speeches");
            doc.remove("uima");
            String json = JSON.serialize(doc);
            return json;
        });

        get("/comments", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            String id = request.queryMap().get("id").value() + "-";
            Document match = Document.parse("{$match:{_id: /" + id + "/i}}");
            MongoCursor cursor = handler.getCollection("comments").aggregate(Arrays.asList(match)).cursor();
            String json;
            StringBuilder finalJson = new StringBuilder();
            while (cursor.hasNext()) {
                Document doc = (Document) cursor.next();
                doc.remove("uima");
                json = JSON.serialize(doc);
                finalJson.append(json);
            }
            return finalJson.toString();
        });

        get("/speaker", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");

            String id = request.queryMap().get("id").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-20";
                endDate = "2022-02-11";
            }
            Document doc;
            if (! request.queryMap().hasKeys()) {
                doc = (Document) handler.getCollection("members").aggregate(Arrays.asList(
                        Document.parse("{$project : {_id : \"all\", id : \"$_id\", name: \"$name\", surname : \"$surname\", party : \"$party\", picture : \"$picture\", allSpeeches : {$size : \"$allSpeeches\"}}}"),
                        Document.parse("{$group : {_id:\"$_id\", speakers : {$push : {id : \"$id\", name: \"$name\", surname : \"$surname\", party : \"$party\", picture : \"$picture\", allSpeeches : \"$allSpeeches\"}}}}")
                )).first();
                doc.remove("_id");
            } else {
                doc = handler.getDBDocument(id, "members");
                Document countSpeeches = (Document) handler.getCollection("speeches").aggregate(Arrays.asList(
                        Document.parse("{$match: {speaker : \"" + id + "\", \"protocol.date\": {$gte: ISODate(\""+beginDate+"\"), $lt: ISODate(\""+endDate+"\")}}}"),
                        Document.parse("{$group : {_id : \"$speaker\", count: {$sum : 1}}}")
                )).first();
                if (countSpeeches != null) {
                    doc.put("allSpeeches", countSpeeches.get("count"));
                } else {
                    doc.put("allSpeeches", 0);
                }
            }
            String json = com.mongodb.util.JSON.serialize(doc);
            return json;
        });


        get("/party", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            ArrayList<JSONObject> partyList = new ArrayList<>();
            for (int i = 0; i < allParties.size(); i++) {
                String currentParty = allParties.get(i);
                Document match = Document.parse("{$match:{party: \"" + currentParty + "\"}}");
                MongoCursor cursor = handler.getCollection("members").aggregate(Arrays.asList(match)).cursor();
                int counter = 0;
                while (cursor.hasNext()) {
                    counter += 1;
                    cursor.next();
                }
                JSONObject json = new JSONObject();
                json.put("members", counter);
                json.put("id", currentParty);
                partyList.add(json);
            }
            JSONObject result = new JSONObject();
            result.put("result", partyList);
            return result.toJSONString();
        });

        get("/speakers", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            MongoCursor cursor = handler.getCollection("members").find().cursor();
            ArrayList<JSONObject> speakerList = new ArrayList<>();
            while (cursor.hasNext()) {
                JSONObject json = new JSONObject();
                Document doc = (Document) cursor.next();
                json.put("id", (String) doc.get("_id"));
                json.put("name", (String) doc.get("name"));
                json.put("surname", (String) doc.get("surname"));
                json.put("party", (String) doc.get("party"));
                speakerList.add(json);
            }
            JSONObject result = new JSONObject();
            result.put("result", speakerList);
            return result.toJSONString();
        });

        get("/api/close", (request, response) -> {
            stop();
            return "Shutdown Server";
        });

        get("/token", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            String attribute = "token";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-26";
                endDate = "2022-02-11";
            }

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response, beginDate, endDate);
            if (resultList == null) {
                return response.body();
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/pos", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            String attribute = "pos";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-26";
                endDate = "2022-02-11";
            }

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response, beginDate, endDate);
            if (resultList == null) {
                return response.body();
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/lemma", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");
            String attribute = "lemma";

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-26";
                endDate = "2022-02-11";
            }

            ArrayList resultList = prozessQuery(attribute, speakerID, party, request, response, beginDate, endDate);
            if (resultList == null) {
                return response.body();
            } else {
                JSONObject result = new JSONObject();
                result.put("result", resultList);

                return result.toJSONString();
            }
        });

        get("/namedEntities", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String entities = request.queryMap().get("entities").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-26";
                endDate = "2022-02-11";
            }

            ArrayList resultList;
            if (entities == null) {
                return errorMessage(response, "Use Attribute entities");
            } else if (entities.equals("persons")) {
                resultList = prozessQuery("persons", speakerID, party, request, response, beginDate, endDate);
            } else if (entities.equals("locations")) {
                resultList = prozessQuery("locations", speakerID, party, request, response, beginDate, endDate);
            } else if (entities.equals("organisations")) {
                resultList = prozessQuery("organisations", speakerID, party, request, response, beginDate, endDate);
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
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json;charset=UTF-8");

            String speakerID = request.queryMap().get("speakerID").value();
            String party = request.queryMap().get("party").value();
            String beginDate = request.queryMap().get("beginDate").value();
            String endDate = request.queryMap().get("endDate").value();
            if (beginDate == null || endDate == null) {
                beginDate = "2017-10-26";
                endDate = "2022-02-11";
            }

            MongoCursor cursor;
            Document match;
            Document group = Document.parse("{$group: {_id:\"$speaker\", sentiment: {$push: \"$sentiment\"}}}");
            Document project = Document.parse("{$project: {_id:\"$_id\", sentiment:\"$sentiment\"}}");

            if (! request.queryMap().hasKeys()) {
                // Alle pos
                cursor = handler.getCollection("speeches").aggregate(Arrays.asList(group, project)).cursor();
            } else if (speakerID != null && party == null) {
                // pos pro Speaker
                if (handler.getDBDocument(speakerID, "members") != null) {
                    match = Document.parse("{$match: {speaker:\"" + speakerID + "\"}}");
                    cursor = handler.getCollection("speeches").aggregate(Arrays.asList(match, group, project)).cursor();
                } else {
                    return errorMessage(response, String.format("No user with id '%s' found", speakerID));
                }
            } else if (speakerID == null && party != null) {
                // pos pro Party
                if (allParties.contains(party)) {
                    Document doc = (Document) handler.getCollection("members").aggregate(Arrays.asList(
                            Document.parse("{$match: {\"party\" : \"" + party + "\"}}"),
                            Document.parse("{$group:{_id: \"$party\", ids: { $push:  \"$_id\" }}}"))).first();

                    List<String> idList = (ArrayList<String>) doc.get("ids");

                    idList = idList.stream().map((String id) -> id = "\"" + id + "\"").collect(Collectors.toList());

                    cursor = handler.getCollection("speeches").aggregate(Arrays.asList(
                            Document.parse("{$match: {\"speaker\" : {$in : " + idList + "}, \"protocol.date\": {$gte: ISODate(\""+beginDate+"\"), $lt: ISODate(\""+endDate+"\")}}}"),
                            Document.parse("{$project: {_id:\""+party+"\", sentiment:\"$sentiment\"}}"),
                            Document.parse("{$group: {_id:\"$_id\", sentiment: {$push: \"$sentiment\"}}}"),
                            project)).cursor();
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
    private static ArrayList prozessQuery(String attribute, String speakerID, String party, Request request, Response response,
                                          String beginDate, String endDate) {
        MongoCursor cursor;
        if (! request.queryMap().hasKeys() || (request.queryMap().get("entities").value() != null && speakerID == null && party == null)) {
            // Alle pos
            Document project = Document.parse("{$project: {\"" + attribute + "\": \"$" + attribute + "\"}}");
            cursor = handler.getCollection("speeches").aggregate(Arrays.asList(project)).cursor();
        } else if (speakerID != null && party == null) {
            // pos pro Speaker
            if (handler.getDBDocument(speakerID, "members") != null) {
                cursor = getSpeechAttributeSpeaker(attribute, speakerID, beginDate, endDate);
            } else {
                response.body(errorMessage(response, String.format("No user with id '%s' found", speakerID)));
                return null;
            }
        } else if (speakerID == null && party != null) {
            // pos pro Party
            if (allParties.contains(party)) {
                cursor = getSpeechAttributeParty(attribute, party, beginDate, endDate);
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
    private static MongoCursor getSpeechAttributeSpeaker(String attribute, String speakerID, String beginDate, String endDate) {

        Document match = Document.parse("{$match : {\"speaker\" : \"" + speakerID +"\", \"protocol.date\": {$gte: ISODate(\""+beginDate+"\"), $lt: ISODate(\""+endDate+"\")}}}");
        Document group = Document.parse("{$group: {_id : \"$speaker\", all" + attribute + " : {$push : \"$" + attribute + "\"}}}");
        Document project = Document.parse("{$project : {" +
                "_id : \"$_id\"," +
                attribute + " : {" +
                "$reduce : {" +
                "input:\"$all" + attribute + "\"," +
                "initialValue: [ ]," +
                "in: { $concatArrays: [ \"$$value\", \"$$this\" ] }" +
                "}}}}");
        return handler.getCollection("speeches").aggregate(Arrays.asList(match, group, project)).cursor();
    }

    /**
     * This methode is for the get request token, pos, lemma, namedEntities.
     * Its returns a MongoCursor over a grouped attribute, by a party, of the Collection speeches.
     * @param attribute
     * @param party
     * @return
     */
    private static MongoCursor getSpeechAttributeParty(String attribute, String party, String beginDate, String endDate) {
        Document doc = (Document) handler.getCollection("members").aggregate(Arrays.asList(
                Document.parse("{$match: {\"party\" : \"" + party + "\"}}"),
                Document.parse("{$group:{_id: \"$party\", ids: { $push:  \"$_id\" }}}"))).first();

        List<String> idList = (ArrayList<String>) doc.get("ids");

        idList = idList.stream().map((String id) -> id = "\"" + id + "\"").collect(Collectors.toList());

        return handler.getCollection("speeches").aggregate(Arrays.asList(
                Document.parse("{$match: {\"speaker\" : {$in : " + idList + "}, \"protocol.date\": {$gte: ISODate(\""+beginDate+"\"), $lt: ISODate(\""+endDate+"\")}}}"),
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
     * Sorting a HashMap<String, Integer>
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
