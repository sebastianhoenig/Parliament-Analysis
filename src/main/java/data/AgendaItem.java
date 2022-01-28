package data;

import org.w3c.dom.Node;

import java.util.ArrayList;

public interface AgendaItem {

    void setAgendaItemID(String id);

    String getAgendaItemID();

    void setAllSpeeches(Node nNode);
    ArrayList<Speech> getSpeeches();
}
