package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to describe a celestial object according on its description in the SIMBAD
 * website . The attributes chosen here are the relevant data we need to achieve our goal .
 */
public class CelestialObject {
    private String identifier;
    private String type;
    private String ra;
    private String dec;
    private String distance;//angular distance


    // Constructor to initialize the object
    public CelestialObject(String identifier,String type, String coordinates, String distance) {
        this.identifier = identifier;
        String typeDescription=SimbadObjectType.getDescription(type);
        this.type=type+": "+typeDescription;
        this.ra=coordinateSplitter(coordinates).get(0);
        this.dec=coordinateSplitter(coordinates).get(1);
        this.distance = distance;

    }

    /**
     * This method splits the coordinates string from the simbad query to Ra and Dec
     * @param coordinates : Simbad string for coordinates
     * @return :a list containing Ra and Dec data
     */
    public List<String> coordinateSplitter(String coordinates){
        List<String> out = new ArrayList<>();
        String[] parts = coordinates.split("\\s*[+-]\\s*");
        boolean sign=coordinates.contains("+");
        // parts[0] will contain the first substring
        // parts[1] will contain the second substring

            String firstSubstring = parts[0];
            String secondSubstring = parts[1];
            if(sign){
                secondSubstring = "+"+parts[1];
            }
            else {
                secondSubstring = "-"+parts[1];
            }

            out.add(firstSubstring);
            out.add(secondSubstring);

        return out;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public String getRa() {
        return ra;
    }
    public String getDec(){
        return dec;
    }

    public String getDistance() {
        return distance;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }
    public void setDec(String dec){
        this.dec=dec;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "CelestialObject{" +
                "identifier='" + identifier + '\'' +
                ", type='" + type + '\'' +
                ", coordinates='" + ra + " "+dec+ '\'' +
                ", distance=" + distance +
                '}';
    }

    /**
     *This is a mock method used to test my logic in parisng the simbadResponse
     * @param simbadResponse
     * @return
     */
    public static List<String> asciiParser(String simbadResponse) {

        // Split the response into lines
        String[] lines = simbadResponse.split("\n");
        List<String> data = new ArrayList<>();
        for (int i = 5; i < lines.length; i++) {
            String[] columns = lines[i].split("\\|");

            String number = columns[0];
            String distAsec = (columns.length > 1) ? columns[1] : "";
            String identifier = (columns.length > 2) ? columns[2] : "";
            String type = (columns.length > 3) ? columns[3] : "";
            String coord = (columns.length > 4) ? columns[4] : "";
            data.add(number);
            data.add(distAsec);
            data.add(identifier);
            data.add(type);
            data.add(coord);
        }
        return data;
    }
    public static String paresedDataToString(List<String> list){
        String out="";
        for(String line : list){
            out+=line+"\n";
        }
        return out;
    }

    /**
     *
     * @param simbadResponse
     * @return
     */
    /**
     * This method parses the simbadResponse and creates the correspond instances of the Celestial Object class
     * @param simbadResponse
     * @return
     */
    public static List<CelestialObject> asciiToObjects(String simbadResponse) {

        // Split the response into lines
        String[] lines = simbadResponse.split("\n");
        List<CelestialObject> list = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {


            String[] columns = lines[i].split("\\|");
            // Skip lines that are headers or irrelevant
            if (columns.length < 5 || !isInteger(columns[0])) {
                continue;
            }
            String number = columns[0];
            String distAsec = (columns.length > 1) ? columns[1] : "";
            String identifier = (columns.length > 2) ? columns[2] : "";
            String type = (columns.length > 3) ? columns[3] : "";
            String coord = (columns.length > 4) ? columns[4] : "";
            Log.d("Type : ",type);

            //create the object
            CelestialObject object=new CelestialObject(identifier,type,coord,distAsec);
            list.add(object);
        }
        return list;
    }
    public static String listOfObjectsToString(List<CelestialObject> list){
        String out="";
        for(CelestialObject object : list){
            out+=object.toString()+"\n";
        }
        return out;
    }
    public static  boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
