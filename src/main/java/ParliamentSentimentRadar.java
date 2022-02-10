import DownloadData.DownloadZip;
import DownloadData.Parse;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ParliamentSentimentRadar{

    public static void main(String[] args) {
        Download();

        InitializeProtocols initialize = new InitializeProtocols(50, 59);

        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        handler.uploadAll(initialize.getAllProtocols(), initialize.allMembers);
    }

    private static void Download() {
        Parse.pars();
        String dir = "src/main/resources/MdB-Stammdaten-data.zip";
        String target = "src/main/resources";
        String source = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip"; //TODO: Parse Link (Sebastian)
        DownloadZip.download(source, dir);
        UnzipFile.unzip(dir, target);
    }
}

