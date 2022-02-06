package database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * Class for the config property of MongoDB
 * @author benwerner
 * @date 27.01.2022
 */
public class PropertiesFile {

    /**
     * Methode to read am properties file.
     * @return Properties
     */
    public static Properties readPropertiesFile(){
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/resources/PRG_WiSe21_Gruppe_8_4");
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
