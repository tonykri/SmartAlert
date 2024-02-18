package com.example.smartalert

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.smartalert.responseData.UserRequestResponseData
import com.google.gson.annotations.SerializedName

class RequestUserFragment : Fragment() {

    private lateinit var categoryName: TextView
    private lateinit var createdAt: TextView
    private lateinit var message: TextView
    private lateinit var coordinates: TextView
    private lateinit var approved: TextView
    private lateinit var similarRequests: TextView


    companion object {
        fun newInstance(data: UserRequestResponseData): RequestUserFragment {
            val fragment = RequestUserFragment()
            val args = Bundle()
            args.putString("id", data.id)
            args.putString("categoryName", data.categoryName)
            args.putString("createdAt", data.createdAt)
            args.putString("message", data.message)
            args.putDouble("latitude", data.latitude)
            args.putDouble("longitude", data.longitude)
            args.putBoolean("approved", data.approved)
            args.putInt("similarRequests", data.similarRequests)

            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request_user, container, false)

        categoryName = view.findViewById(R.id.textViewRequestUserCategory)
        createdAt = view.findViewById(R.id.textViewRequestUserCreatedAt)
        message = view.findViewById(R.id.textViewRequestUserMessage)
        coordinates = view.findViewById(R.id.textViewRequestUserCoordinates)
        approved = view.findViewById(R.id.textViewRequestUserApproved)
        similarRequests = view.findViewById(R.id.textViewRequestUserSimilarRequests)

        categoryName.text = arguments?.getString("categoryName")
        createdAt.text = arguments?.getString("createdAt")?.substring(0, 10)
        message.text = arguments?.getString("message")
        coordinates.text = arguments?.getDouble("latitude").toString() + ", " + arguments?.getDouble("longitude").toString()
        approved.text = arguments?.getBoolean("approved").toString()
        similarRequests.text = arguments?.getInt("similarRequests").toString()

        return view
    }

}