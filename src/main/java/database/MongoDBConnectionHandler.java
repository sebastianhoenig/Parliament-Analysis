package database;


import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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

        // select default connection
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





//    Update, insert von Uebung2 Ben

    /**
     * Methode to create a doc in collectionName.
     * @param collectionName
     * @param doc
     */
    public void createDocumentOnDB(String collectionName, Document doc) {
        this.database.getCollection(collectionName).insertOne(doc);
    }

    /**
     * Methode to pull a document with key from collectionName.
     * @param collectionName
     * @param key
     * @return document from collectionName with key.
     */
    public Document getDocumentOnDB(String collectionName, Document key) {
        return this.database.getCollection(collectionName).find(key).first();
    }

    /**
     * Methode to update an document on collectionName
     * @param collectionName
     * @param doc
     * @param newDoc
     */
    public void updateDocumentOnDB(String collectionName, Document doc, Document newDoc) {
        this.database.getCollection(collectionName).findOneAndUpdate(doc, new Document().append("$set", newDoc));
    }

    /**
     * Methode to delete an document on collectionName
     * @param collectionName
     * @param key
     */
    public void deleteDocumentOnDB(String collectionName, Document key) {
        this.database.getCollection(collectionName).findOneAndDelete(key);
    }

}
