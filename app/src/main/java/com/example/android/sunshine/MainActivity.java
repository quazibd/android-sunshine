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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.data.FetchWeatherTask;
import com.example.android.sunshine.data.ForecastItemAdapter;
import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.utilities.Constants;
import com.example.android.sunshine.views.ForecastDataHandler;
import com.example.android.sunshine.views.ForecastItemClickListener;
import com.example.android.sunshine.views.MainViewModel;

public class MainActivity extends AppCompatActivity implements ForecastItemClickListener {
    MainViewModel mViewModel;

    ForecastItemAdapter mForecastItemAdapter;
    RecyclerView mForecastItemListRecyclerView;
    TextView mForecastDataErrorTextView;
    ProgressBar mForecastDataProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Initializing the ViewModel (with LiveData).
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Initializing the Adapter for the RecyclerView.
        mForecastItemAdapter = new ForecastItemAdapter(this);

        // Initializing the RecyclerView.
        mForecastItemListRecyclerView = (RecyclerView) findViewById(R.id.rv_forecast);
        mForecastItemListRecyclerView.setHasFixedSize(true);
        mForecastItemListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemListRecyclerView.setAdapter(mForecastItemAdapter);

        // The text view for data load error. This is shown in the place of the RecyclerView if
        // the returned WeatherData is null.
        mForecastDataErrorTextView = (TextView) findViewById(R.id.weather_data_load_error);

        // The progress bar to show during loading of data.
        mForecastDataProgressBar = (ProgressBar) findViewById(R.id.weather_data_loading_bar);

        mViewModel.getWeatherData().observe(
                this, weatherData -> {
                    mForecastDataProgressBar.setVisibility(View.INVISIBLE);
                    if (weatherData == null) {
                        mForecastItemListRecyclerView.setVisibility(View.INVISIBLE);
                        mForecastDataErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        mForecastDataErrorTextView.setVisibility(View.INVISIBLE);
                        mForecastItemListRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mForecastItemAdapter.setWeatherData(weatherData);
                }

        );

        mViewModel.getWeatherDataLoadProgress().observe(
                this, progress -> {
                    mForecastDataProgressBar.setProgress(progress);
                    if (progress != 100) {
                        mForecastDataProgressBar.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            mViewModel.loadWeatherData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onForecastItemClicked(Integer position, WeatherData forecastItemData) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(Constants.WEATHER_DATA_EXTRA, forecastItemData);
        startActivity(detailActivityIntent);
        // Toast.makeText(this, position + ": " + forecastItemData, Toast.LENGTH_SHORT).show();
    }
}