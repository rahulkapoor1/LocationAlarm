package com.rahul.locationalarm.dashboard.alarms;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmModel implements Parcelable {

    private int id;

    private String name;

    private String ringtone;

    private boolean shouldVibrate;

    private boolean active;

    private double latitude;

    private double longitude;

    public AlarmModel(String name, String ringtone, boolean shouldVibrate, boolean active, double latitude, double longitude) {
        this.name = name;
        this.ringtone = ringtone;
        this.shouldVibrate = shouldVibrate;
        this.active = active;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AlarmModel(int id, String name, String ringtone, boolean shouldVibrate, boolean active, double latitude, double longitude) {
        this(name, ringtone, shouldVibrate, active, latitude, longitude);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRingtone() {
        return ringtone;
    }

    public boolean isShouldVibrate() {
        return shouldVibrate;
    }

    public boolean isActive() {
        return active;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    protected AlarmModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ringtone = in.readString();
        shouldVibrate = in.readByte() != 0x00;
        active = in.readByte() != 0x00;
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(ringtone);
        dest.writeByte((byte) (shouldVibrate ? 0x01 : 0x00));
        dest.writeByte((byte) (active ? 0x01 : 0x00));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AlarmModel> CREATOR = new Parcelable.Creator<AlarmModel>() {
        @Override
        public AlarmModel createFromParcel(Parcel in) {
            return new AlarmModel(in);
        }

        @Override
        public AlarmModel[] newArray(int size) {
            return new AlarmModel[size];
        }
    };
}