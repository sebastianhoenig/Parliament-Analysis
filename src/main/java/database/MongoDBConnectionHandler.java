package database;


import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import data.*;
import me.tongfei.progressbar.ProgressBar;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.mongodb.client.model.Aggregates.*;

/**
 * Class for communicate with MongoDB
 * @author benwerner
 * @date 27.01.2022
 */

public class MongoDBConnectionHandler {

    /**
     * Mongo Client for interaction with Mongo DB
     */
    private MongoClient mongoClient;

    /**
     * Connection to Database PRG_WiSe21_239
     */
    private MongoDatabase database;

    /**
     * Main collection protocol
     */
    private MongoCollection<Document> collection;

    /**
     * Create Connection to Mongo DB
     */
    public MongoDBConnectionHandler(){
        Properties prop = PropertiesFile.readPropertiesFile();

        // defined credentials (Username, database, password)
        MongoCredential credential = MongoCredential.createScramSha1Credential(prop.getProperty("remote_user"),
                prop.getProperty("remote_database"), prop.getProperty("remote_password").toCharArray());
        // defining Hostname and Port
        ServerAddress seed = new ServerAddress(prop.getProperty("remote_host"),
                Integer.parseInt(prop.getProperty("remote_port")));
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        // defining some Options
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(10)
                .socketTimeout(10000)
                .maxWaitTime(10000)
                .connectTimeout(10000)
                .sslEnabled(false)
                .build();

        // connect to MongoDB
        mongoClient = new MongoClient(seeds, credential, options);

        // select database
        database = mongoClient.getDatabase(prop.getProperty("remote_database"));

        // select default connection (speeches)
        collection = database.getCollection(prop.getProperty("remote_collection"));

        // some debug information
//        System.out.println("Connect to "+prop.getProperty("remote_database")+" on "+prop.getProperty("remote_host"));
    }

    /**
     * To close Connection with DB
     */
    public void close() {
        this.mongoClient.close();
    }

    /**
    * to reset/delete all Collection in database
    */
    public void deleteAllCollections(){
        MongoCursor<String> cursor = this.database.listCollectionNames().cursor();
        while (cursor.hasNext()) {
            this.database.getCollection(cursor.next()).drop();
        }
    }

    /**
     *
     * @return Database PRG_WiSe21_239
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     *
     * @return Client to database
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * Method for default Collection
     * @return MongoCollection
     */
    public MongoCollection getCollection() {
        return this.collection;
    }

    /**
     * Method for Collection
     * @return MongoCollection
     */
    public MongoCollection getCollection(String collectionName) {
        return this.database.getCollection(collectionName);
    }


    /**
     * Method to find a Document with the "_id" in a specific Collection.
     * @param id
     * @param collectionName
     * @return Document
     */
    public Document getDBDocument(String id, String collectionName) {
        BasicDBObject where = new BasicDBObject();
        where.put("_id", id);

        MongoCursor<Document> searchResults = this.getCollection(collectionName).find(where).iterator();

        Document resultDoc = null;

        while(searchResults.hasNext()) {
            resultDoc = searchResults.next();
        }

        return resultDoc;
    }

    /**
     * This function is to upload all Protocol and Members.
     * @param protocolList
     * @param memberList
     */
    public void uploadAll(ArrayList<Protocol> protocolList, ArrayList<Member> memberList) {
        BTObjectToMongoDocument.createDCCMap();
        BTObjectToMongoDocument.createPOSMap();
        initializeStatus();

        int speechCounter = 0;
        int CommentCounter = 0;
        for (Protocol protocol :  protocolList) {
            for (AgendaItem agendaItem : protocol.getAllAgendaItems()) {
                for (Speech speech : agendaItem.getSpeeches()) {
                    speechCounter++;
                    for (Comment comment : speech.getAllComments()) {
                        CommentCounter++;
                    }
                }
            }
        }

        int counterM = memberList.size();
        ProgressBar pb4 = new ProgressBar("upload Member", counterM);
        for (Member member : memberList) {
            insertMember(member);
            pb4.step();
        }

        ProgressBar speechP = new ProgressBar("upload Speeches", speechCounter);
        for (Protocol protocol :  protocolList) {
            for (AgendaItem agendaItem : protocol.getAllAgendaItems()) {
                for (Speech speech : agendaItem.getSpeeches()) {
                    this.insertSpeech(speech);
                    updateStatus(protocol, speech);
                    speechP.step();
                }
            }
        }
        speechP.close();

        ProgressBar commentP = new ProgressBar("upload Comments", CommentCounter);
        for (Protocol protocol :  protocolList) {
            for (AgendaItem agendaItem : protocol.getAllAgendaItems()) {
                for (Speech speech : agendaItem.getSpeeches()) {
                    for (Comment comment : speech.getAllComments()) {
                        this.insertComment(comment);
                        updateStatus(protocol, comment);
                        commentP.step();
                    }
                }
            }
        }
        commentP.close();
    }

    /**
     * This function initialize the Status on the DB.
     */
    public void initializeStatus() {
        Document statusSpeech = getDBDocument("speech", "status");
        statusSpeech.put("protocolNumber", 0);
        statusSpeech.put("speech", "");
        statusSpeech.put("countSpeech", 0);
        statusSpeech.put("error", new ArrayList<String>());

        Document statusComment = getDBDocument("comment", "status");
        statusComment.put("protocolNumber", 0);
        statusComment.put("comment", "");
        statusComment.put("countComment", 0);
        statusComment.put("error", new ArrayList<String>());

        try {
            this.getCollection("status").updateOne(new Document().append("_id", "speech"),
                    new Document().append("$set", statusSpeech));
            this.getCollection("status").updateOne(new Document().append("_id", "comment"),
                    new Document().append("$set", statusComment));
        } catch (Exception e) {
            System.out.println("Status couldn't be updated");
        }
    }

    /**
     * This function is updating the Status for Speeches on the DB.
     * @param protocol
     * @param speech
     */
    public void updateStatus(Protocol protocol, Speech speech) {
        Document statusSpeech = getDBDocument("speech", "status");
        statusSpeech.put("protocolNumber", protocol.getSessionID());
        statusSpeech.put("speech", speech.getSpeechID());
        statusSpeech.put("countSpeech", (int) statusSpeech.get("countSpeech") + 1);

        try {
            this.getCollection("status").updateOne(new Document().append("_id", "speech"),
                    new Document().append("$set", statusSpeech));
        } catch (Exception e) {
            System.out.println("Status couldn't be updated");
        }
    }

    /**
     * This function is updating the Status for Comments on the DB.
     * @param protocol
     * @param comment
     */
    public void updateStatus(Protocol protocol, Comment comment) {
        Document statusComment = getDBDocument("comment", "status");
        statusComment.put("protocolNumber", protocol.getSessionID());
        statusComment.put("comment", comment.getCommentID());
        statusComment.put("countComment", (int) statusComment.get("countComment") + 1);

        try {
            this.getCollection("status").updateOne(new Document().append("_id", "comment"),
                    new Document().append("$set", statusComment));
        } catch (Exception e) {
            System.out.println("Status couldn't be updated");
        }
    }

    /**
     * This function is to insert one Member into the DB.
     * @param member
     */
    public void insertMember(Member member) {

        Document memberDoc = getDBDocument(member.getId(), "members");

        if (memberDoc == null) {
            memberDoc = BTObjectToMongoDocument.createMongoDocument(member);
        } else {
            return;
        }
        try {
            this.getCollection("members").insertOne(memberDoc);
        } catch (Exception e) {
            System.out.println("Objekt Member (" + member.getId() + ") can not be uploaded");
        }
    }

    /**
     * This function is to insert one Speech into the DB.
     * @param speech
     */
    public void insertSpeech(Speech speech) {
        Document speechDoc = getDBDocument(speech.getSpeechID(), "speeches");

        if (speechDoc == null) {
            speechDoc = BTObjectToMongoDocument.createMongoDocument(speech);
        } else {
            return;
        }
        try {
            this.getCollection("speeches").insertOne(speechDoc);
        } catch (Exception e) {
            System.out.println("Objekt Speech (" + speech.getSpeechID() + ") can not be uploaded");
            Document statusSpeech = getDBDocument("speech", "status");
            ArrayList<String> errorList = (ArrayList<String>) statusSpeech.get("error");
            errorList.add(speech.getSpeechID());
            statusSpeech.put("error", errorList);
            try {
                this.getCollection("status").updateOne(new Document().append("_id", "speech"),
                        new Document().append("$set", statusSpeech));
            } catch (Exception ex) {
                System.out.println("Status couldn't be updated");
            }
        }
    }

    /**
     * This function is to insert one Comment into the DB.
     * @param comment
     */
    public void insertComment(Comment comment) {
        Document commentDoc = getDBDocument(comment.getCommentID(), "comments");

        if (commentDoc == null) {
            commentDoc = BTObjectToMongoDocument.createMongoDocument(comment);
        } else {
            return;
        }
        try {
            this.getCollection("comments").insertOne(commentDoc);
        } catch (Exception e) {
            System.out.println("Objekt Comment (" + comment.getCommentID() + ") can not be uploaded");
            Document statusComment = getDBDocument("comment", "status");
            ArrayList<String> errorList = (ArrayList<String>) statusComment.get("error");
            errorList.add(comment.getCommentID());
            statusComment.put("error", errorList);
            try {
                this.getCollection("status").updateOne(new Document().append("_id", "comment"),
                        new Document().append("$set", statusComment));
            } catch (Exception ex) {
                System.out.println("Status couldn't be updated");
            }
        }
    }

    /**
     * This function is to update one Member on the DB.
     * @param member
     */
    public void updateSpeaker(Member member) {
        Document where = new Document().append("_id", member.getId());

        try {
            this.getCollection("members").updateOne(where,
                    BTObjectToMongoDocument.createMongoDocument(member));
        } catch (Exception e) {
            System.out.println("Objekt Speaker (" + member.getId() + ") can not be updated");
        }
    }

    /**
     * This function is to update one Speech on the DB.
     * @param speech
     */
    public void updateSpeech(Speech speech) {
        Document where = new Document().append("_id", speech.getSpeechID());

        try {
            this.getCollection("speeches").updateOne(where,
                    new Document().append("$set", BTObjectToMongoDocument.createMongoDocument(speech)));
        } catch (Exception e) {
            System.out.println("Objekt Speech (" + speech.getSpeechID() + ") can not be updated");
        }

    }

    /**
     * This function is to update one Comment on the DB.
     * @param comment
     */
    public void updateComment(Comment comment) {
        Document where = new Document().append("_id", comment.getCommentID());

        try {
            this.getCollection("comments").updateOne(where,
                    BTObjectToMongoDocument.createMongoDocument(comment));
        } catch (Exception e) {
            System.out.println("Objekt Comment (" + comment.getCommentID() + ") can not be updated");
        }
    }

    /**
     * This function is to delete one document on the DB.
     * @param id
     * @param collectionName
     */
    public void deleteDocumentOnDB(String id, String collectionName) {
        Document where = new Document().append("_id", id);
        this.getCollection(collectionName).deleteOne(where);
    }

    /**
     * This function is to count the Document after a query.
     * @param fieldName
     * @param value
     * @param collectionName
     * @return
     */
    public int countQuery(String fieldName, int value, String collectionName) {
        Document result = (Document) this.getCollection(collectionName).aggregate(Arrays.asList(
                match(Filters.eq(fieldName, value)),
                count())).first();

        return (int) result.get("count");
    }

    /**
     * This function is to count the Document after a query.
     * @param fieldName
     * @param value
     * @param collectionName
     * @return
     */
    public int countQuery(String fieldName, String value, String collectionName) {
        Document result = (Document) this.getCollection(collectionName).aggregate(Arrays.asList(
                match(Filters.eq(fieldName, value)),
                count())).first();

        return (int) result.get("count");
    }

    /**
     * This function is to join two collections.
     * @param collectionName
     * @param from
     * @param localField
     * @param foreignField
     * @param as
     * @param fieldName
     * @param value
     * @return
     */
    public MongoCursor joinQuery(String collectionName, String from, String localField, String foreignField, String as, String fieldName, String value) {
        MongoCursor result = this.getCollection(collectionName).aggregate(Arrays.asList(
                limit(1000),
                lookup(from, localField, foreignField, as),
                match(Filters.eq(fieldName, value))
                )).cursor();

        return  result;
    }

    /**
     * This function is to join two collections.
     * @param collectionName
     * @param from
     * @param localField
     * @param foreignField
     * @param as
     * @param fieldName
     * @param value
     * @return
     */
    public MongoCursor joinQuery(String collectionName, String from, String localField, String foreignField, String as, String fieldName, int value) {
        MongoCursor result = this.getCollection(collectionName).aggregate(Arrays.asList(
                limit(1000),
                lookup(from, localField, foreignField, as),
                match(Filters.eq(fieldName, value))
        )).cursor();

        return  result;
    }

    /**
     * This function is to join two collections.
     * @param collectionName
     * @param from
     * @param localField
     * @param foreignField
     * @param as
     * @return
     */
    public MongoCursor joinQuery(String collectionName, String from, String localField, String foreignField, String as) {
        MongoCursor result = this.getCollection(collectionName).aggregate(Arrays.asList(
                limit(10),
                lookup(from, localField, foreignField, as))
        ).cursor();

        return  result;
    }

}
