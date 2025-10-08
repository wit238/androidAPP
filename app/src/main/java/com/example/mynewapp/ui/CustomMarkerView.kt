package com.example.mynewapp.ui

import android.content.Context
import android.widget.TextView
import com.example.mynewapp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    // This method is called every time the MarkerView is redrawn
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) {
            return
        }
        val timestamp = sdf.format(Date(e.x.toLong()))
        val heartRate = e.y.toInt()
        tvContent.text = "HR: $heartRate bpm at $timestamp"
        super.refreshContent(e, highlight)
    }

    // This method is used to position the marker
    override fun getOffset(): MPPointF {
        // Center the marker horizontally and display it above the selected point
        return MPPointF(-(width / 2).toFloat(), -height.toFloat() - 20)
    }
}
