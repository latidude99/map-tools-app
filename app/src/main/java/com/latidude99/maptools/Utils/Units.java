package com.latidude99.maptools.Utils;

public enum Units {
    DISTANCE_MM("millimetre(s)"),
    DISTANCE_CM("centimetre(s)"),
    DISTANCE_METRE("metre(s)"),
    DISTANCE_KM("km"),

    DISTANCE_IN("inch(es)"),
    DISTANCE_FT("ft"),
    DISTANCE_YARD("yard(s)"),
    DISTANCE_MILE("mile(s)"),
    DISTANCE_NM("nautical mile(s)"),

    SCALE_FRACTIONAL("fractional"),
    SCALE_INTOMILE("in to a mile"),
    SCALE_MILETOIN("ile to an in"),
    SCALE_CMTOKM("cm to a km"),
    SCALE_KMTOCM("km to a cm");


    private final String description;

    private Units(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
/*
private static final String KM = "km";
    private static final String MILE = "mile";
    private final String SCALE_FRACTIONAL = "fractional";
    private final String SCALE_INTOMILE = "in to the mile";
    private final String SCALE_MILETOIN = "mile to an in    ";
    private final String SCALE_CMTOKM = "cm to a km";
    private final String SCALE_KMTOCM = "km to a cm";
    private final String DISTANCE_MM = "millimetre(s)";
    private final String DISTANCE_CM = "centimetre(s)";
    private final String DISTANCE_IN = "inch(es)";
    private final String DISTANCE_FT = "ft";
    private final String DISTANCE_METRE = "metre(s)";
    private final String DISTANCE_KM = "km";
    private final String DISTANCE_MILE = "mile(s)";
    private final String DISTANCE_NAUTICAL = "nautical mile(s)";
 */