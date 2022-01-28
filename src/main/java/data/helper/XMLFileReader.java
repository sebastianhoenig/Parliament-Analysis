package data.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLFileReader {
    ArrayList<Document> allXmlArrayList = new ArrayList<Document>();

    /**
     * Die Funktion getAllFiles() benötigt den genauen Adresspfad des Ordners, in welchem die xml-Dateien liegen.
     * @return Arrayliste mit den geparsten xml Dateien
     */
    public ArrayList<Document> getAllFiles() {
        File folder = new File("/home/vanessa/Dokumente/Bundestag_19/");
        File[] files = folder.listFiles();
        assert files != null;
        for(File file : files) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                System.out.println("File: "+file);
                try{allXmlArrayList.add(ProcessFile(file));}
                catch (NullPointerException e){e.printStackTrace();
                }
            }}

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
