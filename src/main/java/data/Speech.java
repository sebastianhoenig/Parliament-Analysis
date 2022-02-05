package data;
import org.w3c.dom.Node;

import java.util.ArrayList;
//TODO: Add Javadoc and refactor
public interface Speech {
    void setSpeechID(String speechID);
    void initialize(Node kNode, Protocol protocol, AgendaItem agendaItem);
    String getSpeechID();
    String getSpeakerID();
    void setSpeakerID(String speakerID);
    String getText();
    ArrayList<Comment> getAllComments();

    Protocol getProtocol();
    void setProtocol(Protocol protocol);

    AgendaItem getAgendaItem();
    void setAgendaItem(AgendaItem agendaItem);
}
