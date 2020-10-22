package utility;

import java.io.*;
import java.util.Properties;

public class PropertiesFile {
    Properties prop = new Properties();
    InputStream input = null;
    OutputStream output = null;


    public PropertiesFile() {

        try {
            input = new FileInputStream(ConstantUtil.CONFIG_FILE_NAME);
            if (input != null) {
                prop.load(input);
                System.out.println(prop.getProperty("fromLanguage"));
                System.out.println(prop.getProperty("toLanguage"));
            } else {

            }
        } catch (FileNotFoundException e) {
            try {
                System.out.println("File Not Found. Creating New File...");
                output = new FileOutputStream("config.properties");
                prop.setProperty("fromLanguage", "English");
                prop.setProperty("toLanguage", "Japanese");
                prop.store(output, null);
                input = new FileInputStream("config.properties");
                prop.load(input);
            } catch (FileNotFoundException e1) {
                System.out.println("File Not Found. Creating New File...");
            } catch (IOException e1) {
                System.out.println("File Not Found. Creating New File...");
            }
        } catch (IOException e) {
            System.out.println("File Not Found. Creating New File...");
        }

    }

    public void setProperty(String propName, String value) {
        try {
            output = new FileOutputStream("config.properties");
            prop.setProperty(propName, value);
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getProperty(String propName) {
        try {
            prop.load(input);
            return prop.getProperty(propName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
