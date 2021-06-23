package com.example.android.sunshine.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.android.sunshine.utilities.AppExecutors;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;

public class WeatherDataFetcher {
    WeatherDataChangeListener listener;
    Context context;

    public WeatherDataFetcher(WeatherDataChangeListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshWeatherData() {
        Executor networkExecutor = AppExecutors.getInstance().getNetworkIOExecutor();
        networkExecutor.execute(
                () -> {
                    WeatherData[] weatherData = fetchWeatherData(context);
                    listener.onWeatherDataReady(weatherData);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private WeatherData[] fetchWeatherData(Context context) {
        String preferredLocationString =
                SunshinePreferences.getPreferredWeatherLocation(context);
        URL urlToFetch = NetworkUtils.buildUrl(preferredLocationString);
        WeatherData[] weatherResults = null;
        try {
            String fetchResult = NetworkUtils.getResponseFromHttpUrl(urlToFetch);
            weatherResults =
                    OpenWeatherJsonUtils.getWeatherDataFromJson(context, fetchResult);
        } catch (IOException e) {
            Log.d("WeatherDataFetcher", "Exception while fetching data from URL: " + urlToFetch, e);
        } catch (JSONException je) {
            Log.d("WeatherDataFetcher", "Exception while parsing JSON from URL: " + urlToFetch, je);
        }

        return weatherResults;
    }
}
