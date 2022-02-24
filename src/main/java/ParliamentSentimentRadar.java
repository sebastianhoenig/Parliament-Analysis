import DownloadData.DownloadZip;
import DownloadData.Parse;
import DownloadData.UnzipFile;
import data.helper.InitializeProtocols;
import database.MongoDBConnectionHandler;

/**
 * Main class to download xml's, create datastructures, analyse and upload the parlament data.
 */
public class ParliamentSentimentRadar{

    public static void main(String[] args) {
        Download();

        InitializeProtocols initialize = new InitializeProtocols();

        MongoDBConnectionHandler handler = new MongoDBConnectionHandler();
        handler.uploadAll(initialize.getAllProtocols(), initialize.allMembers);
    }

    private static void Download() {
        Parse.pars();
        String dir = "src/main/resources/MdB-Stammdaten-data.zip";
        String target = "src/main/resources";
        String source = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";
        DownloadZip.download(source, dir);
        UnzipFile.unzip(dir, target);
    }
}

