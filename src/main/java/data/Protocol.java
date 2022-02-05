package data;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
//TODO: Add Javadoc
public interface Protocol {
    int setStartPageNr(Integer startPageNr);
    int getStartPageNr();

    String setDate(String pDate);
    String getDate();
    ArrayList<AgendaItem> setAgendaItem(Document doc);
    ArrayList<AgendaItem> getAllAgendaItems();
    int setSessionID(Integer sessionID);
    Integer getSessionID();
    ArrayList<String> setLeaders (Document doc);
    ArrayList<String> getLeaders();
    int setElectionPeriod (Document doc);
    Integer getElectionPeriod();
    String setTitle(String title);
    String getTitle();

}
