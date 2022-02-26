package DownloadData;

import me.tongfei.progressbar.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * The class Parse runs the parsing process
 * @author Emmelina und Sebastian
 * @return int nameOfFile
 * @return int period
 * @return String linkToLoad
 */

public class Parse {

    /**
     * Calls up the respective subpage of the election period, changing the URL in places.
     * All XMLs from the 19/20 election periods are passed on to the DownloadXml.java class with the identifier 19 or 20.
     */

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
            ProgressBar progressBar = new ProgressBar("Downloading XML-Files :", totalProtocols);
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
                        DownloadXml.load(xmlName, 19, url);
                    } else {
                        DownloadXml.load(xmlName, 20, url);
                    }
                    xmlName--;
                    progressBar.step();
                }
            }
            progressBar.close();
        }
        DownloadXml.loadDTD();
    }
}
