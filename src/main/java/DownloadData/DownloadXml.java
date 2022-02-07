package DownloadData;

import java.io.File;
import java.io.IOException;

public class DownloadXml {

    // Checks if Folder/File already exists, otherwise it will be created.

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
                    Thread t = new Thread(new Sort.protocolLoading(link, out));
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadDTD() {
        String link = "https://www.bundestag.de/resource/blob/575720/70d7f2af6e4bebd9a550d9dc4bc03900/dbtplenarprotokoll-data.dtd";
        for (String period : new String[] {"19", "20"}) {
            File dtd = new File("src/main/resources/" + period + "/" + "dbtplenarprotokoll.dtd");
            if (!dtd.exists()) {
                try {
                    if (dtd.createNewFile()) {
                        Thread t = new Thread(new Sort.protocolLoading(link, dtd));
                        t.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
