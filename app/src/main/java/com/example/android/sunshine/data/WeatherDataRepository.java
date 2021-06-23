package com.example.android.sunshine.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.sunshine.utilities.AppExecutors;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;

public class WeatherDataRepository {
    private MutableLiveData<WeatherData[]> liveWeatherData = new MutableLiveData<>();
    private Context context;

    public WeatherDataRepository(Context context) {
        this.context = context;
    }

    public LiveData<WeatherData[]> getWeatherData() {
        return liveWeatherData;
    }

    public void refreshWeatherData(Context context) {
        Executor networkExecutor = AppExecutors.getInstance().getNetworkIOExecutor();
        networkExecutor.execute(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        liveWeatherData.postValue(fetchWeatherData(context));
                    }
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
            // publishProgress(0);
            String fetchResult = NetworkUtils.getResponseFromHttpUrl(urlToFetch);
            weatherResults =
                    OpenWeatherJsonUtils.getWeatherDataFromJson(context, fetchResult);
            // publishProgress(100);
        } catch (IOException e) {
            Log.d("FetchWeatherTask", "Exception while fetching data from URL: " + urlToFetch, e);
        } catch (JSONException je) {
            Log.d("FetchWeatherTask", "Exception while parsing JSON from URL: " + urlToFetch, je);
        }

        return weatherResults;
    }
}
