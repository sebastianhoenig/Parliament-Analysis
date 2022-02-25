package data.impl;

import data.AgendaItem;
import data.Member;
import data.Protocol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Vanessa Rosenbaum
 * this class implements a protocol of a Bundestag parliament session. A protocol has a date, an election period,
 * a session id, a title, a startpage, agendaitems and leaders
 */
public class ProtocolFile_Impl implements Protocol {
    Date pDate;
    Integer electionPeriod;
    Integer sessionID;
    String title;
    Integer startPageNr;
    ArrayList<AgendaItem> allAgendaItems = new ArrayList<>();
    ArrayList<String> leaders = new ArrayList<String>();

    public ProtocolFile_Impl(Document doc, HashMap<String, Member> allMembers) {
        Element root = doc.getDocumentElement();
        Element elKopfData = (Element) root.getElementsByTagName("vorspann").item(0).getChildNodes().item(1);
        this.pDate = setDate(root.getAttribute("sitzung-datum"));
        this.electionPeriod = setElectionPeriod(doc);
        this.sessionID = setSessionID(Integer.parseInt(root.getAttribute("sitzung-nr")));
        this.title = setTitle(elKopfData.getElementsByTagName("sitzungstitel").item(0).getTextContent());
        this.startPageNr = setStartPageNr(Integer.parseInt(root.getAttribute("start-seitennr")));
        this.allAgendaItems = setAgendaItem(doc, allMembers);
        this.leaders = setLeaders(doc);
    }

    /**
     * @return start page number
     */
    public int getStartPageNr() {
        return startPageNr;
    }

    /**
     * @param startPageNr
     * @return start page number
     */
    public int setStartPageNr(Integer startPageNr){
        return startPageNr;
    }

    /**
     * modified by @author Ben
     * @param pDate
     * @return date
     */
    public Date setDate(String pDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = formatter.parse(pDate);
            return date;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return date of the protocol
     */
    public Date getDate(){
        return this.pDate;
    }


    /**
     * this function intitializes all agenda items of a protocol
     * @param doc
     * @param allMembers
     * @return all agenda items
     */
    public ArrayList<AgendaItem> setAgendaItem(Document doc, HashMap<String, Member> allMembers) {
        ArrayList<AgendaItem> allAgendaItems = new ArrayList<>();
        NodeList tagesordnungspunkt = doc.getElementsByTagName("tagesordnungspunkt");
        for (int temp = 0; temp < tagesordnungspunkt.getLength(); temp++) {
            Node nNode = tagesordnungspunkt.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String nummer = eElement.getAttribute("top-id");
                AgendaItem tpunkt = new AgendaItemFile_Impl(nummer, nNode, this, allMembers);
                allAgendaItems.add(tpunkt);
            }
        }
        return allAgendaItems;
    }

    /**
     * @return arraylist of all agenda items
     */
    public ArrayList<AgendaItem> getAllAgendaItems(){
        return this.allAgendaItems;
    }


    /**
     * @param sessionID
     * @return sessionID
     */
    public int setSessionID(Integer sessionID) {
        return sessionID;
    }

    /**
     * @return session id of a protocol
     */
    public Integer getSessionID(){
        return this.sessionID;
    }

    /**
     * this function sets the leaders of a protocol
     * @param doc
     * @return leaders
     */
    public ArrayList<String> setLeaders (Document doc){
        ArrayList<String> leaders = new ArrayList<>();
        NodeList speechNodes = doc.getElementsByTagName("rede");
        for (int speakerNode = 0; speakerNode < speechNodes.getLength(); speakerNode++) {
            Node currentSpeech = speechNodes.item(speakerNode);

            NodeList childNodes = currentSpeech.getChildNodes();
            for (int temp = 0; temp < childNodes.getLength(); temp++) {

                if (childNodes.item(temp).getNodeName().equals("name")) {
                    String name = childNodes.item(temp).getTextContent();
                    name = name.substring(0, name.length()-1);
                    if (name.contains("präsident") && !leaders.contains(name)) {
                        leaders.add(name);
                    }
                }
            }
        }
        return leaders;
    }

    /**
     * @return session leaders
     */
    public ArrayList<String> getLeaders(){
        return leaders;
    }

    /**
     * this function sets the election period of a protocol
     * @param doc
     */
    public int setElectionPeriod (Document doc) {
        NodeList plenarprotokoll_nummer = doc.getElementsByTagName("plenarprotokoll-nummer");
        for (int temp = 0; temp < plenarprotokoll_nummer.getLength(); temp++) {
            Node nNode = plenarprotokoll_nummer.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                return Integer.valueOf(eElement.getElementsByTagName("wahlperiode").item(0).getTextContent());
            }
        }
        return Integer.parseInt(null);
    }


    /**
     * @return election period
     */
    public Integer getElectionPeriod(){
        return this.electionPeriod;
    }


    /**
     * @param title
     * @return title
     */
    public String setTitle(String title) {
        return title;
    }

    /**
     * @return title
     */
    public String getTitle(){
        return this.title;
    }

}
