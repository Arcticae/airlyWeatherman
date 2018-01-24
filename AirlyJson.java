import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class AirlyJson{




    public static JsonObject getJson(String url,String apikey) throws IOException,IllegalArgumentException
    {



                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", apikey);
                connection.setRequestProperty("Accept", "application/json");
                JsonParser parser = new JsonParser();
                JsonObject data = parser.parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
                return data;




    }

}
