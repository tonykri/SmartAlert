package com.example.smartalert

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.smartalert.responseData.LoginResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.util.Calendar

class RegisterFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var birthdate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val textView = view.findViewById<TextView>(R.id.goToSignInPage)
        textView.setOnClickListener{
            (requireActivity() as MainActivity).goToSignIn()
        }

        firstname = view.findViewById(R.id.editTextRegisterFirstname)
        lastname = view.findViewById(R.id.editTextRegisterLastname)
        email = view.findViewById(R.id.editTextRegisterEmail)
        password = view.findViewById(R.id.editTextRegisterPassword)
        birthdate = view.findViewById(R.id.editTextRegisterDate)

        birthdate.setOnClickListener{
            showDatePickerDialog()
        }

        val registerBtn = view.findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            register()
        }
        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                var month = (selectedMonth + 1).toString()
                if (selectedMonth + 1 < 10)
                    month = 0.toString() + (selectedMonth + 1).toString()
                var day = (selectedDay).toString()
                if (selectedDay < 10)
                    day = 0.toString() + (selectedDay).toString()
                val selectedDate = "$selectedYear-${month}-$day"
                birthdate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun register() {
        val url = "http://10.0.2.2:5025/account/register"

        val jsonBody = """
        {
            "Firstname": "${firstname.text}",
            "Lastname": "${lastname.text}",
            "Email": "${email.text}",
            "Password": "${password.text}",
            "BirthDate": "${birthdate.text}"
        }
        """.trimIndent()

        Fuel.post(url)
            .jsonBody(jsonBody)
            .response { _, response, result ->
                when (result) {
                    is Result.Success -> {
                        val responseData = String(response.data)
                        val gson = Gson()
                        val myData: LoginResponseData = gson.fromJson(responseData, LoginResponseData::class.java)
                        saveData(myData)
                        if(myData.role == "User")
                            (requireActivity() as MainActivity).goToUser()
                        else
                            (requireActivity() as MainActivity).goToAdmin()
                    }
                    is Result.Failure -> {
                        val error = result.error.exception
                        showMessageDialog("Ops", error.localizedMessage)
                    }
                }
            }
    }

    private fun showToast(message: Any?) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showMessageDialog(title: String, message: Any?) {
        activity?.runOnUiThread {
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message.toString())
                .show()
        }
    }

    private fun saveData(data: LoginResponseData) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString("firstname", data.firstname)
        editor.putString("lastname", data.lastname)
        editor.putString("email", data.email)
        editor.putString("role", data.role)
        editor.putString("accessToken", data.accessToken)
        editor.putString("refreshToken", data.refreshToken)

        editor.apply()
    }

}