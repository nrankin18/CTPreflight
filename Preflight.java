import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Preflight.java
 * A preflight tool that retrieves weather data for Conneticut area. Based on
 * Steven Schmidt's "Preflight.m"
 * @author Nathan Rankin
 * @version 1.1
 */
public class Preflight {

  public static void main(String[] args) {

    try {
      PrintWriter writer = new PrintWriter("preflight.txt");
      //Header
      writer.println("Mission # / Symbol / Sortie #/:" + System.getProperty("line.separator"));
      writer.println("Crew / FRO:" + System.getProperty("line.separator"));
      writer.println("-Compute Weight & Balance");
      writer.println("-Compute Takeoff & Landing Dist.");
      writer.println("-Review AFD (runway length, pattern proc., etc)");
      writer.println("-Check WX (Prog Charts, Radar, SIGMETS, AIRMETS, etc)");
      writer.println("-Check for TFRs");
      writer.println("-File Flight Plan");
      writer.println("Brief (takeoff/climb/cruise/descent/landing)" + System.getProperty("line.separator"));
      //FAs
      writer.println("Area Forecast (FA):");
      writer.print(readFAURL("https://www.aviationweather.gov/fcstdisc/data?cwa=KOKX"));
      writer.println(readFAURL("https://www.aviationweather.gov/fcstdisc/data?cwa=KBOX"));

      //METARs
      writer.println("Current Conditions (METARs):");
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KGON.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KSNC.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KMMK.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KOXC.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KHVN.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KBDR.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KBDL.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KHFD.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KIJD.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KPVD.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KOQU.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KWST.TXT"));
      writer.print(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KISP.TXT"));
      writer.println(readMETARURL("https://tgftp.nws.noaa.gov/data/observations/metar/stations/KHTO.TXT") + System.getProperty("line.separator"));
      writer.println("Terminal Aerodrome Forecast (TAFs):" + System.getProperty("line.separator"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KGON.TXT"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KBDL.TXT"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KPVD.TXT"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KISP.TXT"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KBDR.TXT"));
      writer.println(readTAFURL("https://tgftp.nws.noaa.gov/data/forecasts/taf/stations/KSWF.TXT"));
      writer.println("Winds Aloft:" + System.getProperty("line.separator"));
      writer.println(readWindsAloft() + System.getProperty("line.separator"));
      writer.println("Airmen Meteorological Information (AIRMETS):" + System.getProperty("line.separator"));
      writer.println(readAIRMETURL("https://forecast.weather.gov/product.php?site=NWS&product=WA1&issuedby=S"));
      writer.println(readAIRMETURL("https://forecast.weather.gov/product.php?site=NWS&product=WA1&issuedby=T"));
      writer.print(readAIRMETURL("https://forecast.weather.gov/product.php?site=NWS&product=WA1&issuedby=Z"));
      writer.close();
    } catch(FileNotFoundException e) {
        System.out.println("Error: FileNotFound: preflight.txt");
    }
  }

  /**
   * Reads a FA URL
   * @param  link the FA URL String.
   * @return      a String representation of the FA.
   */
  public static String readFAURL(String link) {
      String page = readFromURL(link);
      return page.substring(page.indexOf("<!-- raw data starts -->") + 24 , page.indexOf("<!-- raw data ends -->") - 1);
  }

  /**
   * Reads a METAR URL
   * @param  link the METAR URL String.
   * @return      a String representation of the METAR.
   */
  public static String readMETARURL(String link) {
    String id = link.substring(link.length() - 8, link.length() - 4);
    String page = readFromURL(link);
    return page.substring(page.indexOf(id) - 1, page.length() - 1);
  }

  /**
   * Reads a TAF URL
   * @param  link the TAF URL String.
   * @return      a String representation of the TAF.
   */
  public static String readTAFURL(String link) {
    String id = link.substring(link.length() - 8, link.length() - 4);
    String page = readFromURL(link);
    return page.substring(page.indexOf(id));
    }

    /**
     * Reads a Winds Aloft URL
     * @return      a String representation of the Winds Aloft.
     */
  public static String readWindsAloft() {
    String page = readFromURL("https://www.aviationweather.gov/windtemp/data?region=bos");
    return page.substring(page.indexOf("FD1US1"), page.indexOf("BGR")-1);
  }
  /**
   * Reads an AIRMET URL
   * @return      a String representation of the AIRMET.
   */
    public static String readAIRMETURL(String link) {
      String type = link.substring(link.length() - 1);
      String page = readFromURL(link);
      return page.substring(page.indexOf("WA1" + type), page.indexOf("....") + 1);
  }

  /**
   * Returns a String representation of a linked page.
   * @param  link the link.
   * @return      a String representation of the page.
   */
  public static String readFromURL(String link) {
    try {
      String page = "";
      URL url = new URL(link);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      String line;
      while ((line = reader.readLine()) != null)
        page += (line + System.getProperty("line.separator"));
      reader.close();
      return page;
    } catch (MalformedURLException e) {
        return "Error processing URL";
    } catch (IOException e1) {
        return "Error processing URL";
    }
  }
}
