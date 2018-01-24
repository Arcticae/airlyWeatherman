import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryHandler {

    private JsonArray historyData;
    public HistoryHandler(PollutionData data)
    {
        this.historyData=data.getHistoryMeasurements();
    }

    public void printHistory()
    {
        System.out.println("\n├────────┤ Historia Pomiarów ├──────┤");

        System.out.println("├ Hours |   Pm10     |    Pm25    ┤");
        System.out.println("├──────────────────────────────────────┤");
        for(int i=0;i<historyData.size();i++)
        {
            JsonObject block=historyData.get(i).getAsJsonObject();
            System.out.print("| "+getHour(block.get("fromDateTime").getAsString())+" |");
            JsonObject measurement=block.get("measurements").getAsJsonObject();
            System.out.print("     "+String.valueOf((int)measurement.get("pm10").getAsDouble())+"     |");
            System.out.print("     "+String.valueOf((int)measurement.get("pm25").getAsDouble())+"    \n");
            System.out.println("| "+getHour(block.get("tillDateTime").getAsString())+" |     "+getDate(block.get("tillDateTime").getAsString()));
            System.out.println("├──────────────────────────────────────┤");
        }



    }

    private String getHour(String hour)
    {
        Pattern hourPattern=Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        Matcher mat =hourPattern.matcher(hour);
        if(mat.find())
        {
            return hour.substring(mat.start(),mat.end());
        }
        return null;
    }
    private String getDate(String dateTime)
    {
        Pattern datePattern=Pattern.compile("^\\d{4}-\\d{2}-\\d{2}");
        Matcher mat =datePattern.matcher(dateTime);
        if(mat.find())
        {
            return dateTime.substring(mat.start(),mat.end());
        }
        return null;
    }
}
