package com.example.android.sunshine.views;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.databinding.ForecastListItemBinding;

public class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ForecastListItemBinding mItemBinding;
    WeatherData mItemData;
    ForecastItemClickListener mClickListener;

    public ForecastItemViewHolder(@NonNull ForecastListItemBinding forecastListItemBinding,
                                  ForecastItemClickListener clickListener) {
        super(forecastListItemBinding.getRoot());
        this.mItemBinding = forecastListItemBinding;
        this.mClickListener = clickListener;
        mItemBinding.getRoot().setOnClickListener(this);
    }

    public void bind(WeatherData itemData) {
        mItemData = itemData;

        Log.d("ForecastItemViewHolder", "Bind : " + itemData);
        mItemBinding.tvWeatherData.setText(mItemData.toShortString(mItemBinding.getRoot().getContext()));
    }

    @Override
    public void onClick(View view) {
        mClickListener.onForecastItemClicked(getAdapterPosition(), mItemData);
    }
}
