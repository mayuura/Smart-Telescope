/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;

/**
 *
 * @author embouddi
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


public class SIMBADQuery {
    public static String querySIMBAD(Double ra, Double dec, int radius) {
        // Define your proxy server details
        String proxyHost = "172.16.10.250";
        int proxyPort = 3128;  

        // Set proxy properties for HTTP and HTTPS
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", String.valueOf(proxyPort));

        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", String.valueOf(proxyPort));
        try {
            // Construct the URL based on your query, including coordinates and search radius
            String query = "https://simbad.cds.unistra.fr/simbad/sim-coo?Coord="+ra+"d+"+dec+"d&CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius="+radius+"&Radius.unit=arcmin&submit=submit+query&CoordList=";
            //String query="https://www.google.com";
            // Create an HTTP connection
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the response and extract relevant information
            String simbadResult = response.toString();
            
            // Read the response and parse using SAVOT
        InputStream inputStream = conn.getInputStream();
        System.out.println(simbadResult);
        

             // Handle the SIMBAD result as needed
             writeToFile("simbadResult.txt", simbadResult.toString());
            
            return simbadResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
     public static void writeToFile(String filename, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(content);
            writer.close();
            System.out.println("Result written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

