package com.example.smartalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.smartalert.responseData.DangerResponseData
import com.example.smartalert.responseData.UserRequestResponseData

class DangerFragment : Fragment() {

    private lateinit var categoryName: TextView
    private lateinit var createdAt: TextView
    private lateinit var message: TextView
    private lateinit var similarRequests: TextView

    companion object {
        fun newInstance(data: DangerResponseData): DangerFragment {
            val fragment = DangerFragment()
            val args = Bundle()
            args.putString("id", data.id)
            args.putString("categoryName", data.categoryName)
            args.putString("createdAt", data.createdAt)
            args.putString("protectionEn", data.category.protectionEn)
            args.putString("protectionEl", data.category.protectionEl)
            args.putDouble("latitude", data.latitude)
            args.putDouble("longitude", data.longitude)
            args.putInt("noOfRequests", data.noOfRequests)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_danger, container, false)

        categoryName = view.findViewById(R.id.textViewDangerCategory)
        createdAt = view.findViewById(R.id.textViewDangerCreatedAt)
        message = view.findViewById(R.id.textViewDangerProtection)
        similarRequests = view.findViewById(R.id.textViewDangerNoOfRequests)

        var msg = arguments?.getString("protectionEn")
        if (resources.configuration.locale.language == "el")
            msg = arguments?.getString("protectionEl")

        categoryName.text = arguments?.getString("categoryName")
        createdAt.text = arguments?.getString("createdAt")?.substring(0, 10)
        message.text = msg
        similarRequests.text = arguments?.getInt("noOfRequests").toString()

        return view
    }

}