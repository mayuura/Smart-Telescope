package com.example.myapplication;

import java.util.List;

public class CelestialObject {
    private String identifier;
    private String type;
    private String coordinates;
    private String distance;//angular distance


    // Constructor to initialize the object
    public CelestialObject(String identifier,String type, String coordinates, String distance) {
        this.identifier = identifier;
        this.type=type;
        this.coordinates=coordinates;
        this.distance = distance;

    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public String getCoordinates() {
        return coordinates;
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

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "CelestialObject{" +
                "identifier='" + identifier + '\'' +
                ", type='" + type + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", distance=" + distance +
                '}';
    }

}
