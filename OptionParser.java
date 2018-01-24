import org.apache.commons.cli.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OptionParser {

    public Options options;

    public OptionParser()
    {

        Options options=new Options();
        options.addOption("latitude",null,true,"Szerokość geograficzna na mapie");
        options.addOption("longtitude",null,true,"Długość geograficzna na mapie");
        options.addOption("sensor_id",null,true,"ID sensora");
        options.addOption("api_key",null,true,"Klucz api do airly");
        options.addOption("history",null,false,"Pokazanie historii pomiarów z 24 godzin z danego miejsca");
        options.addOption("h","help",false,"Pokaż pomoc");
        this.options=options;

    }

    public CommandLine parsedArgumets(String[] args) throws ParseException,IllegalArgumentException,IOException
    {
        CommandLineParser parser=new DefaultParser();


        CommandLine cmd=parser.parse(options,args,false);
        return cmd;
    }
    public void argsFormat(CommandLine cmd)throws IllegalArgumentException,IOException
    {
        if(cmd.getOptions().length==0) throw new IllegalArgumentException("Niewłaściwa liczba argumentów, podaj co najmniej jeden argument");
        if(!(cmd.hasOption("sensor_id") ||(cmd.hasOption("latitude")&&cmd.hasOption("longtitude"))))
        {
            throw new IllegalArgumentException("Konieczne jest podanie współrzędnych albo ID sensora");
        }
        if(!(System.getenv().containsKey("API_KEY"))&&!cmd.hasOption("api_key"))
        {
            throw new IllegalArgumentException("Zdefiniuj api key w zmiennej środowiskowej API_KEY , lub podaj opcję -api_key a po niej klucz api");
        }
        String apikey;
        if(System.getenv().containsKey("API_KEY"))
        {
            apikey=System.getenv("API_KEY");
        }else
            apikey=cmd.getOptionValue("api_key");
        //testing the connection to service with apikey
        HttpURLConnection test=(HttpURLConnection) new URL("https://airapi.airly.eu/v1/nearestSensor/measurements?latitude=50.06&longitude=20&maxDistance=1000").openConnection();
        test.setRequestMethod("GET");
        test.setRequestProperty("Accept","application/json");
        test.setRequestProperty("apikey",apikey);
        if(test.getResponseCode()!=200) {
            switch (test.getResponseCode()) {
                case 500:
                    throw new IllegalArgumentException("Nastąpił niespodziewany error ze strony serwera, spróbuj jeszcze raz");
                case 401:
                    throw new IllegalArgumentException("Nastąpił error ze strony serwera, powód : UNAUTHORIZED, podaj prawidłowy klucz API");
                case 404:
                    throw new IllegalArgumentException("Nie znaleziono żądanego elementu, spróbuj podać inne dane wejściowe");

                default:
                    throw new IllegalArgumentException("Podano zły klucz api, wprowadź prawidłowy klucz jeszcze raz.");
            }
        }
    }

}
