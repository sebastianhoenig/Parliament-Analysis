package data.helper;
import data.Protocol;
import data.impl.ProtocolFile_Impl;
import org.w3c.dom.Document;
import java.util.ArrayList;

public class InitializeProtocols {
    ArrayList<Document> allXmlArrayList = new ArrayList<>();

    public InitializeProtocols(){
        XMLFileReader xml = new XMLFileReader();
        allXmlArrayList = xml.getAllFiles();

        for (int curr_xml = 0; curr_xml < allXmlArrayList.size(); curr_xml++) {
            Protocol protocol = new ProtocolFile_Impl();
            protocol.initialize(allXmlArrayList.get(curr_xml));
        }
    }
}
