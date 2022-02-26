package DownloadData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * The class DownloadXml runs the downloading process.
 * @author Emmelina und Sebastian
 */

public class DownloadXml {

    /**
     * Checks if Folder/File already exists, otherwise it will be created.
     */

    public static void load(int nameOfFile, int period, String linkToLoad) {
        String link = linkToLoad;
        File folder = new File("src/main/resources/" + period);

        if (!folder.exists()) {
            if (folder.mkdir()) {
            }
        }

        File out = new File("src/main/resources/" + period + "/" + nameOfFile + ".xml");
        if (!out.exists()) {
            try {
                if (out.createNewFile()) {
                    InputStream inputStream = new URL(link).openStream();
                    Files.copy(inputStream, Paths.get("src/main/resources/" + period + "/" + nameOfFile + ".xml"), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls up the respective sub-pages of the electoral periods (19 and 20).
     * If not present, creates an InputStream for the createNewFile() function above.
     */

    public static void loadDTD() {
        String link = "https://www.bundestag.de/resource/blob/575720/70d7f2af6e4bebd9a550d9dc4bc03900/dbtplenarprotokoll-data.dtd";
        for (String period : new String[] {"19", "20"}) {
            File dtd = new File("src/main/resources/" + period + "/" + "dbtplenarprotokoll.dtd");
            if (!dtd.exists()) {
                try {
                    if (dtd.createNewFile()) {
                        InputStream inputStream = new URL(link).openStream();
                        Files.copy(inputStream, Paths.get("src/main/resources/" + period + "/" + "dbtplenarprotokoll.dtd"), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
