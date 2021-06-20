package com.example.android.sunshine.views;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.data.ForecastDataChangeListener;
import com.example.android.sunshine.data.ForecastDataHolder;

public class ForecastDataHandler implements ForecastDataChangeListener {
    RecyclerView mForecastItemListRecyclerView;
    TextView mForecastDataErrorTextView;
    ProgressBar mForecastDataProgressBar;
    ForecastDataHolder mForecastDataHolder;

    public ForecastDataHandler(
            RecyclerView rvForecastItemList,
            TextView tvForecastDataError,
            ProgressBar pbForecastDataProgress,
            ForecastDataHolder forecastDataHolder) {
        mForecastItemListRecyclerView = rvForecastItemList;
        mForecastDataErrorTextView = tvForecastDataError;
        mForecastDataProgressBar = pbForecastDataProgress;
        mForecastDataHolder = forecastDataHolder;
    }

    @Override
    public void onForecastDataChanged(String[] forecastData) {
        mForecastDataProgressBar.setVisibility(View.INVISIBLE);

        if (forecastData == null) {
            mForecastItemListRecyclerView.setVisibility(View.INVISIBLE);
            mForecastDataErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mForecastDataErrorTextView.setVisibility(View.INVISIBLE);
            mForecastItemListRecyclerView.setVisibility(View.VISIBLE);
        }

        mForecastDataHolder.setWeatherData(forecastData);
    }

    @Override
    public void onForecastDataProgress(Integer progress) {
        mForecastDataProgressBar.setVisibility(View.VISIBLE);
        mForecastDataProgressBar.setProgress(progress);
    }
}
