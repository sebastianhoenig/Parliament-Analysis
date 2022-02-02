package XmlConnection;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlReader {
    ArrayList<Document> documentArrayList = new ArrayList<Document>();

    public ArrayList<Document> loopFiles() {

        File dir = new File("/home/seb/Desktop/Bundestag_19/");

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                documentArrayList.add(ProcessFile(file));
            }
        }
        return documentArrayList;
    }

    public Document getMetadataXml() {
        File dir = new File("C:\\Users\\User\\Desktop\\test");
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".XML")) {
                return ProcessFile(file);
            }
        }
        return null;
    }

    public Document ProcessFile(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(String.valueOf(file)));

            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}
