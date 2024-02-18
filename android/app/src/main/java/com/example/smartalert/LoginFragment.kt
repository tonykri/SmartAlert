package com.example.smartalert

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.example.smartalert.responseData.LoginResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.util.Locale

class LoginFragment : Fragment() {

    private lateinit var spinnerLanguages: Spinner

    private lateinit var sharedPref: SharedPreferences

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        spinnerLanguages = view.findViewById(R.id.spinnerLanguagesLogin)

        val textView = view.findViewById<TextView>(R.id.goToSignupPage)
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)

        email = view.findViewById(R.id.editTextLoginEmail)
        password = view.findViewById(R.id.editTextLoginPassword)

        initSpinner(view)
        setupSpinnerListener()

        textView.setOnClickListener{
            (requireActivity() as MainActivity).goToRegister()
        }
        loginBtn.setOnClickListener{

            login()
        }

        return view
    }

    private fun initSpinner(view: View) {
        val categories: Array<String> = resources.getStringArray(R.array.languages)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguages.adapter = adapter
    }
    private fun setupSpinnerListener() {
        if (resources.configuration.locale.language == "el")
            spinnerLanguages.setSelection(1)
        spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedLanguage = spinnerLanguages.getItemAtPosition(position).toString()
                handleLanguageItemSelected(selectedLanguage)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun handleLanguageItemSelected(language: String) {
        if (language == "English") {
            changeLanguage("en")
        } else {
            changeLanguage("el")
        }
    }
    private fun changeLanguage(language: String) {
        if (resources.configuration.locale.language == language)
            return

        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
        saveLanguage(language)
        restartApp()
    }
    private fun restartApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
    private fun saveLanguage(language: String) {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selectedLanguage", language)
        editor.apply()
    }


    private fun login() {
        val url = "http://10.0.2.2:5025/account/login"

        val jsonBody = """
        {
            "email": "${email.text}",
            "password": "${password.text}"
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
                            showToast("Wrong Credentials")
                        }
                    }
                }
        }


    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showMessageDialog(title: String, message: Any) {
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