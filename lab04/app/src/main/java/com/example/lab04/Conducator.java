package com.example.lab04;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Conducator implements Parcelable {
    private String nume;             // String
    private boolean arePermis;       // Boolean
    private int aniExperienta;       // Integer
    private LicenseType tipPermis;   // Enum
    private float varsta;            // Float
    private Date dataObtinerePermis; // Date

    public enum LicenseType {
        A, B, C, D, AM, BE
    }

    public Conducator(String nume, boolean arePermis, int aniExperienta,
                      LicenseType tipPermis, float varsta, Date dataObtinerePermis) {
        this.nume = nume;
        this.arePermis = arePermis;
        this.aniExperienta = aniExperienta;
        this.tipPermis = tipPermis;
        this.varsta = varsta;
        this.dataObtinerePermis = dataObtinerePermis;
    }

    protected Conducator(Parcel in) {
        nume = in.readString();
        arePermis = in.readByte() != 0;
        aniExperienta = in.readInt();
        tipPermis = LicenseType.valueOf(in.readString());
        varsta = in.readFloat();
        dataObtinerePermis = new Date(in.readLong());
    }

    public String getNume() { return nume; }
    public boolean isArePermis() { return arePermis; }
    public int getAniExperienta() { return aniExperienta; }
    public LicenseType getTipPermis() { return tipPermis; }
    public float getVarsta() { return varsta; }
    public Date getDataObtinerePermis() { return dataObtinerePermis; } // Corectat

    @Override
    public String toString() {
        return "Nume: " + nume + "\nAre permis: " + arePermis +
                "\n Experiență: " + aniExperienta + " ani\nTip permis: " + tipPermis +
                "\nVârsta: " + varsta + "\nData Obtinere permis: " + dataObtinerePermis+"\n";
    }

    public static final Creator<Conducator> CREATOR = new Creator<Conducator>() {
        @Override
        public Conducator createFromParcel(Parcel in) {
            return new Conducator(in);
        }

        @Override
        public Conducator[] newArray(int size) {
            return new Conducator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeByte((byte) (arePermis ? 1 : 0));
        dest.writeInt(aniExperienta);
        dest.writeString(tipPermis.name());
        dest.writeFloat(varsta);
        dest.writeLong(dataObtinerePermis.getTime());
    }
}