import DownloadData.DownloadZip;
import DownloadData.UnzipFile;
import data.Member;
import data.helper.InitializeProtocols;
import data.helper.XMLFileReader;
import data.impl.MemberFile_Impl;
import database.MongoDBConnectionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import DownloadData.Parse;

import java.util.ArrayList;
import java.util.HashMap;

public class testRun {
    public static void main(String[] args) throws InterruptedException {
        Parse.pars();
        HashMap<String, Member> hashedMembers = new HashMap<>();
        //TODO: Auslagern in eigene Methode
        String dir = "src/main/resources/MdB-Stammdaten-data.zip";
        String target = "src/main/resources";
        String source = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip"; //TODO: Parse Link (Sebastian)
        DownloadZip.download(source, dir);
        UnzipFile.unzip(dir, target);
        Document doc = XMLFileReader.getMetadataXml();
        ArrayList<Member> allMembers = new ArrayList<>();
        assert doc != null;
        NodeList MdbList = doc.getElementsByTagName("MDB");
        int counter = 0;
        for (int i = 0; i < MdbList.getLength(); i++) {
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                Thread.sleep(500);
                MemberFile_Impl m = new MemberFile_Impl(Mdb);
                allMembers.add(m);
//                System.out.println(m.getFullInfoForTesting());
                counter ++;
                hashedMembers.put(m.getId(), m);
            }
            if (counter == 2) {
                break;
            }
        }
        InitializeProtocols initialize = new InitializeProtocols(hashedMembers);
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        //TODO: GET PATHS CORRECTLY FOR SAVING DATA IN PROJECT RATHER THAN ON LOCAL LAPTOP
        handler.uploadAll(initialize.getAllProtocols(), allMembers);
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
                                for (int k=0; k < singleElectionAttributes.getLength(); k++) {
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

