package protocolData;

import java.io.*;

public class downloading {

    // Checks if Folder/File already exists, otherwise it will be created.

    public static void loadd(int nameOfFile, String linkToLoad) {
        File folder = new File("src/main/resources/loadedProtocols");
        String link = linkToLoad;
        String nameZIP = null;
        String format = null;
        File out;

        if  (linkToLoad.equals("https://www.bundestag.de/resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip")){
            nameZIP = "wp19";
            format = "zip";
            out = new File("src/main/resources/loadedProtocols/" + nameZIP + "." + format);

        } else {
            out = new File("src/main/resources/loadedProtocols/" + "20" + nameOfFile + ".xml");
        }

        Thread t = new Thread(new sorting.protocolLoading(link, out));
        t.start();

        // Check if the folder is already there
        if (out.exists()) {
            System.out.println("Der genannte Ordner ist bereits vorhanden!");
        } else {
            folder.mkdir();          // Creates a directory named by the abstract pathname above
            System.out.println("Der Ordner nun erstellt worden!");
        }

        // Check if the file is already there
        if(out.exists()) {
            System.out.println("Die genannte Datei ist bereits vorhanden!");
        } else {
            try {
                out.createNewFile();
                System.out.println("Die Datei nun erstellt worden!");

            } catch (IOException ex) {
                ex.printStackTrace(); }
        }
    }

}
