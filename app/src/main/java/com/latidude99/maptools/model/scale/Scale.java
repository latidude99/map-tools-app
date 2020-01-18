package com.latidude99.maptools.model.scale;

import java.text.NumberFormat;
import java.util.Locale;

public class Scale {
    Locale locale = Locale.getDefault();
    private int fractional;
    private double inToMile;
    private double mileToIn;
    private double cmToKm;
    private double kmToCm;

    private Scale(ScaleBuilder builder){
        this.fractional = builder.fractional;
        this.inToMile = builder.inToMile;
        this.mileToIn = builder.mileToIn;
        this.cmToKm = builder.cmToKm;
        this.kmToCm = builder.kmToCm;
    }

    public int getFractional() {
        return fractional;
    }

    public double getInToMile() {
        return inToMile;
    }

    public double getMileToIn() {
        return mileToIn;
    }

    public double getCmToKm() {
        return cmToKm;
    }

    public double getKmToCm() {
        return kmToCm;
    }


    public static class ScaleBuilder{
        private int fractional;
        private double inToMile;
        private double mileToIn;
        private double cmToKm;
        private double kmToCm;

        public ScaleBuilder(){
        }

        public ScaleBuilder setFractionalAndConvert(int fractional){
            this.fractional = fractional;
            convertAllFromFractional(fractional);
            return this;
        }

        public ScaleBuilder setInToMileAndConvert(double inToMile) {
            this.inToMile = inToMile;
            convertAllFromInchtomile(inToMile);
            return this;
        }

        public ScaleBuilder setMileToInAndConvert(double mileToIn){
            this.mileToIn = mileToIn;
            convertAllFromMiletoinch(mileToIn);
            return this;
        }

        public ScaleBuilder setCmToKmAndConvert(double cmToKm){
            this.cmToKm = cmToKm;
            convertAllFromCmtokm(cmToKm);
            return this;
        }

        public ScaleBuilder setKmToCmAndConvert(double kmToCm){
            this.kmToCm = kmToCm;
            convertAllFromKmtocm(kmToCm);
            return this;
        }

        private void convertAllFromFractional(int fra){
            this.fractional = fra;
            this.inToMile = 63360D / fra;
            this.mileToIn = fra / 63360D;
            this.cmToKm = 100000D / fra;
            this.kmToCm = fra / 100000D;
            System.out.println("Builder.inToMile: " + this.inToMile);
        }

        private void convertAllFromInchtomile(double inches){
            this.fractional = (int) (63360 / inches);
            this.inToMile = inches;
            this.mileToIn = 1 / inches;
            this.cmToKm =  100000 / (63360 / inches);
            this.kmToCm = (63360 / inches) / 100000;
        }

        private void convertAllFromMiletoinch(double miles){
            this.fractional = (int) (miles * 63360);
            this.inToMile = 1 / miles;
            this.mileToIn = miles;
            this.cmToKm =  100000 / (miles * 63360);
            this.kmToCm = (miles * 63360) / 100000;
        }

        private void convertAllFromCmtokm(double cm){
            this.fractional = (int) (100000 / cm);
            this.inToMile = 63360 / (100000 / cm);
            this.mileToIn = (100000 / cm) / 63360;
            this.cmToKm = cm;
            this.kmToCm = 1 /cm;
        }

        private void convertAllFromKmtocm(double km){
            this.fractional = (int) (100000 * km);
            this.inToMile = 63360 / (100000 * km);
            this.mileToIn = (100000 * km) / 63360;
            this.cmToKm = 1 /km ;
            this.kmToCm = km;
        }

        public Scale build() throws InsufficientParameterException {
            System.out.println(this);
            Scale scale = new Scale(this);
            if(scale.fractional == 0
                    || scale.inToMile == 0
                    || scale.mileToIn == 0
                    || scale.cmToKm == 0
                    || scale.kmToCm == 0) {
                String message = "There has to be at least one parawmeter set in the builder";
                throw new InsufficientParameterException(message);
            }
            return scale;
        }

        @Override
        public String toString() {
            return "ScaleBuilder{" +
                    "fractional=" + fractional +
                    ", inToMile=" + inToMile +
                    ", mileToIn=" + mileToIn +
                    ", cmToKm=" + cmToKm +
                    ", kmToCm=" + kmToCm +
                    '}';
        }
    }

    // additional methods of the Scale class

    public String getFractionalFormattedString() {
        return "1: " + NumberFormat.getNumberInstance(locale).format(fractional);
    }

    public String getInToMileFormattedString() {
        if(inToMile < 0.1)
            return "less than 0.1 inch to the mile";
        else
            return String.format(locale, "%.2f", inToMile) + " inch(es) to the mile";
    }

    public String getMileToInFormattedString() {
        if(mileToIn < 0.001)
            return "less than 0.001 mile to an inch";
        else
            return String.format(locale, "%.1f", mileToIn) + " mile(s) to an inch";
    }

    public String getCmToKmFormattedString() {
        if(cmToKm < 0.1)
            return "less than 0.1 cm to a kilometre";
        else
            return String.format(locale,"%.1f", cmToKm) +  " centimetre(s) to a kilometre";
    }

    public String getKmToCmFormattedString() {
        if(kmToCm >= 0.1)
            return String.format(locale, "%.1f", kmToCm) + " km to a centimetre";
        else
            return String.format(locale, "%.1f", kmToCm*1000) + " metre(s) to a centimetre";
    }



}
