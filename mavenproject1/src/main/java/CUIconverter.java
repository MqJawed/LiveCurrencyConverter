import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CUIconverter {
    public static void main(String[] args) throws IOException {


        HashMap<Integer, String> hash = new HashMap<Integer, String>();

        hash.put(1, "USD");
        hash.put(2, "CAD");
        hash.put(3, "EUR");
        hash.put(4, "HKD");
        hash.put(5, "INR");

        int from, to;
        String fromcur, tocur;
        double amount;

        Scanner scanner = new Scanner(System.in);
        int check;
        boolean running = true;
        while (running) {
            System.out.println("Convert from: ");
            System.out.println("1: USD\t2: CAD\t3: EUR\t4: HKD\t5: INR");
            from = scanner.nextInt();
            while (from < 1 || from > 5) {
                System.out.println("Enter a valid choice: ");
                System.out.println("Convert from: ");
                System.out.println("1: USD\t2: CAD\t3: EUR\t4: HKD\t5: INR");
                from = scanner.nextInt();
            }
            fromcur = hash.get(from);

            System.out.println("Convert to: ");
            System.out.println("1: USD\t2: CAD\t3: EUR\t4: HKD\t5: INR");
            to = scanner.nextInt();
            while (to < 1 || to > 5) {
                System.out.println("Enter a valid choice: ");
                System.out.println("Convert to: ");
                System.out.println("1: USD\t2: CAD\t3: EUR\t4: HKD\t5: INR");
                to = scanner.nextInt();
            }
            tocur = hash.get(to);

            System.out.println("Enter amount to convert: ");
            amount = scanner.nextDouble();

            livecurrconverter(fromcur, tocur, amount);
            System.out.println("1.Convert again\t2.Exit");
            check = scanner.nextInt();
            if (check != 1) {
                running = false;
            }
        }
    }

    private static void livecurrconverter(String fromcurr, String tocurr, double amount) throws IOException {

        DecimalFormat df = new DecimalFormat("00.00");
        //String geturl = "https://exchangeratesapi.io/Latest?base="+tocurr+"&symbols="+fromcurr; // to get the website or api name in a string including the variable added to it
        String GetUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_t2H35kFRJCM9Dw0OnAgQM735bawUTMqYWWeX7WeK&currencies="+fromcurr+"&base_currency="+tocurr;
        URL url = new URL(GetUrl); // to convert the string into actual url
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); // to make connection using browser
        httpURLConnection.setRequestMethod("GET"); // what are we requesting to api

        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader buf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputline;
            StringBuffer response = new StringBuffer();

            while((inputline = buf.readLine()) != null)
            {
                response.append(inputline);
            }
            buf.close();

            //Now api will give us the value in return in jason format i.e, Javascript Object Notation
            //{"data": {"USD": 0.0120385914}} NB: from USD to INR
            //So we somehow need to understand jason format will import library and add it to the path

            JSONObject js = new JSONObject(response.toString()); //js will contain line no. 91

            Double exchangerate = js.getJSONObject("data").getDouble(fromcurr); //rate = (from/to)
            System.out.println(js.getJSONObject("data")); 
            System.out.println(exchangerate+"\n"); 
            System.out.println(df.format(amount) + fromcurr + "=" + df.format(amount/exchangerate) + tocurr);


        }
        else {
            System.out.println("Failed to set connection");
        }

    }
}
