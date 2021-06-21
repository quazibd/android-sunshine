package com.example.android.sunshine.views;

import com.example.android.sunshine.data.WeatherData;

public interface ForecastItemClickListener {
    void onForecastItemClicked(Integer position, WeatherData forecastItemData);
}
