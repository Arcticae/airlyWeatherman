import com.google.gson.JsonSyntaxException;
import org.apache.commons.cli.*;
import java.io.FileNotFoundException;
import java.io.IOException;

//użyte biblioteki - GSON 2.8, apache.commons.cli
public class Main {

    public static void main(String[] args)
    {
        OptionParser optionParser=new OptionParser();
        try {

            CommandLine cmd=optionParser.parsedArgumets(args);

            HelpFormatter help=new HelpFormatter();
            if (cmd.hasOption("help"))
            {
                help.printHelp("Argumenty programu mają następującą składnię: (history jest opcjonalne)",optionParser.options);
            }else
            {
                optionParser.argsFormat(cmd);

                String apikey;
                if(System.getenv().containsKey("API_KEY"))
                {
                    apikey=System.getenv("API_KEY");
                }else
                    apikey=cmd.getOptionValue("api_key");

                String latitude=cmd.getOptionValue("latitude");
                String longtitude=cmd.getOptionValue("longtitude");
                String sensorID=cmd.getOptionValue("sensor_id");
                PollutionData pack;



                if(sensorID!=null)
                {
                    pack=new PollutionData(sensorID,apikey);
                }
                else
                {
                    pack=new PollutionData(latitude,longtitude,apikey);
                }

                if(cmd.hasOption("history"))
                    {
                    DefaultPrinter printer=new DefaultPrinter(pack);
                    printer.printSensorNormal();
                    HistoryHandler history=new HistoryHandler(pack);
                    history.printHistory();
                }else
                {
                    DefaultPrinter printer=new DefaultPrinter(pack);
                    printer.printSensorNormal();
                }
            }

        }
        catch(ParseException ex)
        {
            System.out.println("Przetwarzanie argumentów nie powiodło się :"+ex.getMessage());
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Nie znaleziono sensora z podanymi danymi wejściowymi, spróbuj jeszcze raz");
        }
        catch(IllegalArgumentException ex)
        {
            System.out.println(ex.getMessage());
            HelpFormatter help=new HelpFormatter();
            help.printHelp("Użycie programu: " ,optionParser.options);
        }
        catch(IOException ex)
        {
            System.out.println(" Nie znaleziono sensora o podanej lokalizacji, wprowadź poprawną lokalizację");
        }
        catch(JsonSyntaxException ex)
        {
            System.out.println("Malformed json from server");
        }

    }


}
