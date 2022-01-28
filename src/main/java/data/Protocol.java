package data;

import org.w3c.dom.Document;

import java.util.ArrayList;

public interface Protocol {
    void initialize(Document doc);
    void setStartPageNr(Integer startPageNr);

    void setDate(String pDate);
    String getDate();
    void setAgendaItem(Document doc);
    ArrayList<AgendaItem> getAllAgendaItems();
    void setSessionID(Integer sessionID);
    Integer getSessionID();
    void setLeaders (Document doc);
    ArrayList<String> getLeaders();
    void setElectionPeriod (Document doc);
    Integer getElectionPeriod();
    void setTitle(String title);
    String getTitle();

}
