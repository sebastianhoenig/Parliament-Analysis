package protocolData;

import java.io.File;
import java.io.IOException;

// TODO:  (open zip before!)
// TODO:  Count files then create  "Downloaded ... % of the files"

public class mainLoading {

    public static void main(String[] args) {

        // File datei = new File ("src/main/java/protocolData/test/dateiTest.xml");
        File folder = new File("src/main/java/protocolData/loadedProtocols");

        String link = "https://deutsch.fit/Deutschkurs/Deutschkurs-ABC.pdf";                             // TODO:  just a test doc
        File out = new File("src/main/java/protocolData/loadedProtocols/test.pdf");
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
                System.out.println("Datei nun erstellt!");                                                                                                          // TODO : "nicht erstellen" sondern geladene Datei  abspeichern!
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

}
