package com.conestoga.lifetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherList: List<WeatherItem>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weatherText: TextView = view.findViewById(R.id.weatherText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.weatherText.text = "Condition: ${weatherList[position].description}"
    }

    override fun getItemCount(): Int = weatherList.size
}
