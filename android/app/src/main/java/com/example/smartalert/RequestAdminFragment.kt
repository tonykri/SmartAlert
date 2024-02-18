package com.example.smartalert

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.smartalert.responseData.UserRequestResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class RequestAdminFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var categoryName: TextView
    private lateinit var createdAt: TextView
    private lateinit var message: TextView
    private lateinit var coordinates: TextView
    private lateinit var approved: TextView
    private lateinit var similarRequests: TextView

    private lateinit var approveBtn: Button
    private lateinit var declineBtn: Button


    companion object {
        fun newInstance(data: UserRequestResponseData): RequestAdminFragment {
            val fragment = RequestAdminFragment()
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
        val view = inflater.inflate(R.layout.fragment_request_admin, container, false)

        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        categoryName = view.findViewById(R.id.textViewRequestAdminCategory)
        createdAt = view.findViewById(R.id.textViewRequestAdminCreatedAt)
        message = view.findViewById(R.id.textViewRequestAdminMessage)
        coordinates = view.findViewById(R.id.textViewRequestAdminCoordinates)
        approved = view.findViewById(R.id.textViewRequestAdminApproved)
        similarRequests = view.findViewById(R.id.textViewRequestAdminSimilarRequests)

        categoryName.text = arguments?.getString("categoryName")
        createdAt.text = arguments?.getString("createdAt")?.substring(0, 10)
        message.text = arguments?.getString("message")
        coordinates.text = arguments?.getDouble("latitude").toString() + ", " + arguments?.getDouble("longitude").toString()
        approved.text = arguments?.getBoolean("approved").toString()
        similarRequests.text = arguments?.getInt("similarRequests").toString()

        approveBtn = view.findViewById(R.id.approveBtn)
        declineBtn = view.findViewById(R.id.declineBtn)


        approveBtn.setOnClickListener{
            approveRequest(arguments?.getString("id"))
        }
        declineBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .remove(this@RequestAdminFragment)
                .commit()
        }

        return view
    }

    private fun approveRequest(id: String?) {
        if (id == null)
            return
        val url = "http://10.0.2.2:5025/admin/dangerrequests/approve?requestId=${id}"
        val authToken = sharedPref.getString("accessToken", "")

        Fuel.get(url)
            .header("Authorization" to "Bearer $authToken")
            .response { _, response, result ->
                when (result) {
                    is Result.Success -> {
                        showToast("Request Approved")
                        parentFragmentManager.beginTransaction()
                            .remove(this@RequestAdminFragment)
                            .commit()
                    }
                    is Result.Failure -> {
                        val error = result.error.exception
                        showToast("Something is wrong")
                    }
                }
            }
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}