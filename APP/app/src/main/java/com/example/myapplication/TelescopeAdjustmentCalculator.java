package com.example.myapplication;

public class TelescopeAdjustmentCalculator {



    public static String calculateTelescopeAdjustment(String targetRa, String targetDec, double currentRa, double currentDec) {

        //convert target Ra and target dec
        //double ra_deg=sexagesimalToDegrees(targetRa);
        double dec_deg=sexagesimalToDegrees(targetDec);//dec from simbad is in sexagesimal

        // Calculate Ra and Dec adjustments in degrees
        //double raAdjustment = ra_deg - currentRa;
        double decAdjustment = dec_deg - currentDec;

        //calculate the new telescope Ra because in the telescope Ra is in sexagesimal
        //double telescope_ra=currentRa+raAdjustment;

        //convert Ra back to sexagesimal since telescope works with sexagesimal
        //String telescope_ra_sexa=degreesToSexagesimal(telescope_ra);

        // Provide clear instructions
        String instructions = "Your telescope Ra should be : " + targetRa+".\n"+
                "The telescope's Dec should be " + dec_deg + " degrees.";

        return instructions;
    }
    public static String calculateTelescopeAdjustment2(String targetRa, String targetDec, double currentRa, double currentDec){
        //convert ra/dec to alt az

        return  "Your telescope Alt should be : " + targetRa+".\n"+
                "The telescope's Azimuth should be " + targetDec + ".";

    }
    private static double sexagesimalToDegrees(String sexagesimal) {
        String[] components = sexagesimal.split(" ");

        // Extract sign and remove it from the first component
        String sign = components[0].substring(0, 1);
        components[0] = components[0].substring(1);

        double degrees = Double.parseDouble(components[0]);
        double minutes = Double.parseDouble(components[1]);
        double seconds = Double.parseDouble(components[2]);

        // Determine the sign based on the extracted prefix
        double signMultiplier = (sign.equals("+")) ? 1.0 : -1.0;

        // Calculate declination using the conversion formula
        double declination = signMultiplier * (degrees + minutes / 60.0 + seconds / 3600.0);

        return declination;
    }




    private static String degreesToSexagesimal(double degrees) {
        int hours = (int) Math.floor(degrees / 15);
        degrees -= hours * 15;
        int minutes = (int) Math.floor(degrees / 0.25);
        degrees -= minutes * 0.25;
        double seconds = degrees * 240;

        return String.format("%02d %02d %.3f", hours, minutes, seconds);
    }

    public static String calculateTelescopeAdjustment3(String targetAltitude, String targetAz) {

        return "Your telescope Altitude should be : " + decimalToSexa(targetAltitude)+".\n"+
                "The telescope's Azimuth should be " + decimalToSexa(targetAz) + ".";
    }
    //144°05'12.5"   +31°02'09.1"
    public static String decimalToSexa(String number){
        Double num=Double.parseDouble(number);
        int index=number.indexOf(".");
        String intPart=number.substring(0,index);//degrees
        String deciPart=number.substring(index);
        Double decimalPart=Double.parseDouble(deciPart)*60;
        int index1=Double.toString(decimalPart).indexOf(".");
        String minutes=Double.toString(decimalPart).substring(0,index1);//minutes
        String seconds=Double.toString(Double.parseDouble(Double.toString(decimalPart).substring(index1))*60);
        int index2=seconds.indexOf(".");
        return intPart+"°"+minutes+"'"+seconds.substring(0,index2)+"''";
    }

}

