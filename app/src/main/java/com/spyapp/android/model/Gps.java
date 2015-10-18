package com.spyapp.android.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import static com.spyapp.android.provider.SpyContracts.Gps.Columns.LATITUDE;
import static com.spyapp.android.provider.SpyContracts.Gps.Columns.LONGITUDE;
import static com.spyapp.android.provider.SpyContracts.Sms.Columns.DATE;

public class Gps implements Parcelable {

    private long id;
    private double latitude;
    private double longitude;
    private long date;

    public Gps() {}

    public Gps(long id, double latitude, double longitude, long date) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public Gps(Parcel in) {
        id = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        date = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ContentValues prepareContentValues() {
        ContentValues values = new ContentValues();
        values.put(LATITUDE, latitude);
        values.put(LONGITUDE, longitude);
        values.put(DATE, date);
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeLong(date);
    }

    public static final Parcelable.Creator<Gps> CREATOR = new Parcelable.Creator<Gps>() {

        public Gps createFromParcel(Parcel in) {
            return new Gps(in);
        }

        public Gps[] newArray(int size) {
            return new Gps[size];
        }

    };

}