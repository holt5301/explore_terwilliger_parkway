package com.friendsofterwilliger.explore_terwilliger_parkway

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalLandmark(
    val name: String,
    val brief: String,
    val details: String,
    val coordinates: List<Double>,
    val image_tag: String,
) : InterestingLandmark {
    override fun title(): String { return  name }
    override fun detail_explanation(): String { return details }
    override fun summary(): String { return brief }
    override fun image_tag(): String { return image_tag }
    val latlng: LatLng
        get() = LatLng(coordinates[1], coordinates[0])
}

@Serializable
data class HistoricalLandmarkCollection(
    var historical_landmarks: List<HistoricalLandmark>
)
