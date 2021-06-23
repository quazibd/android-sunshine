package com.example.android.sunshine.data;

public interface WeatherDataChangeListener {
    void onWeatherDataReady(WeatherData[] weatherData);
}
