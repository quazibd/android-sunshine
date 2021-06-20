package com.example.android.sunshine.views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.R;

public class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mItemTextView;
    String mItemText;
    ForecastItemClickListener mClickListener;

    public ForecastItemViewHolder(@NonNull View itemView, ForecastItemClickListener clickListener) {
        super(itemView);
        mItemTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);

        mClickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(String itemText) {
        Log.d("ForecastItemViewHolder", "Bind : " + itemText);
        mItemTextView.setText(itemText);
        mItemText = itemText;
    }

    @Override
    public void onClick(View view) {
        mClickListener.onForecastItemClicked(getAdapterPosition(), mItemText);
    }
}
