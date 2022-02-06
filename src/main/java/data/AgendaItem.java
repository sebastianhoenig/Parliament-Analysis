package data;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: Add Javadoc
public interface AgendaItem {

    String setAgendaItemID(String id);

    String getAgendaItemID();

    ArrayList<Speech> setAllSpeeches(Node nNode, Protocol protocol, HashMap<String, Member> allMembers);
    ArrayList<Speech> getSpeeches();
}
