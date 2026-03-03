package com.conestoga.lifetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter for displaying weather items.
 * Note: This contains intentional bug #2 - incorrect data display (using wrong field).
 */
class WeatherAdapter(private var weatherList: List<WeatherItem>) :
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
        val item = weatherList[position]
        // BUG #2: Intentionally using 'main' instead of 'description'
        // This should display: "Condition: ${item.description}"
        holder.weatherText.text = "Condition: ${item.main}"
    }

    override fun getItemCount(): Int = weatherList.size

    /**
     * Update the weather list with new data.
     *
     * @param newList The new list of weather items
     */
    fun updateList(newList: List<WeatherItem>) {
        weatherList = newList
        notifyDataSetChanged()
    }
}

