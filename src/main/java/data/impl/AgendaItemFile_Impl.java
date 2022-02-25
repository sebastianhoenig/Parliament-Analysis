package data.impl;

import data.AgendaItem;
import data.Member;
import data.Protocol;
import data.Speech;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Vanessa Rosenbaum
 * this class implements an agenda item. An agenda item has an id and an arraylist with all speeches
 */
public class AgendaItemFile_Impl implements AgendaItem {
    String agendaItemID;
    ArrayList<Speech> allSpeeches;

    /**
     * @param nummer
     * @param nNode
     * @param protocol
     * @param allMembers
     */
    public AgendaItemFile_Impl(String nummer, Node nNode, ProtocolFile_Impl protocol, HashMap<String, Member> allMembers) {
        this.agendaItemID = setAgendaItemID(nummer);
        this.allSpeeches = setAllSpeeches(nNode, protocol, allMembers);
    }


    /**
     * @param id
     * @return id of an agenda item
     */
    public String setAgendaItemID(String id) {
        return id;
//        System.out.println("TAGESORDNUNGSPUNKT ID: "+agendaItemID);
    }

    /**
     * @return if of the agenda item
     */
    public String getAgendaItemID(){
        return this.agendaItemID;
    }


    /**
     * this function initializes all speeches and speakers
     * @param nNode node of the protocol
     * @return Arraylist with all speeches
     */
    public ArrayList<Speech> setAllSpeeches(Node nNode, Protocol protocol, HashMap<String, Member> allMembers) {
        ArrayList<Speech> allSpeeches = new ArrayList<>();
        NodeList reden = nNode.getChildNodes();
        for (int temp = 0; temp < reden.getLength(); temp++) {
            Node curr_speech = reden.item(temp);
            if (curr_speech.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) curr_speech;
                if (eElement.getTagName().equalsIgnoreCase("rede")){
                    String speechID = eElement.getAttribute("id");
                    Node kNode = reden.item(temp);
                    Node rednerNode = eElement.getElementsByTagName("redner").item(0);
                    Element rednerElement = (Element) rednerNode;
                    String speakerID = rednerElement.getAttribute("id");
                    Speech speech = new SpeechFile_Impl(speechID, kNode, protocol, this, speakerID, allMembers);
                    allSpeeches.add(speech);
                }
            }
        }
        return allSpeeches;
    }

    /**
     * @return Arraylist of all speeches.
     */
    public ArrayList<Speech> getSpeeches(){
        return this.allSpeeches;
    }

}
