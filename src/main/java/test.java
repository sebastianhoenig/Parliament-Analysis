import database.MongoDBConnectionHandler;
import org.bson.Document;

public class test {
    public static void main(String[] args){
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
//        handler.createDocumentOnDB("test", new Document().append("_id", "1").append("Art", "Vogel").append("Alter", 2));
        System.out.println(handler.getDBDocument("1", "test"));
        handler.close();
    }

}
