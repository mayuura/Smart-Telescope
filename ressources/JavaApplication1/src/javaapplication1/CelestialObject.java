/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import static javaapplication1.JavaApplication1.getDecFromJson;
import static javaapplication1.JavaApplication1.getIdFromJson;
import static javaapplication1.JavaApplication1.getRaFromJson;
import static javaapplication1.JavaApplication1.getSepFromJson;

/**
 *
 * @author embouddi
 */
public class CelestialObject {
    private String name;
    private String rightAscension;
    private String declination;
    private double distance;//angular distance
    

    // Constructor to initialize the object
    public CelestialObject(String name, String rightAscension, String declination, double distance) {
        this.name = name;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.distance = distance;
        
    }
    //creates  the celestial object in the output corresponding to the index
    public CelestialObject(List<String> outputData,int i) {
      
        // get attributes
        this.name=getIdFromJson(outputData.get(i));
        this.rightAscension=getRaFromJson(outputData.get(i));
        this.declination=getDecFromJson(outputData.get(i));
        String distance=getSepFromJson(outputData.get(i));
        this.distance=Double.parseDouble(distance);
        //create the instance
         
    }
    public static List<CelestialObject> getFromOutput(List<String> outputData){
        List<CelestialObject> objects=new ArrayList<>();
        for(int i=0;i<outputData.size()-1;i++){
            CelestialObject obj= new CelestialObject(outputData,i);
            objects.add(obj);
        }
        return objects;
    }

    // Getters to access the attributes
    public String getName() {
        return name;
    }

    public String getRightAscension() {
        return rightAscension;
    }

    public String getDeclination() {
        return declination;
    }

    public double getApparentMagnitude() {
        return distance;
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

    // You can add additional methods as needed for specific operations related to celestial objects.

    @Override
    public String toString() {
        return "CelestialObject{" + "name=" + name + ", rightAscension=" + rightAscension + ", declination=" + declination + ", distance=" + distance + '}';
    }
}

