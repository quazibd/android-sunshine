package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.databinding.ActivityDetailBinding;
import com.example.android.sunshine.utilities.Constants;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding mActivityDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        WeatherData forecastItemData = intent.getParcelableExtra(Constants.WEATHER_DATA_EXTRA);
        mActivityDetailBinding.setWeatherDetail(forecastItemData.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share_menu) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mActivityDetailBinding.tvDetailWeather.getText());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Fake Weather Details");
            startActivity(shareIntent);
        }

        // This is needed for the framework to handle the UP button.
        return super.onOptionsItemSelected(item);
    }
}