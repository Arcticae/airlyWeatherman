import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.LinkedList;


import static com.sun.xml.internal.ws.util.StringUtils.capitalize;

public class PollutionData {

    public String sensorID;
    public JsonObject sensorInfo;
    public JsonObject currentMeasurements;
    private JsonArray historyMeasurements;

    public PollutionData (String latitutde,String longtitude,String apikey) throws IOException,IllegalArgumentException
    {


           String url = "https://airapi.airly.eu/v1/nearestSensor/measurements?" + "latitude=" + latitutde + "&longitude=" + longtitude + "&maxdistance=1000";
           if(!AirlyJson.getJson(url,apikey).has("id")) throw new IllegalArgumentException("Nie ma sensora o podanej lokalizacji lub id.");
           this.sensorID = AirlyJson.getJson(url,apikey).getAsJsonPrimitive("id").getAsString();
           url = "https://airapi.airly.eu/v1/sensors/" + sensorID;
           this.sensorInfo = AirlyJson.getJson(url,apikey).getAsJsonObject();
           url = "https://airapi.airly.eu/v1/sensor/measurements?sensorId=" + sensorID;
           this.currentMeasurements = AirlyJson.getJson(url,apikey).getAsJsonObject("currentMeasurements");
           this.historyMeasurements = AirlyJson.getJson(url,apikey).getAsJsonArray("history");


    }
    public PollutionData (String sensorNumber,String apikey)throws IOException
    {

        this.sensorID=sensorNumber;

        String url="https://airapi.airly.eu/v1/sensors/"+sensorID;
        this.sensorInfo=AirlyJson.getJson(url,apikey).getAsJsonObject();

        url = "https://airapi.airly.eu/v1/sensor/measurements?sensorId="+sensorID;
        this.currentMeasurements=AirlyJson.getJson(url,apikey).getAsJsonObject("currentMeasurements");

        this.historyMeasurements=AirlyJson.getJson(url,apikey).getAsJsonArray("history");

    }


    public LinkedList<String> sensorAddress()
    {
        LinkedList<String> address=new LinkedList<>();
        JsonObject addressJson=this.sensorInfo.getAsJsonObject("address");
        for(String identifier: addressJson.keySet())
        {
            if(addressJson.get(identifier).isJsonPrimitive())
            {
                address.addFirst(addressJson.get(identifier).getAsString());
            }

        }
        address.set(2,address.get(2)+" "+address.get(3));
        address.removeLast();
        return address;


    }

    public LinkedList<String> otherInfo()
    {
        LinkedList<String> info=new LinkedList<>();

        for(String identifier:this.sensorInfo.keySet())
        {
            if(sensorInfo.get(identifier).isJsonPrimitive()&&!identifier.equals("pollutionLevel"))info.add(capitalize(identifier)+" "+sensorInfo.get(identifier).getAsString());
        }
        return info;
    }
    public JsonArray getHistoryMeasurements()
    {
        return this.historyMeasurements;
    }

}
