package DownloadData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadZip {

    public static void download(String link, String dir) {
        try {
            File file = new File("src/main/resources/MdB-Stammdaten-data.zip");
            if (!file.exists()) {
                Files.copy(
                        new URL(link).openStream(),
                        Paths.get(dir));
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
