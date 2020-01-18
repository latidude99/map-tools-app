package com.latidude99.maptools.model.distance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.latidude99.maptools.R;

import java.util.Map;

/*
This class represents one instance of map reading process.
- distance of the map
- distance on the ground
- map scale
All three numbers are strictly correlated, having any two of them
the remaining one can be calculated
 */
public final class MapEntry {
    private final String ERROR_GROUND_CALCULATION = "Neither distance on the map nor map scale may equal 0";
    private final String ERROR_MAP_CALCULATION = "Neither distance on the ground nor map scale may equal 0";
    private final String ERROR_SCALE_CALCULATION = "Neither distance on the map nor ground may equal 0";

    private double mapDistanceMM;
    private double mapDistanceCM;
    private double mapDistanceIN;

    private double groundDistanceFT;
    private double groundDistanceMetre;
    private double groundDistanceKM;
    private double groundDistanceMile;
    private double groundDistanceNautical;

    private int mapScaleFractional;

    private MapEntry(){}

    public MapEntry(double mapDistanceMM, int mapScaleFractional){
        if(mapDistanceMM != 0 && mapScaleFractional != 0){
            this.mapDistanceMM = mapDistanceMM;
            this.mapScaleFractional = mapScaleFractional;
            this.groundDistanceKM = calculateGroundDistance(mapDistanceMM, mapScaleFractional);
        }else{
            throw new NullPointerException(ERROR_GROUND_CALCULATION);
        }

    }

    public MapEntry(int mapScaleFractional, double groundDistanceKM){
        if(mapScaleFractional != 0 && groundDistanceKM != 0){
            this.groundDistanceKM = groundDistanceKM;
            this.mapScaleFractional = mapScaleFractional;
            this.mapDistanceMM = calculateMapDistance(mapScaleFractional, groundDistanceKM);
        }else{
            throw new NullPointerException(ERROR_MAP_CALCULATION);
        }
    }

    public MapEntry(double mapDistanceMM, double groundDistanceKM){
        if(mapDistanceMM != 0 && groundDistanceKM != 0){
            this.groundDistanceKM = groundDistanceKM;
            this.mapDistanceMM = mapDistanceMM;
            this.mapScaleFractional = calculateMapScale(mapDistanceMM, groundDistanceKM);
        }else{
            throw new NullPointerException(ERROR_SCALE_CALCULATION);
        }
    }

    public double getMapDistanceMM() {
        return mapDistanceMM;
    }

    public double getGroundDistanceKM() {
        return groundDistanceKM;
    }

    public int getMapScaleFractional() {
        return mapScaleFractional;
    }

    private double calculateGroundDistance(double mapDistance, int mapScale){
        double ground = (mapDistance * mapScale) / 1_000_000D; // result in km
        return ground;
    }

    private double calculateMapDistance(int mapScale, double groundDistance){
        double map = (groundDistance * 1_000_000D) / mapScale; // result in mm
        return map;
    }

    private int calculateMapScale(double mapDistance, double groundDistance){
        int scale = (int) ((groundDistance * 1_000_000D) / mapDistance); // result fractional scale denominator
        return scale;
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




















