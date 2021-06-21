package com.example.android.sunshine.data;

public interface ForecastDataChangeListener {
    void onForecastDataChanged(WeatherData[] forecastData);
    void onForecastDataProgress(Integer progress);
}
