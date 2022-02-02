package protocolData.old;


/*public interface httpParsing {

    class loadData {

        public static void main(String[] args) throws Exception {

            String [] links = new String[0];

            // String finalZip = null;

            try {

                List<String> zipArr = new ArrayList<>();

                // Parse for the ZIP file
                Document zipFile = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/488214-488214").get();
                String finalZip = null;
                for (Element tableZip : zipFile.select("table")) {
                    for (Element rowZip : tableZip.select("tr")) {
                        Elements tdsZip = rowZip.select("td div.bt-documents-description ul.bt-linkliste li a");
                        // System.out.println(tdsZip);

                        String zipString = String.valueOf(tdsZip);
                        boolean containsStr = zipString.contains("pp19");

                        if (containsStr) {
                            //System.out.println(containsStr);
                            //System.out.println(tdsZip);

                            String[] parts = zipString.split("href=\"/");
                            String part1 = parts[1];
                            //System.out.println(part1);

                            String[] parts2 = part1.split("\" target=\"_blank\"> ZIP | 51 MB</a>");
                            String part12 = parts2[0];
                            //System.out.println(part12);

                            finalZip = "https://www.bundestag.de/" + part12;
                            //System.out.println(finalZip);

                            // UnzipFile(String source, String target);


                            // zipArr.add(zipString);
                        }
                    }
                    // System.out.println("Modified ArrayList:\n" + zipArr);

                }

                File folder = new File("src/main/resources/loadedProtocols");
                String link = finalZip;
                File out = new File("src/main/resources/loadedProtocols/test.zip");
                new Thread(new protocolLoading(link, out)).start();

                // TODO:     https://www.bundestag.de/resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip   ^^^^^^^
                // TODO:                             /resource/blob/870686/91b713c492499db98eec5b2f8f142d20/pp19-data.zip   |||||||


                int count = 0;
                // Parse for the XML files
                for (int selectedPage = 0; selectedPage <= 231; selectedPage = selectedPage + 10) {
                    Document xml19 = Jsoup.connect("https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=" + selectedPage).get();

                    for (Element table : xml19.select("table")) {
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td div.bt-documents-description ul.bt-linkliste li a");
                            // System.out.println(tds);
                            String xmlStrings = String.valueOf(tds);
                            // System.out.println(xmlStrings);

                            /*String[] partsXML = xmlStrings.split("ML | 40");
                            String partsXML1 = partsXML[1];
                            System.out.println(partsXML1);

                            String[] parts2 = partsXML1.split("\" target=\"_blank\"> ZIP | 51 MB</a>");
                            String partsXML12 = parts2[0];
                            //System.out.println(partsXML12);

                            String finalXML = "https://www.bundestag.de/" + partsXML12;
                            //System.out.println(finalZip);*/



                            /*char[] chars = s.toCharArray();
                            int len = chars.length; */

                            // String tds = String.valueOf(tds);
                            /*if (s.equals("")) {

                            } else {
                                count ++;
                            }*/

                            /*for (int j = 0; j <= len); j ++) {
                                if (s[j].equals("h") && s[j+1].equals("r") && s[j+2].equals("e") && s[j+3].equals("f") {
                                    for (int z = j+4, s.length(); z++)
                                }
                            }
                            // links.add(s);

                            //System.out.println(tds.getClass().getSimpleName());
                            //System.out.println(s.getClass().getSimpleName());

                        }
                    }
                    // System.out.println(count);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            /*File folder = new File("src/main/java/protocolData/testpls");
            String link = doc.html();
            File ht = new File("src/main/java/protocolData/testpls/pls.html");
            new Thread(new protocolLoading(link, ht)).start();
            ht.createNewFile();

        }
    }

} */
