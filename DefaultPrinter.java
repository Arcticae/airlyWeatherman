import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import com.google.gson.JsonObject;
import  com.sun.xml.internal.ws.util.StringUtils.*;

import static com.sun.xml.internal.ws.util.StringUtils.capitalize;

public class DefaultPrinter {

    public PollutionData data;

    public DefaultPrinter(PollutionData in)
    {
        this.data=in;
    }


    public void printSensorNormal() throws IOException{
        //basic info
        LinkedList<String> address = data.sensorAddress();
        LinkedList<String> additionalInfo = data.otherInfo();
        LinkedList<String> mainInfo = new LinkedList<>();
        String lines = "──────────────────────────────────────────────────────";
        if (!data.currentMeasurements.keySet().isEmpty()){
            for (String key : data.currentMeasurements.keySet()) {
                if (data.currentMeasurements.get(key).isJsonPrimitive()) {
                    String number = getProperNumber(data.currentMeasurements, key);


                    mainInfo.add(capitalize(key) + ": " + number + " " + getUnit(key));
                }
            }
        }
        else
        {

            throw new IOException("Pobrana paczka danych jest pusta. Spróbuj użyć innego sensora");}
        System.out.println("Dane pomiarowe dla sensora o ID: "+data.sensorID);
        System.out.println(lines.substring(0,mainInfo.get(0).length()+5));
        for(String line:mainInfo)
        {

            System.out.print("|"); System.out.println(line);
            System.out.println(lines.substring(0,line.length()+5));
        }
        System.out.println("Adres sensora: ");
        for(String line:address) {
            System.out.print(line);
            if (!address.getLast().equals(line)) System.out.print(", ");
        }
        System.out.println("");


    }


    public String getUnit(String descriptor)
    {
        switch(descriptor.toLowerCase())
        {
            case "airqualityindex":
                return "";
            case "pm1":
                return "μg/m^3";
            case "pm25":
                return "μg/m^3";
            case "pm10":
                return "μg/m^3";
            case "pressure":
                return "hPa";
            case "humidity":
                return "%";
            case "temperature":
                return "°C";
            default:
                return "";
        }


    }

    public String getProperNumber(JsonObject data,String key)
    {
        switch(key.toLowerCase())
        {
            case "airqualityindex":
                return String.valueOf((int)data.get(key).getAsDouble());
            case "pm1":
                return String.valueOf((int)data.get(key).getAsDouble());
            case "pm25":
                return String.valueOf((int)data.get(key).getAsDouble());
            case "pm10":
                return String.valueOf((int)data.get(key).getAsDouble());
            case "pressure":
                return String.valueOf((int)data.get(key).getAsDouble()/100);
            case "humidity":
                return String.valueOf((Math.round(data.get(key).getAsDouble()*10.0))/10.0);
            case "temperature":
                return String.valueOf((Math.round(data.get(key).getAsDouble()*10.0))/10.0);
            case "pollutionlevel":
                return String.valueOf((int)(data.get(key).getAsDouble()));
            default:
                return "";
        }


    }

    public void getSensorInfo()
    {
        System.out.println("");
    }
}
