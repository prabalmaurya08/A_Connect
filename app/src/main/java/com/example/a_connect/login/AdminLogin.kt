package com.example.a_connect.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminLoginBinding


class AdminLogin : Fragment() {
    private lateinit var binding: FragmentAdminLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentAdminLoginBinding.inflate(layoutInflater)
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_adminLogin_to_adminMainPage)
        }
        return binding.root
    }


}