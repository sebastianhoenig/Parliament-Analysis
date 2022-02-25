package data;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * @author Vanessa Rosenbaum
 * interface of a protocol
 */
public interface Protocol {
    int setStartPageNr(Integer startPageNr);
    int getStartPageNr();

    /**
     * sets date of protocol
     * @param pDate
     * @return
     */
    Date setDate(String pDate);

    /**
     * @return date of a protocol
     */
    Date getDate();

    /**
     * sets agenda items of a protocol
     * @param doc
     * @param allMembers
     * @return
     */
    ArrayList<AgendaItem> setAgendaItem(Document doc, HashMap<String, Member> allMembers);

    /**
     * gets all agenda items of a protocol
     * @return
     */
    ArrayList<AgendaItem> getAllAgendaItems();

    /**
     * sets session id od a protocol
     * @param sessionID
     * @return
     */
    int setSessionID(Integer sessionID);

    /**
     * gets session id of a protocol
     * @return
     */
    Integer getSessionID();

    /**
     * sets leaders of a protocol
     * @param doc
     * @return
     */
    ArrayList<String> setLeaders (Document doc);

    /**
     * @return gets leaders of a protocol
     */
    ArrayList<String> getLeaders();

    /**
     * sets election period of a protocol
     * @param doc
     * @return
     */
    int setElectionPeriod (Document doc);

    /**
     * gets election period of a protocol
     * @return
     */
    Integer getElectionPeriod();

    /**
     * sets title of a protocol
     * @param title
     * @return
     */
    String setTitle(String title);

    /**
     * gets title of a protocol
     * @return
     */
    String getTitle();

}
