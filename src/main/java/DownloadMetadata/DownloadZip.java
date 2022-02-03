package DownloadMetadata;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadZip {

    public static void download(String link, String dir) {
        try {
            Files.copy(
                    new URL(link).openStream(),
                    Paths.get(dir));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
