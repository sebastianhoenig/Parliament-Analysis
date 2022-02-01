package protocolData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net. HttpURLConnection;
import java.net.URL;

// TODO: Jede Datei einzeln laden lassen ODER " 3 von 24 Dateien eingelsen" ?

public class protocolLoading implements Runnable {

    String link;
    File out;

    public protocolLoading (String link, File out) {

        this.link = link;
        this.out = out;

    }

    @Override
    public void run() {

        try {
            URL url = new URL(link);                                                        // create new URL object with an URL file
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            double fileSize = (double) http.getContentLengthLong();                         // get the file size (bytes) so you can see the process of downloading
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);                          // to save a file to the folder
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);           // 1024 bytes is the length of BufferedOutputStream
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;                                                       // to see how much it downloaded
            int read = 0;                                                                   // to know how many bytes it read on iteration
            double percentDownloaded = 0.00;                                                // to see how much it downloaded in %


            while ((read = in.read(buffer,  0, 1024)) >= 0) {                      // >= 0 refers to the bytes (of BufferedOutputStream) // to read a file from URL in a while loop and write it to the file "out"
                bout.write(buffer, 0, read);                                            // read in bytes
                downloaded += read;
                percentDownloaded = (downloaded * 100) /  fileSize;
                String percent = String.format("%3f", percentDownloaded);                   // represent % in String
                System.out.println("Downloaded " + percent + "% of a file.");
            }
            bout.close();
            in.close();
            System.out.println("Download complete");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
