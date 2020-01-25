package com.latidude99.maptools.model.unit;

import com.latidude99.maptools.model.scale.InsufficientParameterException;

import java.util.Locale;

public class AreaEntry {
    Locale locale = Locale.getDefault();
    private double um2; // micrometre
    private double mm2; // millimetre
    private double cm2; // centimetre
    private double in2; // inch
    private double ft2; // foot
    private double yd2; // yard
    private double m2; // metre
    private double are;
    private double acre;
    private double ha; // hectare
    private double km2; // kilometre
    private double mi2; // statue mile, UK

    private AreaEntry(Builder builder){
        this.um2 = builder.um2;
        this.mm2 = builder.mm2;
        this.cm2 = builder.cm2;
        this.in2 = builder.in2;
        this.ft2 = builder.ft2;
        this.yd2 = builder.yd2;
        this.m2 = builder.m2;
        this.are = builder.are;
        this.acre = builder.acre;
        this.ha = builder.ha;
        this.km2 = builder.km2;
        this.mi2 = builder.mi2;
    }

    public double getUm2() {
        return um2;
    }

    public double getMm2() {
        return mm2;
    }

    public double getCm2() {
        return cm2;
    }

    public double getIn2() {
        return in2;
    }

    public double getFt2() {
        return ft2;
    }

    public double getYd2() {
        return yd2;
    }

    public double getM2() {
        return m2;
    }

    public double getAre() {
        return are;
    }

    public double getAcre() {
        return acre;
    }

    public double getHa() {
        return ha;
    }

    public double getKm2() {
        return km2;
    }

    public double getMi2() {
        return mi2;
    }

    public static class Builder {
        private double um2; // micrometre
        private double mm2; // millimetre
        private double cm2; // centimetre
        private double in2; // inch
        private double ft2; // foot
        private double yd2; // yard
        private double m2; // metre
        private double are;
        private double acre;
        private double ha; // hectare
        private double km2; // kilometre
        private double mi2; // statue mile, UK

        public Builder() {
        }

        public Builder setMicrometres2AndConvertAll(double um2) {
            this.um2 = um2;
            convertAllFromUM2(um2);
            return this;
        }

        public Builder setMillimetres2AndConvertAll(double mm2) {
            this.mm2 = mm2;
            convertAllFromMM2(mm2);
            return this;
        }

        public Builder setCentimetres2AndConvertAll(double cm2) {
            this.cm2 = cm2;
            convertAllFromCM2(cm2);
            return this;
        }

        public Builder setInches2AndConvertAll(double in2) {
            this.in2 = in2;
            convertAllFromIN2(in2);
            return this;
        }

        public Builder setFeet2AndConvertAll(double ft2) {
            this.ft2 = ft2;
            convertAllFromFT2(ft2);
            return this;
        }

        public Builder setYards2AndConvertAll(double yd2) {
            this.yd2 = yd2;
            convertAllFromYD2(yd2);
            return this;
        }

        public Builder setMetres2AndConvertAll(double m2) {
            this.m2 = m2;
            convertAllFromM2(m2);
            return this;
        }

        public Builder setAresAndConvertAll(double are) {
            this.are = are;
            convertAllFromARES(are);
            return this;
        }

        public Builder setAcresAndConvertAll(double acre) {
            this.acre = acre;
            convertAllFromACRES(acre);
            return this;
        }

        public Builder setHectaresAndConvertAll(double ha) {
            this.ha = ha;
            convertAllFromHA(ha);
            return this;
        }

        public Builder setKilometres2AndConvertAll(double km2) {
            this.km2 = km2;
            convertAllFromKM2(km2);
            return this;
        }

        public Builder setMiles2AndConvertAll(double mi2) {
            this.mi2 = mi2;
            convertAllFromMI2(mi2);
            return this;
        }



        private boolean convertAllFromUM2(double input) {
            try {
                this.um2 = input;
                this.mm2 = input / Math.pow(1_000, 2);
                this.cm2 = input / Math.pow(10_000, 2);
                this.in2 = input / Math.pow(25_400, 2);
                this.ft2 = input / Math.pow(304_800, 2);
                this.yd2 = input / Math.pow(914_400, 2);
                this.m2 = input / Math.pow(1_000_000, 2);
                this.are = input * 0.00000000000001;
                this.acre = input * 0.0000000000000002471053814;
                this.ha = input * 0.0000000000000001;
                this.km2 = input / Math.pow(1_000_000_000,2);
                this.mi2 = input / Math.pow(1_609_344_000, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }


        private boolean convertAllFromMM2(double input) {
            try {
                this.um2 = input * Math.pow(10002, 2);
                this.mm2 = input;
                this.cm2 = input / Math.pow(10, 2);
                this.in2 = input / Math.pow(25.4, 2);
                this.ft2 = input / Math.pow(304.8, 2);
                this.yd2 = input / Math.pow(914.4, 2);
                this.m2 = input / Math.pow(1_000, 2);
                this.are = input * 0.00000001;
                this.acre = input * 0.0000000002471053814;
                this.ha = input * 0.0000000001;
                this.km2 = input / Math.pow(1_000_000,2);
                this.mi2 = input / Math.pow(1_609_344, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromCM2(double input) {
            try {
                this.um2 = input * Math.pow(10_000, 2);
                this.mm2 = input * Math.pow(10, 2);
                this.cm2 = input;
                this.in2 = input / Math.pow(25.4,2);
                this.ft2 = input / Math.pow(30.48, 2);
                this.yd2 = input / Math.pow(91.44, 2);
                this.m2 = input /Math.pow(100, 2);
                this.are = input * 0.000001;
                this.acre = input * 0.000000024710538147;
                this.ha = input * 0.00000001;
                this.km2 = input / Math.pow(100_000, 2);
                this.mi2 = input / Math.pow(160_934.4, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromIN2(double input) {
            try {
                this.um2 = input * Math.pow(25_400, 2);
                this.mm2 = input * Math.pow(25.4, 2);
                this.cm2 = input * Math.pow(2.54, 2);
                this.in2 = input;
                this.ft2 = input / Math.pow(12, 2);
                this.yd2 = input / Math.pow(36, 2);
                this.m2 = input / Math.pow(39.370079, 2);
                this.are = input * 0.0000064516;
                this.acre = input * 0.00000015942250791;
                this.ha = input * 0.000000064516;
                this.km2 = input / Math.pow(39_370.078740, 2);
                this.mi2 = input / Math.pow(63_360, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromFT2(double input) {
            try {
                this.um2 = input * Math.pow(304_800, 2);
                this.mm2 = input * Math.pow(304.8, 2);
                this.cm2 = input * Math.pow(30.48, 2);
                this.in2 = input  * Math.pow(12, 2);
                this.ft2 = input;
                this.yd2 = input / Math.pow(3, 2);
                this.m2 = input / Math.pow(3.280840, 2);
                this.are = input * 0.000929030;
                this.acre = input * 0.000022956841139;
                this.ha = input * 0.000009290304;
                this.km2 = input / Math.pow(3_280.839895, 2);
                this.mi2 = input / Math.pow(5_280, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromYD2(double input) {
            try {
                this.um2 = input * Math.pow(914_400, 2);
                this.mm2 = input * Math.pow(914.4, 2);
                this.cm2 = input * Math.pow(91.44, 2);
                this.in2 = input * Math.pow(36, 2);
                this.ft2 = input * Math.pow(3, 2);
                this.yd2 = input;
                this.m2 = input / Math.pow(1.093613, 2);
                this.are = input * 0.0083612736;
                this.acre = input * 0.20661157025;
                this.ha = input * 0.000083612736;
                this.km2 = input / Math.pow(1_093.613298, 2);
                this.mi2 = input / Math.pow(1_760, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromM2(double input) {
            try {
                this.um2 = input * Math.pow(1_000_000, 2);
                this.mm2 = input * Math.pow(1_000, 2);
                this.cm2 = input * Math.pow(100, 2);
                this.in2 = input * Math.pow(39.370079, 2);
                this.ft2 = input * Math.pow(3.28084, 2);
                this.yd2 = input * Math.pow(1.093613, 2);
                this.m2 = input;
                this.are = input * 0.01;
                this.acre = input * 0.00024710538147;
                this.ha = input * 0.0001;
                this.km2 = input / Math.pow(1_000, 2);
                this.mi2 = input / Math.pow(1609.344, 2);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromARES(double input) {
            try {
                this.um2 = input * 100_000_000_000_000D;
                this.mm2 = input * 100_000_000D;
                this.cm2 = input * 1_000_000D;
                this.in2 = input * 155_000.31;
                this.ft2 = input * 1_076.3910417D;
                this.yd2 = input * 119.59900463;
                this.m2 = input * 100;
                this.are = input;
                this.acre = input * 0.024710538147D;
                this.ha = input * 0.01D;
                this.km2 = input * 0.0001;
                this.mi2 = input * 0.000038610215855D;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromACRES(double input) {
            try {
                this.um2 = input * 4_046_856_422_400_000D;
                this.mm2 = input * 4_046_856_422.4D;
                this.cm2 = input * 40_468_564.224D;
                this.in2 = input * 6_272_640D;
                this.ft2 = input * 43_650D;
                this.yd2 = input * 4_840D;
                this.m2 = input * 4_046.8564224D;
                this.are = input * 40.468564224D;
                this.acre = input;
                this.ha = input * 0.40468564224D;
                this.km2 = input * 0.0040468564224D;
                this.mi2 = input * 0.0015625D;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromHA(double input) {
            try {
                this.um2 = input * 10_000_000_000_000_000D;
                this.mm2 = input * 10_000_000_000D;
                this.cm2 = input * 100_000_000D;
                this.in2 = input * 15_500_031D;
                this.ft2 = input * 107_639.10417D;
                this.yd2 = input * 11_959.900463D;
                this.m2 = input * 10_000D;
                this.are = input * 100D;
                this.acre = input * 2.4710538147D;
                this.ha = input;
                this.km2 = input * 0.01D;
                this.mi2 = input * 0.0038610215855D;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromKM2(double input) {
            try {
                this.um2 = input * 1_000_000_000;
                this.mm2 = input * 1_000_000;
                this.cm2 = input * 100_000;
                this.in2 = input * 39_370.078740;
                this.ft2 = input * 3_280.839895;
                this.yd2 = input * 1_093.613298;
                this.m2 = input * 1_000;
                this.are = input * 10_000;
                this.acre = input * 247.10538147;
                this.ha = input * 100;
                this.km2 = input;
                this.mi2 = input / 1.609344;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromMI2(double input) {
            try {
                this.um2 = input * 1_609_344_000;
                this.mm2 = input * 1_609_344;
                this.cm2 = input * 160_934.4;
                this.in2 = input * 63_360;
                this.ft2 = input * 5_280;
                this.yd2 = input * 1_760;
                this.m2 = input * 1_609.344;
                this.are = input * 25_899.881103;
                this.acre = input * 639.99999999;
                this.ha = input * 258.99881103;
                this.km2 = input * 1.609344;
                this.mi2 = input;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }



        public AreaEntry build() throws InsufficientParameterException {
            System.out.println(this);
            AreaEntry areaEntry = new AreaEntry(this);
            if(areaEntry.um2 == 0
                    || areaEntry.mm2 == 0
                    || areaEntry.cm2 == 0
                    || areaEntry.in2 == 0
                    || areaEntry.ft2 == 0
                    || areaEntry.yd2 == 0
                    || areaEntry.m2 == 0
                    || areaEntry.are == 0
                    || areaEntry.acre == 0
                    || areaEntry.ha == 0
                    || areaEntry.km2 == 0
                    || areaEntry.mi2 == 0){
                String message = "There has to be at least one parawmeter set in the builder";
                throw new InsufficientParameterException(message);
            }
            System.out.println(areaEntry);
            return areaEntry;
        }

    }

    @Override
    public String toString() {
        return "AreaEntry{" +
                "locale=" + locale +
                ", um2=" + um2 +
                ", mm2=" + mm2 +
                ", cm2=" + cm2 +
                ", in2=" + in2 +
                ", ft2=" + ft2 +
                ", yd2=" + yd2 +
                ", m2=" + m2 +
                ", are=" + are +
                ", acre=" + acre +
                ", ha=" + ha +
                ", km2=" + km2 +
                ", mi2=" + mi2 +
                '}';
    }
}
