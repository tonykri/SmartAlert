package com.example.smartalert

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smartalert.responseData.LoginResponseData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = this.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        startApp(savedInstanceState)
    }

    private fun startApp(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        changeLanguage()

        if (savedInstanceState == null) {
            loggedUser()
            goToSignIn()
        }
    }

    private fun changeLanguage() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val language = sharedPreferences.getString("selectedLanguage", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun goToRegister() {
        val fragment = RegisterFragment()
        replaceFragment(fragment)
    }

    fun goToSignIn() {
        val fragment = LoginFragment()
        replaceFragment(fragment)
    }

    fun goToUser() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    fun goToAdmin() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loggedUser() {
        val refreshToken: String = sharedPref.getString("refreshToken", null) ?: return
        val encodedToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString())

        val url = "http://10.0.2.2:5025/account/refreshtoken?token=$encodedToken"

        Fuel.get(url)
            .response{_, response, result ->
                when (result) {
                    is Result.Success -> {
                        Log.d("RefreshToken", "success block")
                        val responseData = String(response.data)
                        val gson = Gson()
                        val myData: LoginResponseData = gson.fromJson(responseData, LoginResponseData::class.java)
                        saveData(myData)
                        val intent: Intent = if(myData.role == "User")
                            Intent(this, UserActivity::class.java)
                        else
                            Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                    }
                    is Result.Failure -> {
                        Log.d("RefreshToken", "failure block")
                    }
                }

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

    private fun showMessageDialog(title: String, message: String) {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show()
    }
}