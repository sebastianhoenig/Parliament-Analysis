package data;
import org.w3c.dom.Node;

import java.util.ArrayList;

public interface Speech {
    void setSpeechID(String speechID);
    void initialize(Node kNode);
    String getSpeechID();
    String getSpeakerID();
    void setSpeakerID(String speakerID);
    String getText();
    ArrayList<Comment> getAllComments();

}
