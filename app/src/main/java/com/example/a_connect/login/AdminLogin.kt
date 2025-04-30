package com.example.a_connect.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminLoginBinding

class AdminLogin : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentAdminLoginBinding.inflate(layoutInflater)

        //toolbar implementation in appBarLayout in admin login fragment
        val toolbar: androidx.appcompat.widget.Toolbar = binding.adminLoginToolbar

        toolbar.setNavigationOnClickListener {

            findNavController().navigate(R.id.action_adminLogin_to_mainLogin)
        }

        binding.submitButton.setOnClickListener {

            if (binding.adminLoginEmail.text.toString() == "admin@gmail.com" && binding.adminLoginPassword.text.toString() == "1234") {

                findNavController().navigate(R.id.action_adminLogin_to_adminMainPage)

            }

            if ( binding.adminLoginPassword.text.toString() != "1234") {

                Toast.makeText(context, "wrong password please try again !!", Toast.LENGTH_SHORT).show()

            }
            if (binding.adminLoginEmail.text.toString() != "admin@gmail.Com") {

                Toast.makeText(context, "wrong credentials please try again !!", Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }
}