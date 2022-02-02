package data.impl;
import data.AgendaItem;
import data.Protocol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;

public class ProtocolFile_Impl implements Protocol {
    String pDate;
    Integer electionPeriod;
    Integer sessionID;
    String title;
    Integer startPageNr;
    ArrayList<AgendaItem> allAgendaItems = new ArrayList<>();
    ArrayList<String> leaders = new ArrayList<String>();

    public void initialize(Document doc){
        Element root = doc.getDocumentElement();
        setDate(root.getAttribute("sitzung-datum"));
        setStartPageNr(Integer.parseInt(root.getAttribute("start-seitennr")));
        Element elKopfData = (Element) root.getElementsByTagName("vorspann").item(0).getChildNodes().item(1);
        setTitle(elKopfData.getElementsByTagName("sitzungstitel").item(0).getTextContent());
        setSessionID(Integer.parseInt(root.getAttribute("sitzung-nr")));
        setAgendaItem(doc);
        setLeaders(doc);
        setElectionPeriod(doc);
//        System.out.println("Datum"+pDate);
//        System.out.println("WAHLPERIODE: "+electionPeriod);
//        System.out.println("TITEL: "+ title);
//        System.out.println("STARTPAGE: "+startPageNr);
//        System.out.println("SESSION ID: "+sessionID);
//        System.out.println("LEADERS: "+getLeaders());


    }

    public int getStartPageNr() {
        return startPageNr;
    }

    public void setStartPageNr(Integer startPageNr){
        this.startPageNr = startPageNr;
    }

    public void setDate(String pDate) {
        this.pDate = pDate;
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
    public void setAgendaItem(Document doc) {
        NodeList tagesordnungspunkt = doc.getElementsByTagName("tagesordnungspunkt");
        for (int temp = 0; temp < tagesordnungspunkt.getLength(); temp++) {
            AgendaItem tpunkt = new AgendaItemFile_Impl();
            Node nNode = tagesordnungspunkt.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String nummer = eElement.getAttribute("top-id");
                tpunkt.setAgendaItemID(nummer);
                tpunkt.setAllSpeeches(nNode, this);
            }
            this.allAgendaItems.add(tpunkt);
        }
    }

    /**
     * @return Arrayliste aller Tagesordnungspunkte eines Protokolls.
     */
    public ArrayList<AgendaItem> getAllAgendaItems(){
        return this.allAgendaItems;
    }


    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
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
    public void setLeaders (Document doc){
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
    public void setElectionPeriod (Document doc) {
        NodeList plenarprotokoll_nummer = doc.getElementsByTagName("plenarprotokoll-nummer");
        for (int temp = 0; temp < plenarprotokoll_nummer.getLength(); temp++) {
            Node nNode = plenarprotokoll_nummer.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                this.electionPeriod = Integer.valueOf(eElement.getElementsByTagName("wahlperiode").item(0).getTextContent());
            }
        }
    }


    /**
     * @return Wahlperiode der PLenarsitzung.
     */
    public Integer getElectionPeriod(){
        return this.electionPeriod;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Sitzungstitel der Plenarsitzung.
     */
    public String getTitle(){
        return this.title;
    }

}
