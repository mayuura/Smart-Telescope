/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication1;

import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author embouddi
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Double ra=3908.4429453; //in degrees
        Double dec=-1.442668530641793;
        int radius=3;
        List<String> outputData=CallPythonScript.call(ra,dec,radius);
        //printData(outputData);
        //System.out.println(outputData.get(1));
        /*String[] line1_split =CallPythonScript.call(ra,dec,radius).get(1).split("   ");
        printSplit(line1_split);*/
        //normally the index 1 should be a variable that corresponds to the user choice 
        //List<CelestialObject> objects=CelestialObject.getFromOutput(outputData);
        //System.out.print(objects.get(0).toString());
        //System.out.print(objects.size());
        List<CelestialObject> objects=CelestialObject.getFromOutput(outputData);
        System.out.println(objects.get(0));
        //System.out.println(getIdFromJson(outputData.get(0)));
    }
    public static void printData(List<String> data){
        for(String line : data){
            System.out.println(line);
        }
    }
    public static void printSplit(String[] line){
        for(String string:line){
            System.out.println(string);
        }
    }
    public static String getIdFromJson(String jsonString){
        // Parse the JSON string into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Get the value associated with the "MAIN_ID" key
        String mainId = jsonObject.get("MAIN_ID").getAsString();

        // Print the value of "MAIN_ID"
        return mainId;
    }
       public static String getRaFromJson(String jsonString){
        // Parse the JSON string into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Get the value associated with the "MAIN_ID" key
        String mainId = jsonObject.get("RA").getAsString();

        // Print the value of "MAIN_ID"
        return mainId;
    }
          public static String getDecFromJson(String jsonString){
        // Parse the JSON string into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Get the value associated with the "MAIN_ID" key
        String mainId = jsonObject.get("DEC").getAsString();

        // Print the value of "MAIN_ID"
        return mainId;
    }
             public static String getSepFromJson(String jsonString){
        // Parse the JSON string into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Get the value associated with the "MAIN_ID" key
        String mainId = jsonObject.get("AngularSeparation_arcsec").getAsString();

        // Print the value of "MAIN_ID"
        return mainId;
    }
}
