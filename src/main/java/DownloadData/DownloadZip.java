package DownloadData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Sebastian Hönig
 * static class that downloads the zip data
 */
public class DownloadZip {

    public static void download(String link, String dir) {
        /**
         * downloads zip data
         */
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
