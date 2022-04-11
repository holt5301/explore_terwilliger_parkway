package com.friendsofterwilliger.explore_terwilliger_parkway

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

val species_map = mapOf(
    "PSME" to "Maple",
    "QUGA" to "Gambel Oak",
    "QUCO" to "Scarlet Oak",
    "ACMA" to "Bigleaf Maple",
    "SEGI" to "Giant Sequoia",
    "ROPS" to "Black Locust",
    "PRCE" to "Cherry Plum",
    "THPL" to "Western Redcedar",
    "JU" to "Juniper",
    null to "Unknown"
).withDefault { "$it (Default)" }

@Serializable
data class TreeProperties(
    val Species: String?,
    val DBH: Float,
    val Condition: String?,
    val TreeHeight: Int,
    val OBJECTID: Int,
)

@Serializable
data class TreeGeometry(
    val coordinates: List<Double>,
) {
    val latlng: LatLng
        get() = LatLng(coordinates[1], coordinates[0])
}

@Serializable
data class Tree(
    val properties: TreeProperties,
    val geometry: TreeGeometry
)  : InterestingLandmark {
    val title : String = "Surveyed Tree"

    override fun title(): String { return title }
    override fun summary(): String {
        return """
            Species: ${species_map.getValue(this.properties.Species)}
            Diameter at Breast Height: ${this.properties.DBH} inches
            Height: ${this.properties.TreeHeight} feet
            Condition: ${this.properties.Condition ?: "Unknown"}
        """.trimIndent()
    }
    override fun detail_explanation(): String {
        return this.summary() + "\n\n" + interpretation
    }

    override fun image_tag(): String {
        return properties.Species ?: "default_tree"
    }

    val interpretation: String = """
    Diameter at Breast Height (DBH) is measured in inches and represents the diameter of the tree when measured approximately 4.5 ft off the ground.

    This is typically measured using a measuring tape or string, wrapping around the entire circumference of the tree.  Dividing the circumference by 3.1415 (pi) yields the diameter!
    """.trimIndent()
}

@Serializable
data class TreeCollection(
    val name: String,
    var features: List<Tree>
)
