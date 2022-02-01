package protocolData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public interface httpParsing {

    class loadData {

        public static void main(String[] args) throws Exception {

            String[] links = new String[0];

            // System.out.println("Der Titel der Website: " + doc.title());
            // die ganze Seite als Html
            //  System.out.println("Wir geben mal die ganze Seite als Html aus:" + doc.html());

            try {

                // Parse for the XML files
                for(int selectedPage = 0; selectedPage <= 231; selectedPage = selectedPage + 10) {
                    Document xml19 = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offsett=" + selectedPage).get();

                    for (Element table : xml19.select("table")) {
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td div.bt-documents-description ul.bt-linkliste li a");
                            //System.out.println(tds);
                            String s = String.valueOf(tds);
                            // links.add(s);

                            System.out.println(tds.getClass().getSimpleName());
                            System.out.println(s.getClass().getSimpleName());

                        }
                    }
                }

                // Parse for the ZIP file
                Document doc = Jsoup.connect("https://www.bundestag.de/services/opendata").get();


                /*ArrayList<String> downServers = new ArrayList<>();
                Element table = doc.select("table.table.bt-table-data").get(0); //select the first table.
                System.out.println(table);*/

                // Elements links = doc.select("div.bt-slide.col-xs-12.bt-standard-content.slick-slide.slick-current.slick-active a[href]");

                /*System.out.println(links.select("tbody tr").size());
                System.out.println(links.attr("href"));*/

                /*for (Element step : body) {
                    String url = step.attr("href");
                    System.out.println(url);

                    String test = step.select("strong").text();
                    System.out.println(test);
                }*/

                /*for (Element row :  doc.select("" + "table.table.bt-table-data tr")) {
                    if (row.select("td:data-th="Titel"").text().equals("")) {
                        continue;                                                                       // TODO : bei einzelnen XMLs soll da weitergeblättert werden
                    } else {
                        final String ticker = row.select("td:data-th="Titel"").text();
                        System.out.println(ticker);
                    }

                } */
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            /*File folder = new File("src/main/java/protocolData/testpls");
            String link = doc.html();
            File ht = new File("src/main/java/protocolData/testpls/pls.html");
            new Thread(new protocolLoading(link, ht)).start();
            ht.createNewFile();*/
            // System.out.println("Gesucht: Plenar 19  ! ---- Gefunden: " + doc.title());



        }
    }

}
