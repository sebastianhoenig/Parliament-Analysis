package data;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Vanessa Rosenbaum
 * interface of a speech
 */
public interface Speech {
    /**
     * sets speech id of a speech
     * @param speechID
     * @return
     */
    String setSpeechID(String speechID);

    /**
     * @return gets speech id of a speech
     */
    String getSpeechID();

    /**
     * gets speaker id of a speech
     * @return
     */
    String getSpeakerID();

    /**
     * sets speaker id of a speech
     * @param speakerID
     * @return
     */
    String setSpeakerID(String speakerID);

    /**
     * gets text of a speech
     * @return
     */
    String getText();

    /**
     * gets plain text of a speech
     * @return
     */
    String getPlainText();

    /**
     * gets all comments of a speech
     * @return
     */
    ArrayList<Comment> getAllComments();

    /**
     * gets protocol of a speech
     * @return
     */
    Protocol getProtocol();

    /**
     * sets protocol of a speech
     * @param protocol
     * @return
     */
    Protocol setProtocol(Protocol protocol);

    /**
     * gets agenda item of a speech
     * @return
     */
    AgendaItem getAgendaItem();

    /**
     * sets agenda item of a speech
     * @param agendaItem
     * @return
     */
    AgendaItem setAgendaItem(AgendaItem agendaItem);

    /**
     * @author Sebastian
     * updates member to a speech
     * @param speakerID
     * @param allMembers
     * @param speechID
     */
    public void updateMemberWithSpeech(String speakerID, HashMap<String, Member> allMembers, String speechID);
}
