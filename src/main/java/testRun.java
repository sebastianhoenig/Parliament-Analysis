import DownloadMetadata.DownloadZip;
import DownloadMetadata.UnzipFile;
import data.helper.InitializeProtocols;
import data.helper.XMLFileReader;
import data.impl.MemberFile_Impl;
import database.MongoDBConnectionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class testRun {
    public static void main(String[] args) throws InterruptedException {

        InitializeProtocols initialize = new InitializeProtocols();
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        handler.uploadAllProtocols(initialize.allProtocols);
        //TODO: GET PATHS CORRECTLY FOR SAVING DATA IN PROJECT RATHER THAN ON LOCAL LAPTOP
        String dir = "C:\\Users\\User\\Desktop\\test\\MdB-Stammdaten-data.zip";
        String target = "C:\\Users\\User\\Desktop\\test\\";
        String source = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";
        DownloadZip.download(source, dir);
        UnzipFile.unzip(dir, target);
        XMLFileReader xml = new XMLFileReader(); //TODO: STATIC?
        Document doc = xml.getMetadataXml();
        ArrayList<MemberFile_Impl> allMembers = new ArrayList<>();
        NodeList MdbList = doc.getElementsByTagName("MDB");
        System.out.println(MdbList);
        System.out.println(MdbList.getLength());
        for (int i = 0; i < MdbList.getLength(); i++) {
            Thread.sleep(500);
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                MemberFile_Impl m = new MemberFile_Impl(Mdb);
                allMembers.add(m);
                System.out.println(m.getFullInfoForTesting());
            }
        }
    }

    private static boolean checkCorrect(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("WAHLPERIODEN")) {
                    NodeList electionAttributes = dataElement.getChildNodes();
                    for (int j=0; j<electionAttributes.getLength(); j++) {
                        Node electionAttribute = electionAttributes.item(j);
                        if (electionAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element electionElement = (Element) electionAttribute;
                            if (electionElement.getTagName().equalsIgnoreCase("WAHLPERIODE")) {
                                NodeList singleElectionAttributes = electionElement.getChildNodes();
                                for (int k=0; j< singleElectionAttributes.getLength(); k++) {
                                    Node singleElectionAttribute = singleElectionAttributes.item(k);
                                    if (singleElectionAttribute.getNodeType() == Node.ELEMENT_NODE) {
                                        Element singleElectionElement = (Element) singleElectionAttribute;
                                        if (singleElectionElement.getTagName().equalsIgnoreCase("WP")) {
                                            if (singleElectionElement.getTextContent().equalsIgnoreCase("19")
                                                    || singleElectionElement.getTextContent().equalsIgnoreCase("20")) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

