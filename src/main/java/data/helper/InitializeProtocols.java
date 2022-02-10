package data.helper;

import data.Member;
import data.Protocol;
import data.impl.MemberFile_Impl;
import data.impl.ProtocolFile_Impl;
import me.tongfei.progressbar.ProgressBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class InitializeProtocols {
    ArrayList<Protocol> allProtocols = new ArrayList<>();
    HashMap<String, Member> hashedMembers = new HashMap<>();
    public ArrayList<Member> allMembers = new ArrayList<>();
    int xmlStart;
    int xmlEnd;

    public InitializeProtocols(int xmlStart, int xmlEnd){
        this.xmlStart = xmlStart;
        this.xmlEnd = xmlEnd;
        setMembers();
        this.allProtocols = setProtocols();
    }

    private ArrayList<Protocol> setProtocols() {
        ArrayList<Protocol> myProtocols = new ArrayList<>();
        ArrayList<Document> allXmlArrayList = XMLFileReader.getAllFiles("src/main/resources/19", xmlStart, xmlEnd);
//        ArrayList<Document> all20XmlArrayList = XMLFileReader.getAllFiles("src/main/resources/20", xmlStart, xmlEnd);
//        allXmlArrayList.addAll(all20XmlArrayList);

        ProgressBar pb1 = new ProgressBar("Einlesen der Daten aus den Files", allXmlArrayList.size());
        for (Document document : allXmlArrayList) {
            Protocol protocol = new ProtocolFile_Impl(document, hashedMembers);
            myProtocols.add(protocol);
            pb1.step();
            pb1.setExtraMessage("Reading...");
        }
        pb1.close();
        return myProtocols;
    }

    private void setMembers(){
        Document doc = XMLFileReader.getMetadataXml();
        assert doc != null;
        NodeList MdbList = doc.getElementsByTagName("MDB");

        // Count total members for Progressbar
        int counter = 0;
        for (int i = 0; i < MdbList.getLength(); i++) {
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                counter++;
            }
        }
        ProgressBar progressBar = new ProgressBar("Einlesen der Member Files: ", counter);
        for (int i = 0; i < MdbList.getLength(); i++) {
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
//                Thread.sleep(500);
                MemberFile_Impl m = new MemberFile_Impl(Mdb);
                allMembers.add(m);
                progressBar.step();
                hashedMembers.put(m.getId(), m);
            }
        }
        progressBar.close();
    }

    public ArrayList<Protocol> getAllProtocols() {
        return this.allProtocols;
    }

    private static boolean checkCorrect(Node Mdb) {
        Element MdbElement = (Element) Mdb;
        NodeList periodList = MdbElement.getElementsByTagName("WAHLPERIODEN").item(0).getChildNodes();

        for (int i = 0; i < periodList.getLength(); i++) {
            if (periodList.item(i).getNodeName().equals("#text")) {
                continue;
            } else {
                Element period = (Element) periodList.item(i);
                String wp = period.getElementsByTagName("WP").item(0).getTextContent();
                if (wp.equals("19") || wp.equals("20")) {
                    return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }
}
