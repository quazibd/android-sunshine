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
package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.data.FetchWeatherTask;
import com.example.android.sunshine.data.ForecastItemAdapter;
import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.utilities.Constants;
import com.example.android.sunshine.views.ForecastDataHandler;
import com.example.android.sunshine.views.ForecastItemClickListener;

public class MainActivity extends AppCompatActivity implements ForecastItemClickListener {
    ForecastDataHandler mForecastDataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        reloadWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            reloadWeatherData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadWeatherData() {
        RecyclerView rvForecastDataView = (RecyclerView) findViewById(R.id.rv_forecast);
        rvForecastDataView.setHasFixedSize(true);
        rvForecastDataView.setLayoutManager(new LinearLayoutManager(this));

        ForecastItemAdapter forecastItemAdapter = new ForecastItemAdapter(this);
        rvForecastDataView.setAdapter(forecastItemAdapter);

        mForecastDataHandler = new ForecastDataHandler(
                rvForecastDataView,
                (TextView) findViewById(R.id.weather_data_load_error),
                (ProgressBar) findViewById(R.id.weather_data_loading_bar),
                forecastItemAdapter);

        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(
                this, mForecastDataHandler);
        fetchWeatherTask.execute();
    }

    @Override
    public void onForecastItemClicked(Integer position, WeatherData forecastItemData) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(Constants.WEATHER_DATA_EXTRA, forecastItemData);
        startActivity(detailActivityIntent);
        // Toast.makeText(this, position + ": " + forecastItemData, Toast.LENGTH_SHORT).show();
    }
}