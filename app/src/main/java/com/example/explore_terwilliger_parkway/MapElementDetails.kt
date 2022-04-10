package com.example.explore_terwilliger_parkway

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 * Use the [MapElementDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapElementDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var details: String? = null
    private var image_path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            details = it.getString("details")
            image_path = it.getString("image_path")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var inflated_view = inflater.inflate(R.layout.fragment_map_element_details, container, false)
        inflated_view.findViewById<TextView>(R.id.details_title)?.text = title
        inflated_view.findViewById<TextView>(R.id.details_body)?.text = details
        val image_id = activity!!.resources.getIdentifier(image_path, "drawable", activity!!.packageName)
        inflated_view.findViewById<ImageView>(R.id.details_image)?.setImageResource(image_id)
        return inflated_view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapElementDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String, details: String, image_path: String) =
            MapElementDetails().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("details", details)
                    putString("image_path", image_path)
                }
            }
    }
}