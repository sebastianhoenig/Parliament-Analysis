package protocolData;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class parsing {

    public static void pars() {

        try {

            // Parse for the ZIP file

            Document zipFile = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/488214-488214").get();
            String finalZip = null;

            for (Element tableZip : zipFile.select("table")) {
                for (Element rowZip : tableZip.select("tr")) {
                    Elements tdsZip = rowZip.select("td div.bt-documents-description ul.bt-linkliste li a");

                    String zipString = String.valueOf(tdsZip);
                    boolean containsStrZIP = zipString.contains("pp19");

                    if (containsStrZIP) {

                        String[] parts = zipString.split("href=\"/");
                        String part1 = parts[1];

                        String[] parts2 = part1.split("\" target=\"_blank\"> ZIP | 51 MB</a>");
                        String part12 = parts2[0];

                        finalZip = "https://www.bundestag.de/" + part12;

                        // downloading.loadd (0, finalZip);

                    }

                }
            }

            // Parse for the XML files

            int xmlname = 1;
            for (int selectedPage = 0; selectedPage <= 10; selectedPage = selectedPage + 10) {
                Document xml19 = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=0" + selectedPage).get();

                for (Element tableXML : xml19.select("table")) {
                    for (Element rowXML : tableXML.select("tr")) {
                        Elements tdsXML = rowXML.select("td div.bt-documents-description ul.bt-linkliste li a");

                        String xmlStrings = String.valueOf(tdsXML);
                        String[] word = xmlStrings.split("\" href=\"");

                        String xmlString = String.valueOf(tdsXML);
                        boolean containsStrXML = xmlString.contains("xml");

                        if (containsStrXML) {

                            String[] partsxml = xmlString.split("t\" href=\"");
                            String partOfLink = partsxml[1];

                            String strArray[] = partOfLink.split(" ");
                            String nearlyXML = String.valueOf(strArray[0]);

                            int index = 0;
                            String newXML = "";
                            char[] chars = nearlyXML.toCharArray();
                            for (char ch : chars) {
                                if(index >= 69) {
                                    continue;
                                } else {
                                    newXML = newXML + ch;
                                }
                                index ++;
                            }

                            String finalXML = "https://www.bundestag.de" + newXML;

                            downloading.loadd (xmlname, finalXML);
                            xmlname ++;

                        }

                    }
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
