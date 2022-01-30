import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import database.MongoDBConnectionHandler;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.mongodb.client.model.Aggregates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class test {
    public static void main(String[] args) {
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();

//        int count = handler.countQuery("fraction", 1489579010, "speaker");
//        System.out.println(count);

        MongoCursor cursor = handler.joinQuery("speaker", "speech", "speechList", "id", "speechList");
        while (cursor.hasNext()) {
            Document doc = (Document) cursor.next();
            System.out.println(doc.get("id"));
        }
        handler.close();
    }

}
