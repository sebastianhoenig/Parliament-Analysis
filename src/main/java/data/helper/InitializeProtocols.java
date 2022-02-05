package data.helper;
import data.Protocol;
import data.impl.ProtocolFile_Impl;
import me.tongfei.progressbar.ProgressBar;
import org.w3c.dom.Document;
import java.util.ArrayList;

public class InitializeProtocols {
    public ArrayList<Protocol> allProtocols = new ArrayList<>();

    public InitializeProtocols(){
        ArrayList<Document> allXmlArrayList = XMLFileReader.getAllFiles("src/main/resources/19");
        ArrayList<Document> all20XmlArrayList = XMLFileReader.getAllFiles("src/main/resources/20");
        allXmlArrayList.addAll(all20XmlArrayList);

        ProgressBar pb1 = new ProgressBar("Einlesen der Daten aus den Files", allXmlArrayList.size());
        for (Document document : allXmlArrayList) {
            pb1.setExtraMessage("Reading...");
            pb1.step();
            Protocol protocol = new ProtocolFile_Impl();
            protocol.initialize(document);
            allProtocols.add(protocol);
        }
        pb1.close();
    }
}
