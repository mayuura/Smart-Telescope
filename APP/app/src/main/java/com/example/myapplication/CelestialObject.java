package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

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

}
