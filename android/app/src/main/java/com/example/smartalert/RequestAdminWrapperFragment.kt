package com.example.smartalert

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.smartalert.responseData.UserRequestResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class RequestAdminWrapperFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var innerLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request_admin_wrapper, container, false)

        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        innerLayout = view.findViewById(R.id.linearLayoutRequestsAdminWrapper)
        innerLayout.removeAllViews()

        if (savedInstanceState == null)
            initRequests()

        return view
    }

    private fun initRequests() {
        var url = "http://10.0.2.2:5025/admin/dangerrequests"
        val authToken = sharedPref.getString("accessToken", "")

        Fuel.get(url)
            .header("Authorization" to "Bearer $authToken")
            .response { _, response, result ->
                when (result) {
                    is Result.Success -> {

                        val responseData = String(response.data)
                        val gson = Gson()
                        val myData: List<UserRequestResponseData> = gson.fromJson(responseData, Array<UserRequestResponseData>::class.java).toList()

                        showData(myData)
                        if (myData.isEmpty())
                            showToast("There are no requests")
                    }
                    is Result.Failure -> {
                        val error = result.error.exception
                        showToast("Something is wrong")
                    }
                }
            }
    }

    private fun showData(myData: List<UserRequestResponseData>) {
        for (dataItem in myData) {
            val adminRequestFragment = RequestAdminFragment.newInstance(dataItem)
            childFragmentManager.beginTransaction()
                .add(innerLayout.id, adminRequestFragment)
                .commit()
        }
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}