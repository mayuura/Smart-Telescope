package com.example.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class uses the HttpURLConnection class to do an http request and fetch the data
 */
public class HttpUtils {
    /**
     * This method does the http request and fetched the data
     * @param urlString : Url of the web page
     * @return : the output data of the http request
     * @throws IOException
     */
    public static String fetchData(String urlString) throws IOException {
        URL url = new URL(urlString);
        //open connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } finally {
            //disconnect when data fetching is done
            urlConnection.disconnect();
        }
    }
}
