package protocolData;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class parsing {

    public static void pars() {
        String[] periods = new String[]{"543410-543410", "866354-866354"};
        // Parse for the XML files
        for (String period : periods) {
            Document xml = null;
            try {
                xml = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + period +"?offset=" + 0).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int totalProtocols = Integer.parseInt(xml.selectXpath("/html/body/div[1]").first().attr("data-hits"));
            int xmlName = totalProtocols;
            for (int selectedPage = 0; selectedPage <= totalProtocols; selectedPage += 10) {
                try {
                    xml = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + period +"?offset=" + selectedPage).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements linkDivs = xml.getElementsByAttribute("href");
                for (Element protocolXML : linkDivs) {
                    String url = protocolXML.attr("href");
                    url = "https://www.bundestag.de" + url;
                    if (period.equals("543410-543410")) {
                        downloading.load(xmlName, 19, url);
                    } else {
                        downloading.load(xmlName, 20, url);
                    }
                    xmlName--;
                }
            }
        }
    }

}
