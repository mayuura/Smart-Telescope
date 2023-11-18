package com.example.myapplication;

public class TelescopeAdjustmentCalculator {

    private static final double ARCSECONDS_PER_DEGREE = 3600.0;

    public static String calculateTelescopeAdjustment(double angularSeparation, double fieldOfView) {
        // Calculate azimuth adjustment in degrees
        double azimuthAdjustment = angularSeparation / fieldOfView;

        // Calculate altitude adjustment in degrees
        double altitudeAdjustment = angularSeparation / ARCSECONDS_PER_DEGREE;

        // Determine the direction for azimuth adjustment
        String azimuthDirection = (angularSeparation >= 0) ? "east" : "west";

        // Provide clear instructions
        String instructions = "Adjust the telescope's azimuth by " + Math.abs(azimuthAdjustment) + " degrees to the " + azimuthDirection + ".\n" +
                "Change the telescope's altitude by " + altitudeAdjustment + " degrees to center the object.";

        return instructions;
    }
}

