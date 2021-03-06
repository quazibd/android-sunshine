/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.android.sunshine.data.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.time.Instant;
import java.util.Date;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public final class OpenWeatherJsonUtils {
    /* Location information */
    private static final String OWM_CITY = "city";
    private static final String OWM_COORD = "coord";

    /* Location coordinate */
    private static final String OWM_LATITUDE = "lat";
    private static final String OWM_LONGITUDE = "lon";

    /* Weather information. Each day's forecast info is an element of the "list" array */
    private static final String OWM_LIST = "list";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    /* All temperatures are children of the "temp" object */
    private static final String OWM_TEMPERATURE = "temp";

    /* Max temperature for the day */
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";

    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";
    private static final String OWM_WEATHER_DESCRIPTION = "description";

    private static final String OWM_MESSAGE_CODE = "cod";

    /**
     * <p>
     * This method parses JSON from a web response and returns an array of {@link WeatherData}
     * describing the weather over various days from the forecast.
     * </p>
     *
     * @param forecastJsonStr JSON response from server
     * @return Array of Strings describing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static WeatherData[] getWeatherDataFromJson(Context context, String forecastJsonStr)
            throws JSONException {
        WeatherData[] parsedWeatherData;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        parsedWeatherData = new WeatherData[weatherArray.length()];

        for (int i = 0; i < weatherArray.length(); i++) {
            parsedWeatherData[i] = getWeatherDataFromJsonObject(weatherArray.getJSONObject(i), i);
        }

        return parsedWeatherData;
    }

    private static WeatherData getWeatherDataFromJsonObject(JSONObject dayForecast, int index) throws JSONException {
        long localDate = System.currentTimeMillis();
        long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
        long startDay = SunshineDateUtils.normalizeDate(utcDate);

        WeatherData weatherData = new WeatherData();

        /*
         * We ignore all the datetime values embedded in the JSON and assume that
         * the values are returned in-order by day (which is not guaranteed to be correct).
         */
        long dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * index;
        Date fakeDayForIndex = Date.from(Instant.ofEpochMilli(dateTimeMillis));
        weatherData.setDay(fakeDayForIndex);

        /*
         * Description is in a child array called "weather", which is 1 element long.
         * That element also contains a weather code.
         */
        JSONObject weatherObject =
                dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
        weatherData.setWeatherLabel(weatherObject.getString(OWM_WEATHER_DESCRIPTION));
        weatherData.setWeatherCode(weatherObject.getInt(OWM_WEATHER_ID));

        /*
         * Temperatures are sent by Open Weather Map in a child object called "temp".
         *
         * Editor's Note: Try not to name variables "temp" when working with temperature.
         * It confuses everybody. Temp could easily mean any number of things, including
         * temperature, temporary and is just a bad variable name.
         */
        JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
        weatherData.setHighTemp(temperatureObject.getDouble(OWM_MAX));
        weatherData.setLowTemp(temperatureObject.getDouble(OWM_MIN));

        weatherData.setPressure(dayForecast.getDouble(OWM_PRESSURE));
        weatherData.setHumidityPercentage(dayForecast.getInt(OWM_HUMIDITY));
        weatherData.setWindSpeed(dayForecast.getDouble(OWM_WINDSPEED));

        return weatherData;
    }
}