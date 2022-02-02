package protocolData;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class mainLOAD {

    String link;
    File out;

    String source;
    String target;

    public static void main(String[] args) {

        pars();             // HttpParsing + link einlesen

    }

//TODO : 1.  HttpParsing
    public static void pars() {

        String [] links = new String[0];

        // String finalZip = null;

        try {

            List<String> zipArr = new ArrayList<>();

            // Parse for the ZIP file
            Document zipFile = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/488214-488214").get();
            String finalZip = null;
            for (Element tableZip : zipFile.select("table")) {
                for (Element rowZip : tableZip.select("tr")) {
                    Elements tdsZip = rowZip.select("td div.bt-documents-description ul.bt-linkliste li a");
                    // System.out.println(tdsZip);

                    String zipString = String.valueOf(tdsZip);
                    boolean containsStr = zipString.contains("pp19");

                    if (containsStr) {
                        //System.out.println(containsStr);
                        //System.out.println(tdsZip);

                        String[] parts = zipString.split("href=\"/");
                        String part1 = parts[1];
                        //System.out.println(part1);

                        String[] parts2 = part1.split("\" target=\"_blank\"> ZIP | 51 MB</a>");
                        String part12 = parts2[0];
                        //System.out.println(part12);

                        finalZip = "https://www.bundestag.de/" + part12;

                        //System.out.println(finalZip);

                        // loadd (0,finalZip);


                        // zipArr.add(zipString);
                    }
                }
                // System.out.println("Modified ArrayList:\n" + zipArr);

            }

            // TODO:     https://www.bundestag.de/resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip   ^^^^^^^
            // TODO:                             /resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip   |||||||


            int nameXML = 0;
            // Parse for the XML files
            for (int selectedPage = 0; selectedPage <= 231; selectedPage = selectedPage + 10) {
                Document xml19 = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=" + selectedPage).get();

                for (Element table : xml19.select("table")) {
                    for (Element row : table.select("tr")) {
                        Elements tds = row.select("td div.bt-documents-description ul.bt-linkliste li a");
                        // System.out.println(tds);
                        String xmlStrings = String.valueOf(tds);
                        // System.out.println(xmlStrings);

                        /*String line = xmlStrings;
                        String[] words = line.split("\" href=\"");
                        System.out.println(" TEST" + words); */

                        int t = 1;
                        int h = 1;
                        int naXML = 0;
                        String[] word = xmlStrings.split("\" href=\"");
                        for (String s: word) {
                            if (t % 2 == 0){

                                String[] newWord = s.split("\" target=\"_blank\"> XML | 698 KB</a>");
                                for(String g : newWord) {
                                    if (h % 2 == 1){
                                        System.out.println("FINAL   " + g);
                                        loadd(naXML, g);
                                        naXML ++;

                                    } else { continue; }
                                    h = h + 1;
                                }

                            } else { continue;}

                            t = t + 1;
                        }

                    }
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


//TODO : 2.  "Prüft ob Folder/File schon gibt, sonst erstellt esc"
    public static void loadd(int namee, String linkToLoad) {
        File folder = new File("src/main/resources/loadedProtocols");
        String link = linkToLoad;
        String name = null;
        String format = null;
        File out;

        if  (linkToLoad.equals("https://www.bundestag.de/resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip")){
            name = "wp19";
            format = "ZIP";
            out = new File("src/main/resources/loadedProtocols/" + name + "." + format);
        } else {
            // name = "wp20";
            out = new File("src/main/resources/loadedProtocols/" + namee + ".XML");
        }

        new Thread(new protocolLoading(link, out)).start();

        // Check if the folder is already there
        if (out.exists()) {
            System.out.println("Ordner ist bereits erstellt!");
        } else {
            folder.mkdir();          //mkdir provides boolean
            System.out.println("Ordner nun erstellt!");
        }

        // Check if the file is already there
        if(out.exists()) {
            System.out.println("Datei ist bereits erstellt!");
        } else {
            try {
                out.createNewFile();
                System.out.println("Datei nun erstellt!");

                // TODO: unpack !!

                String zipString = String.valueOf(out);
                boolean containsStr = zipString.contains("wp19");
                if (containsStr) {
                    UnzipFile("src/main/resources/loadedProtocols/" + name + "." + format,"src/main/resources/loadedProtocols/" + name + ".XML");
                }
            } catch (IOException ex) {
                ex.printStackTrace(); }
        }
    }


//TODO : 3.   Link als Datei einlesen
    public static class protocolLoading implements Runnable {

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
                    // System.out.println("Downloaded " + percent + "% of a file.");
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

//TODO : 4.   zip entpacken
    public void UnzipFile(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public void unzip() {
        Path mySource = Paths.get(this.source);
        Path myTarget = Paths.get(this.target);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(mySource.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                Path newPath = zipSlipProject(zipEntry, myTarget);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                }
                else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public Path zipSlipProject(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }

}
