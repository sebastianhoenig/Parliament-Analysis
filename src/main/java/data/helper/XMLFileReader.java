package data.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import me.tongfei.progressbar.ProgressBar;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLFileReader {
    ArrayList<Document> allXmlArrayList = new ArrayList<Document>();

    /**
     * Die Funktion getAllFiles() benötigt den genauen Adresspfad des Ordners, in welchem die xml-Dateien liegen.
     * @return Arrayliste mit den geparsten xml Dateien
     */
    public ArrayList<Document> getAllFiles() {
        File folder = new File("/Users/benwerner/Qsync/Uni/4. Semester/Programierpraktikum/Übung 1/Bundestag_19/");
        File[] files = folder.listFiles();
        assert files != null;

        // sorting the Files
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                String nameF1 = f1.getName().substring(0, f1.getName().length() - 4);
                String nameF2 = f2.getName().substring(0, f2.getName().length() - 4);
                if (nameF1.matches("dbtplenarprotokoll")) {
                    return -1;
                } else if (nameF2.matches("dbtplenarprotokoll")) {
                    return 1;
                }else {
                    return Integer.valueOf(nameF1).compareTo(Integer.valueOf(nameF2));
                }
            }
        });

        ProgressBar pb2 = new ProgressBar("Einlesen der .xml Files", files.length - 1);
        for(File file : files) {
            pb2.setExtraMessage("Reading...");
            if (file.isFile() && file.getName().endsWith(".xml")) {
                pb2.step();
                if (file.getName().startsWith("10")) {
                    break;
                }
//                System.out.println("File: " + file.getName());
                try{allXmlArrayList.add(ProcessFile(file));}
                catch (NullPointerException e){e.printStackTrace();
                }
            }}
        pb2.close();

        return allXmlArrayList;
    }

    /**
     * @param file unverarbeitete xml Datei
     * @return verarbeitete/geparste xml Datei
     */
    Document ProcessFile(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            assert dBuilder != null;
            doc = dBuilder.parse(new File(String.valueOf(file)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        try{
            assert doc != null;
            doc.getDocumentElement().normalize();}
        catch (NullPointerException e){e.printStackTrace();}


        return doc;
    }

}
