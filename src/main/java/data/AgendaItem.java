package data;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Vanessa Rosenbaum
 * interface of an agenda item
 */
public interface AgendaItem {

    /**
     * sets the id of an agenda item
     * @param id
     * @return
     */
    String setAgendaItemID(String id);

    /**
     * gets the id of an agenda item
     * @return
     */
    String getAgendaItemID();

    /**
     * sets all speeches of an agenda item
     * @param nNode
     * @param protocol
     * @param allMembers
     * @return
     */
    ArrayList<Speech> setAllSpeeches(Node nNode, Protocol protocol, HashMap<String, Member> allMembers);

    /**
     * gets all speeches of an agenda item
     * @return
     */
    ArrayList<Speech> getSpeeches();
}
