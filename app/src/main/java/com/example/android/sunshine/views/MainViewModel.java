package com.example.android.sunshine.views;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.data.WeatherDataChangeListener;
import com.example.android.sunshine.data.WeatherDataFetcher;


public class MainViewModel extends AndroidViewModel implements WeatherDataChangeListener {
    private MutableLiveData<WeatherData[]> weatherData;
    private MutableLiveData<Integer> weatherDataLoadProgress;

    public MainViewModel(Application application) {
        super(application);
        weatherDataLoadProgress = new MutableLiveData<>();
    }

    public LiveData<WeatherData[]> getWeatherData() {
        if (weatherData == null) {
            weatherData = new MutableLiveData<>();
            loadWeatherData();
        }

        return weatherData;
    }

    public LiveData<Integer> getWeatherDataLoadProgress() {
        return weatherDataLoadProgress;
    }

    public void loadWeatherData() {
        WeatherDataFetcher fetcher = new WeatherDataFetcher(this, getApplication());
        weatherDataLoadProgress.setValue(0);
        fetcher.refreshWeatherData();
    }

    @Override
    public void onWeatherDataReady(WeatherData[] fetchedData) {
        weatherDataLoadProgress.postValue(100);
        weatherData.postValue(fetchedData);
    }
}