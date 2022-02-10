package DownloadData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ScrapePictures {
    String searchUrl;
    String[] pictureAndData;

    public ScrapePictures(String name, String surname) {
        this.searchUrl = getSearchUrl(name, surname);
        this.pictureAndData = performSearch();
    }


    String getSearchUrl(String name, String surname) {
        String basicUrl = "https://bilddatenbank.bundestag.de/search/picture-result?query=";
        String specificPerson = String.format("%1s+%2s",name, surname);
        return String.format("%1s%2s", basicUrl, specificPerson);
    }

     String[] performSearch() {
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

            //String[] altContent = pageElements.attr("title").split(":");
            /*String altContent = pageElements.attr("title"); //TODO: MAKE REGEX WORK
            String description = null;
            String photographer = null;
            String matchDesc = "(?<=Beschreibung:)(.*)(?=Fotograf)";
            Pattern patterOne = Pattern.compile(matchDesc);
            Matcher matcherOne = patterOne.matcher(altContent);
            while (matcherOne.find()) {
                for (int j=0; j<matcherOne.groupCount(); j++) {
                    description = matcherOne.group(j);
                    System.out.println(description);
                }
            }
            String matchPhotographer = "(?<=Fotograf/in:)(.*)";
            Pattern patterTwo = Pattern.compile(matchPhotographer);
            Matcher matcherTwo = patterTwo.matcher(altContent);
            while (matcherTwo.find()) {
                for (int j=0; j<matcherTwo.groupCount(); j++) {
                    photographer = matcherTwo.group(1);
                    System.out.println(photographer);
                }
            };*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getMetaData() {
        return this.pictureAndData;
    }
}
