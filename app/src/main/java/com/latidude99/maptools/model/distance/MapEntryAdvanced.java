package com.latidude99.maptools.model.distance;

import java.text.NumberFormat;
import java.util.Locale;

/*
This class represents one instance of map reading process.
- distance of the map
- distance on the ground
- map scale
All three numbers are strictly correlated, having any two of them
the remaining one can be calculated
 */
public final class MapEntryAdvanced {
    private final String ERROR_GROUND_CALCULATION = "Neither distance on the map nor map scale may equal 0";
    private final String ERROR_MAP_CALCULATION = "Neither distance on the ground nor map scale may equal 0";
    private final String ERROR_SCALE_CALCULATION = "Neither distance on the map nor ground may equal 0";

    Locale locale = Locale.getDefault();

    private double mapDistanceMM;
    private double mapDistanceCM;
    private double mapDistanceIN;

    private double groundDistanceFT;
    private double groundDistanceMetre;
    private double groundDistanceKM;
    private double groundDistanceMile;
    private double groundDistanceNautical;

    private long mapScaleFractional;
    private double mapScaleIntomile;
    private double mapScaleMiletoin;
    private double mapScaleCmtokm;
    private double mapScaleKmtocm;

    private MapEntryAdvanced(){}

    public MapEntryAdvanced(double mapDistanceMM, long mapScaleFractional){
        if(mapDistanceMM != 0 && mapScaleFractional != 0){
            this.groundDistanceKM = calculateGroundDistance(mapDistanceMM, mapScaleFractional);
            convertAndSetAllScaleFromFractional(mapScaleFractional);
            convertAndSetAllMapDistanceFromMM(mapDistanceMM);
            convertAndSetAllGroundDistanceFromKM(groundDistanceKM);
        }else{
            throw new NullPointerException(ERROR_GROUND_CALCULATION);
        }
    }

    public MapEntryAdvanced(long mapScaleFractional, double groundDistanceKM){
        if(mapScaleFractional != 0 && groundDistanceKM != 0){
            this.mapDistanceMM = calculateMapDistance(mapScaleFractional, groundDistanceKM);
            convertAndSetAllScaleFromFractional(mapScaleFractional);
            convertAndSetAllMapDistanceFromMM(mapDistanceMM);
            convertAndSetAllGroundDistanceFromKM(groundDistanceKM);
        }else{
            throw new NullPointerException(ERROR_MAP_CALCULATION);
        }
    }

    public MapEntryAdvanced(double mapDistanceMM, double groundDistanceKM){
        if(mapDistanceMM != 0 && groundDistanceKM != 0){
           this.mapScaleFractional = calculateMapScale(mapDistanceMM, groundDistanceKM);
            convertAndSetAllScaleFromFractional(mapScaleFractional);
            convertAndSetAllMapDistanceFromMM(mapDistanceMM);
            convertAndSetAllGroundDistanceFromKM(groundDistanceKM);
        }else{
            throw new NullPointerException(ERROR_SCALE_CALCULATION);
        }
    }

    public double getMapDistanceMM() {
        return mapDistanceMM;
    }
    public double getMapDistanceCM() {
        return mapDistanceCM;
    }
    public double getMapDistanceIN() {
        return mapDistanceIN;
    }
    public double getGroundDistanceFT() {
        return groundDistanceFT;
    }
    public double getGroundDistanceMetre() {
        return groundDistanceMetre;
    }
    public double getGroundDistanceKM() {
        return groundDistanceKM;
    }
    public double getGroundDistanceMile() {
        return groundDistanceMile;
    }
    public double getGroundDistanceNautical() {
        return groundDistanceNautical;
    }
    public String getMapScaleFractionalFormattedString() {
        return "1: " + NumberFormat.getNumberInstance(locale).format(mapScaleFractional);
    }
    public long getMapScaleFractional() {
        return mapScaleFractional;
    }
    public double getMapScaleIntomile() {
        return mapScaleIntomile;
    }
    public double getMapScaleMiletoin() {
        return mapScaleMiletoin;
    }
    public double getMapScaleCmtokm() {
        return mapScaleCmtokm;
    }
    public double getMapScaleKmtocm() {
        return mapScaleKmtocm;
    }


    private double calculateGroundDistance(double mapDistance, long mapScale){
        double ground = (mapDistance * mapScale) / 1_000_000D; // result in km
        return ground;
    }

    private double calculateMapDistance(long mapScale, double groundDistance){
        double map = (groundDistance * 1_000_000D) / mapScale; // result in mm
        return map;
    }

    private long calculateMapScale(double mapDistance, double groundDistance){
        long scale = (long) ((groundDistance * 1_000_000D) / mapDistance); // result fractional scale denominator
        return scale;
    }

    private void convertAndSetAllMapDistanceFromMM(double mm){
        this.mapDistanceMM = mm;
        this.mapDistanceCM = mm / 10D;
        this.mapDistanceIN =  mm / 25.4D;
    };
    private void convertAndSetAllGroundDistanceFromKM(double km){
        this.groundDistanceFT = km * 3280.84D;
        this.groundDistanceMetre = km * 1000D;
        this.groundDistanceKM = km;
        this.groundDistanceMile = km / 1.609D;
        this.groundDistanceNautical = km / 1.852D;
    };

    private void convertAndSetAllScaleFromFractional(long fra){
        this.mapScaleFractional = fra;
        this.mapScaleIntomile = 63360D / fra;
        this.mapScaleMiletoin = fra / 63360D;
        this.mapScaleCmtokm = 100000D / fra;
        this.mapScaleKmtocm = fra / 100000D;
    }





    @Override
    public String toString() {
        return "MapEntry{" +
                "mapDistance=" + mapDistanceMM +
                ", groundDistance=" + groundDistanceKM +
                ", mapScale=" + mapScaleFractional +
                '}';
    }
}




















