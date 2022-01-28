package data.impl;

import data.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class AgendaItemFile_Impl implements AgendaItem {
    String agendaItemID;
    ArrayList<Speech> allSpeeches = new ArrayList<>();


    public void setAgendaItemID(String id) {
        this.agendaItemID = id;
        System.out.println("TAGESORDNUNGSPUNKT ID: "+agendaItemID);
    }

    /**
     * @return Nummer des tagesordnungspunktes
     */
    public String getAgendaItemID(){
        return this.agendaItemID;
    }


    /**
     * Die Funktion initialisiert alle Reden und Redner des Tagesordnungspunktes und fügt diese jeweils einer Arrayliste zu.
     * @param nNode Knotenpunkt des jeweiligen Protokolls
     */
    public void setAllSpeeches(Node nNode) {
        NodeList reden = nNode.getChildNodes();
        for (int temp = 0; temp < reden.getLength(); temp++) {
            Node curr_speech = reden.item(temp);
            if (curr_speech.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) curr_speech;
                if (eElement.getTagName().equalsIgnoreCase("rede")){
                    Speech speech = new SpeechFile_Impl();
                    String speechID = eElement.getAttribute("id");
                    speech.setSpeechID(speechID);
                    Node kNode = reden.item(temp);
                    speech.initialize(kNode);

                    Node rednerNode = eElement.getElementsByTagName("redner").item(0);
                    Element rednerElement = (Element) rednerNode;
                    String speakerID = rednerElement.getAttribute("id");
                    speech.setSpeakerID(speakerID);

                    allSpeeches.add(speech);
                }
            }
        }
    }

    /**
     * @return Arrayliste aller Reden eines Tagesordnungspunktes.
     */
    public ArrayList<Speech> getSpeeches(){
        return this.allSpeeches;
    }

}
