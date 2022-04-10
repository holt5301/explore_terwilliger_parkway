package com.example.explore_terwilliger_parkway

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class TerwilligerCustomWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context

    private fun renderWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

    }

    override fun getInfoContents(marker: Marker): View {
        val window = (mContext as Activity).layoutInflater.inflate(R.layout.tree_info_window, null)
        renderWindowText(marker, window)
        return window
    }

    override fun getInfoWindow(marker: Marker): View? {
        var layoutObj: Int
        if (marker.tag is Tree) {
            layoutObj = R.layout.tree_info_window
        } else if (marker.tag is HistoricalLandmark) {
            layoutObj = R.layout.historical_point_window
        } else {
            layoutObj = R.layout.tree_info_window
        }
        val window = (mContext as Activity).layoutInflater.inflate(layoutObj, null)
        renderWindowText(marker, window)
        return window
    }
}
