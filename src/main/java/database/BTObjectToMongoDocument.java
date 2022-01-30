package database;


import data.Comment;
import data.Speech;
import data.helper.XMLFileReader;
import org.bson.Document;

/**
 * Class to create the Documents for DB with NLP analyse
 * @author benwerner
 * @date
 */
public class BTObjectToMongoDocument {

//    public static Document createMongoDocument(Speaker speaker) {
//        Document speakerDoc = new Document();
//        speakerDoc.append("_id", speaker.getID());
//        // @Todo: Andere Attribute müssen ergänzt werden
//        return speakerDoc;
//    }

    public static Document createMongoDocument(Speech speech) {
        Document speechDoc = new Document();
        speechDoc.append("_id", speech.getSpeechID());
        speechDoc.append("text", speech.getText());
        speechDoc.append("speaker", speech.getSpeakerID());
//        speechDoc.append("comment", speech.getAllComments().stream().map(comment -> comment.getCommentID()));

        // Todo: update some Attributes
        Document protocolDoc = new Document();
        protocolDoc.append("date", speech.getProtocol().getDate()); //Todo: convert to realy Date ?
        protocolDoc.append("electionPeriod", speech.getProtocol().getElectionPeriod());
        protocolDoc.append("sessionID", speech.getProtocol().getSessionID());
        protocolDoc.append("title", speech.getProtocol().getTitle()); //Todo: update
        //protocolDoc.append("startPageNr", speech.getProtocol().getStartPageNr());

        speechDoc.append("protocol", protocolDoc);

        speechDoc.append("agendaItem", speech.getAgendaItem().getAgendaItemID());

//        System.out.println(speechDoc.get("comment"));

        // @Todo: NLP
        return speechDoc;
    }

    public static Document createMongoDocument(Comment comment) {
        Document commentDoc = new Document();
        commentDoc.append("_id", comment.getCommentID());
        commentDoc.append("text", comment.getText());
        // @Todo: Speaker und NLP (und gegebenenfalls Speech??)
        return commentDoc;
    }
}
