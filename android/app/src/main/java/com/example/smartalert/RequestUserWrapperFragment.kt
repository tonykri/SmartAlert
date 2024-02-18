package com.example.smartalert

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.smartalert.responseData.DangerResponseData
import com.example.smartalert.responseData.StatsResponseData
import com.example.smartalert.responseData.UserRequestResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class RequestUserWrapperFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var innerLayout: LinearLayout
    private lateinit var addBtn: Button
    private lateinit var totalTextView: TextView
    private lateinit var approvedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request_user_wrapper, container, false)

        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        innerLayout = view.findViewById(R.id.linearLayoutInnerRequestsUserWrapper)
        totalTextView = view.findViewById(R.id.textViewTotalRequests)
        approvedTextView = view.findViewById(R.id.textViewApprovedRequests)

        val fragmentTag = "YourFragmentTag"
        val existingFragment = childFragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment is RequestAddFragment) {

        } else {
            if (savedInstanceState == null) {
                innerLayout.removeAllViews()
                initRequests()
            }
        }

        addBtn = view.findViewById(R.id.addRequestBtn)
        addBtn.setOnClickListener {
            val fragmentList = childFragmentManager.fragments
            for (fragment in fragmentList) {
                childFragmentManager.beginTransaction().remove(fragment).commit()
            }
            innerLayout.removeAllViews()
            val reqAddFragment = RequestAddFragment()

            childFragmentManager.beginTransaction()
                .add(innerLayout.id, reqAddFragment, fragmentTag)
                .commit()
        }

        return view
    }

    private fun initRequests() {
        val url = "http://10.0.2.2:5025/user/dangerrequests"
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
                    }
                    is Result.Failure -> {
                        val error = result.error.exception
                        showToast("Something is wrong")
                    }
                }
            }
    }

    private fun showData(myData: List<UserRequestResponseData>) {
        var approved = 0
        for (dataItem in myData) {
            if (dataItem.approved)
                approved++
            val userRequestFragment = RequestUserFragment.newInstance(dataItem)
            childFragmentManager.beginTransaction()
                .add(innerLayout.id, userRequestFragment)
                .commit()
        }
        activity?.runOnUiThread {
            totalTextView.text = myData.size.toString()
            approvedTextView.text = approved.toString()
        }
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}