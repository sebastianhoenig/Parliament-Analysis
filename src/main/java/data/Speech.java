package data;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: Add Javadoc
public interface Speech {
    String setSpeechID(String speechID);
    String getSpeechID();
    String getSpeakerID();
    String setSpeakerID(String speakerID);
    String getText();
    ArrayList<Comment> getAllComments();

    Protocol getProtocol();
    Protocol setProtocol(Protocol protocol);

    AgendaItem getAgendaItem();
    AgendaItem setAgendaItem(AgendaItem agendaItem);
    public void updateMemberWithSpeech(String speakerID, HashMap<String, Member> allMembers, String speechID);
}
