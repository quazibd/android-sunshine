package com.example.android.sunshine.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherData implements Parcelable {
    public static final Parcelable.Creator<WeatherData> CREATOR =
            new Parcelable.Creator<WeatherData>() {
                public WeatherData createFromParcel(Parcel in) {
                    return new WeatherData(in);
                }

                public WeatherData[] newArray(int size) {
                    return new WeatherData[size];
                }
            };
    private static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("EEEE, MMM d",
            Locale.US);
    private Date day;
    private double highTemp;
    private double lowTemp;
    private int weatherCode;
    private String weatherLabel;
    private double windSpeed;
    private double pressure;
    private int humidityPercentage;

    public WeatherData() {
    }

    public WeatherData(Parcel in) {
        this.day = new Date(in.readLong());
        this.highTemp = in.readDouble();
        this.lowTemp = in.readDouble();
        this.weatherCode = in.readInt();
        this.weatherLabel = in.readString();
        this.windSpeed = in.readDouble();
        this.pressure = in.readDouble();
        this.humidityPercentage = in.readInt();
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDayAsShortString() {
        return SHORT_DATE_FORMAT.format(getDay());
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getWeatherLabel() {
        return weatherLabel;
    }

    public void setWeatherLabel(String weatherLabel) {
        this.weatherLabel = weatherLabel;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidityPercentage() {
        return humidityPercentage;
    }

    public void setHumidityPercentage(int humidityPercentage) {
        this.humidityPercentage = humidityPercentage;
    }

    @NonNull
    @Override
    public String toString() {
        return "Date: " + SHORT_DATE_FORMAT.format(getDay()) + "\n" +
                "High: " + getHighTemp() + "\n" +
                "Low: " + getLowTemp() + "\n" +
                "Label: " + getWeatherLabel() + "\n" +
                "WeatherCode: " + getWeatherCode() + "\n" +
                "WindSpeed: " + getWindSpeed() + "\n" +
                "Pressure: " + getPressure() + "\n" +
                "HumidityPercentage: " + getHumidityPercentage();
    }

    public String toShortString(Context context) {
        String[] tokens = {
                getDayAsShortString(),
                this.weatherLabel,
                SunshineWeatherUtils.formatHighLows(context,
                        this.highTemp, this.lowTemp)
        };
        return TextUtils.join(" - ", tokens);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getDay().getTime());
        parcel.writeDouble(getHighTemp());
        parcel.writeDouble(getLowTemp());
        parcel.writeInt(getWeatherCode());
        parcel.writeString(getWeatherLabel());
        parcel.writeDouble(getWindSpeed());
        parcel.writeDouble(getPressure());
        parcel.writeInt(getHumidityPercentage());
    }
}
