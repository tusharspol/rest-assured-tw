package utility;




import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;



public class FrameworkUtility {

    protected static Properties properties;

    public static String getProperty(String key) {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(FrameworkConstant.CONFIG_FILE_PATH));

        } catch (Exception e) {
            System.out.println("Cannot find key: " + key + " in Config file due to exception : " + e);
        }
        return properties.getProperty(key).trim();

    }


    public static JSONObject loadRequestWithData(HashMap<String, String> map) {
        JSONObject requestParams = new JSONObject();
        for(Map.Entry<String,String> jsonEntry:map.entrySet())
            requestParams.put(jsonEntry.getKey(), jsonEntry.getValue());

       return requestParams;

    }



}