package com.latidude99.maptools.Utils;

public class UnitConverter {

    public static double mmToCM(long mm){
        return mm / 10D;
    }

    public static double mmToIN(long mm){
        return mm / 25.4D;
    }

    public static double mmToFT(long mm){
        return mm * 0.00328084D; // or mm/304.8
    }

    public static double mmToMetre(long mm){
        return mm / 1000D;
    }

    public static double mmToKM(long mm){
        return mm / 1_000_000D;
    }

    public static double mmToMile(long mm){
        return mm / 1_609_000D;
    }

    public static double cmToIN(double cm){
        return cm / 2.54D;
    }

    public static double cmToFT(double cm){
        return cm * 0.0328084; // or cm/30.48
    }

    public static double cmToMetre(double cm){
        return cm / 100D;
    }

    public static double cmToKM(double cm){
        return cm / 1_000_00D;
    }

    public static double cmToMile(double cm){
        return cm / 1_609_00D;
    }

    public static double cmToNauticalMile(double cm){
        return cm * 0.00000539957; // or in/185200000
    }

    public static double inToMM(double in){
        return in * 25.4D;
    }

    private  static double inToCm(double in){
        return in * 2.54D;
    }

    public static double inToMetre(double in){
        return in * 0.0254D;
    }

    private  static double inToKM(double in){
        return in * 0.0000254D;
    }

    public static double inToFT(double in){
        return in * 0.083333333333D;
    }

    public   static double inToMile(double in){
        return in / 63_360D;
    }

    public static double inToNautical(double in){
        return in * 0.0000137149; // or in/72913
    }

    public static double ftToMetres(double ft){
        return ft * 0.3048D;
    }

    public static double kmToMiles(double km){
        return km / 1.609D;
    }

    public static double kmToNautical(double km){
        return km / 1.852D;
    }

    public static double kmToFT(double km){
        return km * 3280.84;
    }

    public static double milesToKm(double miles){
        return miles * 1.609D;
    }

    public static double nauticalToKm(double nautical){
        return nautical * 1.852D;
    }

    public static double milesToNautical(double miles){
        return miles * 0.868976D;
    }

    public static double nauticalToMiles(double nautical){
        return nautical * 1.150779127681067D;
    }

}













