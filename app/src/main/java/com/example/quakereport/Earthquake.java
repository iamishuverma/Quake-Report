package com.example.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mtime;
    private int color;

    private void setColor(double mMagnitude) {
        switch ((int) Math.floor(mMagnitude)) {
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;
        }
    }

    public Earthquake(double mMagnitude, String mLocation, long mTime) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mtime = mTime;
        setColor(mMagnitude);
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getMtime() {
        return mtime;
    }

    public int getColor() {
        return color;
    }
}
