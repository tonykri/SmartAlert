package com.example.smartalert

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.Locale

class AdminActivity : AppCompatActivity() {

    private lateinit var profileBtn: Button
    private lateinit var requestsBtn: Button
    private lateinit var dangersBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        profileBtn = findViewById(R.id.adminProfileBtn)
        requestsBtn = findViewById(R.id.adminRequestsBtn)
        dangersBtn = findViewById(R.id.adminDangersBtn)

        profileBtn.setOnClickListener{
            goToProfile()
        }
        requestsBtn.setOnClickListener{
            goToRequests()
        }
        dangersBtn.setOnClickListener{
            goToDangers()
        }

        startApp(savedInstanceState)
    }

    private fun startApp(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        changeLanguage()

        if (savedInstanceState == null) {
            goToDangers()
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

    private fun goToDangers() {
        val fragment = DangerWrapperFragment()
        replaceFragment(fragment)
    }
    private fun goToRequests() {
        val fragment = RequestAdminWrapperFragment()
        replaceFragment(fragment)
    }
    private fun goToProfile() {
        val fragment = ProfileFragment()
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutAdmin, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
