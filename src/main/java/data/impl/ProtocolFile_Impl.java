package data.impl;
import data.AgendaItem;
import data.Member;
import data.Protocol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolFile_Impl implements Protocol {
    String pDate;
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

    public int getStartPageNr() {
        return startPageNr;
    }

    public int setStartPageNr(Integer startPageNr){
        return startPageNr;
    }

    public String setDate(String pDate) {
        return pDate;
    }

    /**
     * @return Datum des Protokolls
     */
    public String getDate(){
        return this.pDate;
    }

    /**
     * Diese Funktion initialisiert die Tagesordnungspunkte eines Plenarprotokolls und fügt diese einer Arrayliste hinzu.
     * @param doc geparste xml Datei eines Plenarprotokolls.
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
     * @return Arrayliste aller Tagesordnungspunkte eines Protokolls.
     */
    public ArrayList<AgendaItem> getAllAgendaItems(){
        return this.allAgendaItems;
    }


    public int setSessionID(Integer sessionID) {
        return sessionID;
    }

    /**
     * @return Sitzungsnummer des Protokolls
     */
    public Integer getSessionID(){
        return this.sessionID;
    }

    /**
     * Diese Funktion setzt die Sitzungsleiter einer Plenarsitzung.
     * @param doc geparste xml Datei eines Plenarprotokolls.
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
     * @return Sitzungsleiter einer PLenarsitzung
     */
    public ArrayList<String> getLeaders(){
        return leaders;
    }

    /**
     * @param doc geparste xml Datei eines Plenarprotokolls.
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
     * @return Wahlperiode der PLenarsitzung.
     */
    public Integer getElectionPeriod(){
        return this.electionPeriod;
    }


    public String setTitle(String title) {
        return title;
    }

    /**
     * @return Sitzungstitel der Plenarsitzung.
     */
    public String getTitle(){
        return this.title;
    }

}
