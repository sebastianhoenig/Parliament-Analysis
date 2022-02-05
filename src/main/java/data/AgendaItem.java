package data;

import org.w3c.dom.Node;

import java.util.ArrayList;
//TODO: Add Javadoc and refactor
public interface AgendaItem {

    void setAgendaItemID(String id);

    String getAgendaItemID();

    void setAllSpeeches(Node nNode, Protocol protocol);
    ArrayList<Speech> getSpeeches();
}
