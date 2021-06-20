package com.example.android.sunshine.data;

public interface ForecastDataChangeListener {
    void onForecastDataChanged(String[] forecastData);
    void onForecastDataProgress(Integer progress);
}
