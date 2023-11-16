/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simbad;

/**
 *
 * @author embouddi
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SIMBADQuery {
    public static String querySIMBAD(Double ra, Double dec, int radius) {
        try {
            // Construct the URL based on your query, including coordinates and search radius
            String query = "http://simbad.u-strasbg.fr/simbad/sim-script?script=" + "get coo " + ra + " " + dec + " radius=" + radius;

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

            // Handle the SIMBAD result as needed

            return simbadResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}

