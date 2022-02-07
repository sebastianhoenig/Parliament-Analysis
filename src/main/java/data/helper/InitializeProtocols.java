package data.helper;

import data.Member;
import data.Protocol;
import data.impl.ProtocolFile_Impl;
import me.tongfei.progressbar.ProgressBar;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class InitializeProtocols {
    ArrayList<Protocol> allProtocols = new ArrayList<>();

    public InitializeProtocols(HashMap<String, Member> allMembers){
        this.allProtocols = setProtocols(allMembers);
    }

    ArrayList<Protocol> setProtocols(HashMap<String, Member> allMembers) {
        ArrayList<Protocol> myProtocols = new ArrayList<>();
        ArrayList<Document> allXmlArrayList = XMLFileReader.getAllFiles("src/main/resources/19");
        ArrayList<Document> all20XmlArrayList = XMLFileReader.getAllFiles("src/main/resources/20");
        allXmlArrayList.addAll(all20XmlArrayList);

        ProgressBar pb1 = new ProgressBar("Einlesen der Daten aus den Files", allXmlArrayList.size());
        for (Document document : allXmlArrayList) {
            pb1.setExtraMessage("Reading...");
            pb1.step();
            Protocol protocol = new ProtocolFile_Impl(document, allMembers);
            myProtocols.add(protocol);
        }
        pb1.close();
        return myProtocols;
    }

    public ArrayList<Protocol> getAllProtocols() {
        return this.allProtocols;
    }
}
