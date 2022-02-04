package protocolData;

import java.io.*;

public class downloading {

    // Checks if Folder/File already exists, otherwise it will be created.

    public static void load(int nameOfFile, int period, String linkToLoad) {
        String link = linkToLoad;

        File folder = new File("src/main/resources/" + period);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Directory is created!");
            }
        }

        File out = new File("src/main/resources/" + period + "/" + nameOfFile + ".xml");
        if (!out.exists()) {
            try {
                if (out.createNewFile()) {
                    Thread t = new Thread(new sorting.protocolLoading(link, out));
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
