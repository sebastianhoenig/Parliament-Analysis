package DownloadMetadata;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadZip {
    String link;
    String dir;

    public DownloadZip(String link, String dir) {
        this.link = link;
        this.dir = dir;
    }

    public void run() {
        try {
            Files.copy(
                    new URL(this.link).openStream(),
                    Paths.get(this.dir));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
