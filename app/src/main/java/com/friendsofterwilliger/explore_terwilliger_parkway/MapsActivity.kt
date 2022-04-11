package com.friendsofterwilliger.explore_terwilliger_parkway
import android.content.res.Resources
import kotlinx.serialization.json.Json
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.friendsofterwilliger.explore_terwilliger_parkway.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.serialization.json.decodeFromStream

// TODO: Investigate text to speech

interface InterestingLandmark {
    fun title(): String
    fun detail_explanation(): String
    fun summary(): String
    fun image_tag(): String
}

class MapsActivity :
    AppCompatActivity(),
    //OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowCloseListener {

    private lateinit var mMap: GoogleMap
    //private lateinit var binding: ActivityMapsBinding
    private var m_last_selected_marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMapsBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.main_fragment_view, mapFragment)
        }
        mapFragment.getMapAsync(this)

        val mi_button = findViewById<Button>(R.id.more_info_button)
        mi_button.visibility = View.INVISIBLE
        mi_button.setOnClickListener {
            Log.d("MapsActivity","Clicked the button")
            if (m_last_selected_marker != null){
                val sel_obj = m_last_selected_marker?.tag as InterestingLandmark?
                val details_frag = MapElementDetails.newInstance(
                    sel_obj!!.title(),
                    sel_obj!!.detail_explanation(),
                    sel_obj!!.image_tag()
                )
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.main_fragment_view, details_frag)
                    addToBackStack(null)
                }
                findViewById<Button>(R.id.more_info_button).visibility = View.INVISIBLE
            }
        }

    }

    override fun onInfoWindowClose(p0: Marker) {
        findViewById<Button>(R.id.more_info_button).visibility = View.INVISIBLE
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("MapsActivity", "Marker clicked!  Woot")
        findViewById<Button>(R.id.more_info_button).visibility = View.VISIBLE
        m_last_selected_marker = p0
        return false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        val pixelWidth = Resources.getSystem().displayMetrics.widthPixels
        val pixelHeight = Resources.getSystem().displayMetrics.heightPixels

        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowCloseListener(this)

        mMap.setInfoWindowAdapter(TerwilligerCustomWindowAdapter(this))

        val terwilliger_bound = LatLngBounds(
            LatLng(45.468540493701624, -122.6947030349592),
            LatLng(45.50632373838916, -122.6749176189416))
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(terwilliger_bound, pixelWidth, pixelHeight, 0))
        mMap.setLatLngBoundsForCameraTarget(terwilliger_bound)

        // Handle Trees
        val tree_bmpd = BitmapDescriptorFactory.fromAsset("tree_icon.png")
        val input_stream = this.applicationContext.assets.open("Terwilliger.geojson")
        val decoder = Json{isLenient = true; ignoreUnknownKeys = true}
        val trees = decoder.decodeFromStream<TreeCollection>(input_stream)
        for (tree in trees.features) {
            var tree_marker = mMap.addMarker(
                MarkerOptions()
                    .position(tree.geometry.latlng)
                    .title("Surveyed Tree")
                    .snippet("${tree.summary()}")
                    .icon(tree_bmpd))
            tree_marker?.tag = tree
        }

        // Handle Historical Landmarks
        val info_circle_bmpd = BitmapDescriptorFactory.fromAsset("info_circle.png")
        val input_stream2 = this.applicationContext.assets.open("historical_landmarks.json")
        val decoder2 = Json{isLenient = true; ignoreUnknownKeys = true}
        val hist_lms = decoder2.decodeFromStream<HistoricalLandmarkCollection>(input_stream2)
        for (hist_lm in hist_lms.historical_landmarks) {
            Log.d("MapsActivity","Now printing landmark")
            Log.d("MapsActivity", hist_lm.toString())
            var hist_lm_marker = mMap.addMarker(
                MarkerOptions()
                    .position(hist_lm.latlng)
                    .title(hist_lm.name)
                    .snippet(hist_lm.brief)
                    .icon(info_circle_bmpd))
            hist_lm_marker?.tag = hist_lm
        }
    }
}