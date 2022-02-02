import data.helper.InitializeProtocols;
import database.MongoDBConnectionHandler;

public class testRun {
    public static void main(String[] args) {

        InitializeProtocols initialize = new InitializeProtocols();
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        handler.uploadAllProtocols(initialize.allProtocols);
    }
}
