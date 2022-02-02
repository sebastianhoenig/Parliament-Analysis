import DownloadMetadata.DownloadZip;
import DownloadMetadata.ScrapePictures;
import DownloadMetadata.UnzipFile;
import Parliament.Member;
import XmlConnection.XmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


public class metaDataMain {

    public static void main(String[] args) throws InterruptedException {
        String dir = "C:\\Users\\User\\Desktop\\test\\MdB-Stammdaten-data.zip";
        String target = "C:\\Users\\User\\Desktop\\test\\";
        String source = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";
        DownloadZip download = new DownloadZip(source, dir);
        download.run();
        UnzipFile unzipManager = new UnzipFile(dir, target);
        unzipManager.unzip();
        XmlReader xml = new XmlReader();
        Document doc = xml.getMetadataXml();
        ArrayList<Member> allMembers = new ArrayList<>();
        NodeList MdbList = doc.getElementsByTagName("MDB");
        System.out.println(MdbList);
        System.out.println(MdbList.getLength());
        for (int i=0; i<MdbList.getLength(); i++) {
            Thread.sleep(500);
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                Member m = new Member(Mdb);
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
