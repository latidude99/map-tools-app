package com.latidude99.maptools.model.unit;

import com.latidude99.maptools.model.scale.InsufficientParameterException;

import java.util.Locale;

public class LengthEntry {
    Locale locale = Locale.getDefault();
    private double um;
    private double mm;
    private double cm;
    private double ft;
    private double yd;
    private double m;
    private double ftm;
    private double km;
    private double mi;
    private double nm;

    private LengthEntry(LengthBuilder builder){
        this.um = builder.um;
        this.mm = builder.mm;
        this.cm = builder.cm;
        this.ft = builder.ft;
        this.yd = builder.yd;
        this.m = builder.m;
        this.ftm = builder.ftm;
        this.km = builder.km;
        this.mi = builder.mi;
        this.nm = builder.nm;
    }

    public double getUm() {
        return um;
    }
    public double getMm() {
        return mm;
    }
    public double getCm() {
        return cm;
    }
    public double getFt() {
        return ft;
    }
    public double getYd() {
        return yd;
    }
    public double getM() {
        return m;
    }
    public double getFtm() {
        return ftm;
    }
    public double getKm() {
        return km;
    }
    public double getMi() {
        return mi;
    }
    public double getNm() {
        return nm;
    }

    public static class LengthBuilder{
        private double um;
        private double mm;
        private double cm;
        private double ft;
        private double yd;
        private double m;
        private double ftm;
        private double km;
        private double mi;
        private double nm;

        public LengthBuilder(){
        }

        public LengthBuilder setMicrometresAndConvertAll(double um){
            this.um = um;
            convertAllFromUM(um);
            return this;
        }

        public LengthBuilder setMillimetresAndConvertAll(double mm){
            this.mm = mm;
            convertAllFromMM(mm);
            return this;
        }

        public LengthBuilder setCentimetresAndConvertAll(double cm){
            this.cm = cm;
            convertAllFromCM(cm);
            return this;
        }

        public LengthBuilder setFeetAndConvertAll(double ft){
            this.ft = ft;
            convertAllFromFT(ft);
            return this;
        }

        public LengthBuilder setYardsAndConvertAll(double yd){
            this.yd = yd;
            convertAllFromYD(yd);
            return this;
        }

        public LengthBuilder setMetresAndConvertAll(double m){
            this.m = m;
            convertAllFromM(m);
            return this;
        }

        public LengthBuilder setFathomsAndConvertAll(double ftm){
            this.ftm = ftm;
            convertAllFromFTM(ftm);
            return this;
        }

        public LengthBuilder setKilometresAndConvertAll(double km){
            this.km = km;
            convertAllFromKM(km);
            return this;
        }

        public LengthBuilder setMilesAndConvertAll(double mi){
            this.mi = mi;
            convertAllFromMI(mi);
            return this;
        }

        public LengthBuilder setNauticalMilesAndConvertAll(double nm){
            this.nm = nm;
            convertAllFromNM(nm);
            return this;
        }

        private boolean convertAllFromUM(double input){
            this.um = input;
            this.mm = input *10D;
            this.cm = input * 100D;
            this.ft = input;
            this.yd = input;
            this.m = input * 10_000D;
            this.ftm = input;
            this.km = input * 10_000_000;
            this.mi = input;
            this.nm = input;
        }
    }

            this.um = input;
            this.mm = input;
            this.cm = input;
            this.ft = input;
            this.yd = input;
            this.m = input;
            this.ftm = input;
            this.km = input;
            this.mi = input;
            this.nm = input;

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

}
