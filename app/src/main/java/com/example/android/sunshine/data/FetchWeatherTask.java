package com.example.android.sunshine.data;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.android.sunshine.views.ForecastDataHandler;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class FetchWeatherTask extends AsyncTask<Void, Integer, WeatherData[]> {
    WeakReference<Context> context;
    ForecastDataHandler forecastDataHandler;

    public FetchWeatherTask(Context context, ForecastDataHandler forecastDataHandler) {
        this.context = new WeakReference<>(context);
        this.forecastDataHandler = forecastDataHandler;
    }

    @Override
    protected void onPostExecute(WeatherData[] weatherData) {
        forecastDataHandler.onForecastDataChanged(weatherData);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected WeatherData[] doInBackground(Void... voids) {
        String preferredLocationString =
                SunshinePreferences.getPreferredWeatherLocation(context.get());
        URL urlToFetch = NetworkUtils.buildUrl(preferredLocationString);
        WeatherData[] weatherResults = null;
        try {
            publishProgress(0);
            String fetchResult = NetworkUtils.getResponseFromHttpUrl(urlToFetch);
            weatherResults =
                    OpenWeatherJsonUtils.getWeatherDataFromJson(context.get(), fetchResult);
            publishProgress(100);
        } catch (IOException e) {
            Log.d("FetchWeatherTask", "Exception while fetching data from URL: " + urlToFetch, e);
        } catch (JSONException je) {
            Log.d("FetchWeatherTask", "Exception while parsing JSON from URL: " + urlToFetch, je);
        }
        return weatherResults;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        forecastDataHandler.onForecastDataProgress(progress[0]);
        super.onProgressUpdate(progress);
    }
}
