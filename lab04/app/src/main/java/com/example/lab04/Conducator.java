package com.example.lab04;

import android.os.Parcel;
import android.os.Parcelable;

public class Conducator implements Parcelable {
    private String nume;
    private boolean arePermis;
    private int aniExperienta;
    private LicenseType tipPermis;
    private float varsta;

    public enum LicenseType {
        A, B, C, D, AM, BE
    }

    public Conducator(String nume, boolean arePermis, int aniExperienta,
                      LicenseType tipPermis, float varsta) {
        this.nume = nume;
        this.arePermis = arePermis;
        this.aniExperienta = aniExperienta;
        this.tipPermis = tipPermis;
        this.varsta = varsta;
    }

    protected Conducator(Parcel in) {
        nume = in.readString();
        arePermis = in.readByte() != 0;
        aniExperienta = in.readInt();
        tipPermis = LicenseType.valueOf(in.readString());
        varsta = in.readFloat();
    }

    public String getNume() { return nume; }
    public boolean isArePermis() { return arePermis; }
    public int getAniExperienta() { return aniExperienta; }
    public LicenseType getTipPermis() { return tipPermis; }
    public float getVarsta() { return varsta; }

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
    }
}