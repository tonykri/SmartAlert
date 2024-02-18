package com.example.smartalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ProfileFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var firstname: TextView
    private lateinit var lastname: TextView
    private lateinit var email: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPref = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        firstname = view.findViewById(R.id.textViewProfileFirstname)
        lastname = view.findViewById(R.id.textViewProfileLastname)
        email = view.findViewById(R.id.textViewProfileEmail)

        firstname.text = sharedPref.getString("firstname", null)
        lastname.text = sharedPref.getString("lastname", null)
        email.text = sharedPref.getString("email", null)


        view.findViewById<Button>(R.id.logoutBtn).setOnClickListener{
            val editor: SharedPreferences.Editor = sharedPref.edit()

            editor.putString("firstname", "")
            editor.putString("lastname", "")
            editor.putString("email", "")
            editor.putString("role", "")
            editor.putString("accessToken", "")
            editor.putString("refreshToken", "")

            editor.apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            requireActivity().startActivity(intent)
        }

        return view
    }

}