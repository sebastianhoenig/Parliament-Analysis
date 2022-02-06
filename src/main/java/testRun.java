import DownloadData.DownloadZip;
import DownloadData.UnzipFile;
import data.Member;
import data.helper.InitializeProtocols;
import data.helper.XMLFileReader;
import data.impl.MemberFile_Impl;
import database.MongoDBConnectionHandler;
import me.tongfei.progressbar.ProgressBar;
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

        // Count total members for Progressbar
        int counter = 0;
        for (int i = 0; i < MdbList.getLength(); i++) {
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                counter++;
            }
        }

        int counterS = 0;
        ProgressBar progressBar = new ProgressBar("Einlesen der Member Files: ", counter);
        for (int i = 0; i < MdbList.getLength(); i++) {
            Node Mdb = MdbList.item(i);
            if (checkCorrect(Mdb)) {
                Thread.sleep(500);
                MemberFile_Impl m = new MemberFile_Impl(Mdb);
                allMembers.add(m);
//                System.out.println(m.getFullInfoForTesting());
                progressBar.step();
                counterS ++;
                hashedMembers.put(m.getId(), m);
            }
            if (counterS == 50) {
                break;
            }
        }
        InitializeProtocols initialize = new InitializeProtocols(hashedMembers);
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        //TODO: GET PATHS CORRECTLY FOR SAVING DATA IN PROJECT RATHER THAN ON LOCAL LAPTOP
        handler.uploadAll(initialize.getAllProtocols(), allMembers);
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

