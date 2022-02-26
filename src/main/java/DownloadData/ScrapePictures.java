package DownloadData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * @author Sebastian Hönig
 * Class used for scraping picture of parliament members from the bundestag website
 */

public class ScrapePictures {
    String searchUrl;
    String[] pictureAndData;

    public ScrapePictures(String name, String surname) {
        this.searchUrl = getSearchUrl(name, surname);
        this.pictureAndData = performSearch();
    }


    String getSearchUrl(String name, String surname) {
        /**
         * @author Sebastian Hönig
         * returns the url needed for the search
         */
        String basicUrl = "https://bilddatenbank.bundestag.de/search/picture-result?query=";
        String specificPerson = String.format("%1s+%2s",name, surname);
        return String.format("%1s%2s", basicUrl, specificPerson);
    }

     String[] performSearch() {
         /**
          * @author Sebastian Hönig
          * traversers the dom-tree to get the correct picture
          */
        Document page = null;
        try {
            page = Jsoup.connect(this.searchUrl).get();
            Element pageElements = page.select(".col-xs-12.col-sm-12.rowGridContainer .item a img").first();
            if (pageElements == null) {
                return new String[]{"No picture available", "No desc. available"};
            }
            String src = String.format("https://bilddatenbank.bundestag.de%1s", pageElements.attr("src"));
            String metaData = pageElements.attr("title");
            return new String[]{src, metaData};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author Sebastian Hönig
     * @return the picture and picture subtext of the parliament member
     */
    public String[] getMetaData() {
        return this.pictureAndData;
    }
}
