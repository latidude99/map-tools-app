package com.latidude99.maptools.model.unit;

import com.latidude99.maptools.model.scale.InsufficientParameterException;

import java.util.Locale;

public class LengthEntry {
    Locale locale = Locale.getDefault();
    private double um; // micrometre
    private double mm; // millimetre
    private double cm; // centimetre
    private double in; // inch
    private double ft; // foot
    private double yd; // yard
    private double m; // metre
    private double ftm; // fathom
    private double km; // kilometre
    private double mi; // statue mile, UK
    private double nm; // nautical mile, inernational

    private LengthEntry(Builder builder){
        this.um = builder.um;
        this.mm = builder.mm;
        this.cm = builder.cm;
        this.in = builder.in;
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
    public double getIn() {
        return in;
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

    public static class Builder {
        private double um;
        private double mm;
        private double cm;
        private double in;
        private double ft;
        private double yd;
        private double m;
        private double ftm;
        private double km;
        private double mi;
        private double nm;

        public Builder() {
        }

        public Builder setMicrometresAndConvertAll(double um) {
            this.um = um;
            convertAllFromUM(um);
            return this;
        }

        public Builder setMillimetresAndConvertAll(double mm) {
            this.mm = mm;
            convertAllFromMM(mm);
            return this;
        }

        public Builder setCentimetresAndConvertAll(double cm) {
            this.cm = cm;
            convertAllFromCM(cm);
            return this;
        }

        public Builder setInchesAndConvertAll(double cm) {
            this.cm = cm;
            convertAllFromCM(cm);
            return this;
        }

        public Builder setFeetAndConvertAll(double ft) {
            this.ft = ft;
            convertAllFromFT(ft);
            return this;
        }

        public Builder setYardsAndConvertAll(double yd) {
            this.yd = yd;
            convertAllFromYD(yd);
            return this;
        }

        public Builder setMetresAndConvertAll(double m) {
            this.m = m;
            convertAllFromM(m);
            return this;
        }

        public Builder setFathomsAndConvertAll(double ftm) {
            this.ftm = ftm;
            convertAllFromFTM(ftm);
            return this;
        }

        public Builder setKilometresAndConvertAll(double km) {
            this.km = km;
            convertAllFromKM(km);
            return this;
        }

        public Builder setMilesAndConvertAll(double mi) {
            this.mi = mi;
            convertAllFromMI(mi);
            return this;
        }

        public Builder setNauticalMilesAndConvertAll(double nm) {
            this.nm = nm;
            convertAllFromNM(nm);
            return this;
        }

        private boolean convertAllFromUM(double input) {
            try {
                this.um = input;
                this.mm = input / 1_000;
                this.cm = input / 10_000;
                this.in = input / 25_400;
                this.ft = input / 304_800;
                this.yd = input / 914_400;
                this.m = input / 1_000_000;
                this.ftm = input / 1_828_800;
                this.km = input / 1_000_000_000;
                this.mi = input / 1_609_344_000;
                this.nm = input / 1_852_000_000;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }


        private boolean convertAllFromMM(double input) {
            try {
                this.um = input * 1000;
                this.mm = input;
                this.cm = input / 10;
                this.in = input / 25.4;
                this.ft = input / 304.8;
                this.yd = input / 914.4;
                this.m = input / 1_000;
                this.ftm = input / 1_828.8;
                this.km = input / 1_000_000;
                this.mi = input / 1_609_344;
                this.nm = input / 1_852_000;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromCM(double input) {
            try {
                this.um = input * 10_000;
                this.mm = input * 10;
                this.cm = input;
                this.in = input / 25.4;
                this.ft = input / 30.48;
                this.yd = input / 91.44;
                this.m = input /100;
                this.ftm = input / 182.88;
                this.km = input / 100_000;
                this.mi = input / 160_934.4;
                this.nm = input / 185_200;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromIN(double input) {
            try {
                this.um = input * 25_400;
                this.mm = input * 25.4;
                this.cm = input * 2.54;
                this.in = input;
                this.ft = input / 12;
                this.yd = input / 36;
                this.m = input / 39.370079;
                this.ftm = input / 72;
                this.km = input / 39_370.078740;
                this.mi = input / 63_360;
                this.nm = input / 72_913.385827;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromFT(double input) {
            try {
                this.um = input * 304_800;
                this.mm = input * 304.8;
                this.cm = input * 30.48;
                this.in = input  * 12;
                this.ft = input;
                this.yd = input / 3;
                this.m = input / 3.280840;
                this.ftm = input / 6;
                this.km = input / 3_280.839895;
                this.mi = input / 5_280;
                this.nm = input / 6_080;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromYD(double input) {
            try {
                this.um = input * 914_400;
                this.mm = input * 914.4;
                this.cm = input * 91.44;
                this.in = input * 36;
                this.ft = input * 3;
                this.yd = input;
                this.m = input / 1.093613;
                this.ftm = input / 2;
                this.km = input / 1_093.613298;
                this.mi = input / 1_760;
                this.nm = input / 2025.371829;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromM(double input) {
            try {
                this.um = input * 1_000_000;
                this.mm = input * 1_000;
                this.cm = input * 100;
                this.in = input * 39.370079;
                this.ft = input * 3.28084;
                this.yd = input * 1.093613;
                this.m = input;
                this.ftm = input / 1.828800;
                this.km = input / 1_000;
                this.mi = input / 1609.344;
                this.nm = input / 1852;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromFTM(double input) {
            try {
                this.um = input * 1_828_800;
                this.mm = input * 1_828.8;
                this.cm = input * 182.88;
                this.in = input * 72;
                this.ft = input * 6;
                this.yd = input * 2;
                this.m = input * 1.8288;
                this.ftm = input;
                this.km = input / 546.806649;
                this.mi = input / 880;
                this.nm = input / 1012.685914;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromKM(double input) {
            try {
                this.um = input * 1_000_000_000;
                this.mm = input * 1_000_000;
                this.cm = input * 100_000;
                this.in = input * 39_370.078740;
                this.ft = input * 3_280.839895;
                this.yd = input * 1_093.613298;
                this.m = input * 1_000;
                this.ftm = input * 546.806649;
                this.km = input;
                this.mi = input / 1.609344;
                this.nm = input / 1.852;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromMI(double input) {
            try {
                this.um = input * 1_609_344_000;
                this.mm = input * 1_609_344;
                this.cm = input * 160_934.4;
                this.in = input * 63_360;
                this.ft = input * 5_280;
                this.yd = input * 1_760;
                this.m = input * 1_609.344;
                this.ftm = input * 880;
                this.km = input * 1.609344;
                this.mi = input;
                this.nm = input / 1.150779;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }

        private boolean convertAllFromNM(double input) {
            try {
                this.um = input * 1_852_000_000;
                this.mm = input * 1_852_000;
                this.cm = input * 185_200;
                this.in = input * 72_913.385827;
                this.ft = input * 6_076.115486;
                this.yd = input * 2_025.371829;
                this.m = input * 1_852;
                this.ftm = input * 1_012.685914;
                this.km = input * 1.852;
                this.mi = input * 1.150779;
                this.nm = input;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return false;
            }
        }


        public LengthEntry build() throws InsufficientParameterException {
            System.out.println(this);
            LengthEntry lengthEntry = new LengthEntry(this);
            if(lengthEntry.um == 0
                    || lengthEntry.mm == 0
                    || lengthEntry.cm == 0
                    || lengthEntry.in == 0
                    || lengthEntry.ft == 0
                    || lengthEntry.yd == 0
                    || lengthEntry.m == 0
                    || lengthEntry.ftm == 0
                    || lengthEntry.km == 0
                    || lengthEntry.mi == 0
                    || lengthEntry.nm == 0) {
                String message = "There has to be at least one parawmeter set in the builder";
                throw new InsufficientParameterException(message);
            }
            return lengthEntry;
        }

    }



}
