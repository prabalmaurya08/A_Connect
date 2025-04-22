package com.example.a_connect.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminLoginBinding
import com.google.android.material.appbar.AppBarLayout

class AdminLogin : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentAdminLoginBinding.inflate(layoutInflater)

        //toolbar implementation in appBarLayout in admin login fragment
        val appBarLayout: AppBarLayout = binding.adminLoginAppBarLayout

        val toolbar: androidx.appcompat.widget.Toolbar = binding.adminLoginToolbar

        toolbar.setNavigationOnClickListener {

            findNavController().navigate(R.id.action_adminLogin_to_mainLogin)
        }


        binding.submitButton.setOnClickListener {

            if(binding.adminLoginTv1.text.toString()=="admin" && binding.adminLoginPassword.text.toString()=="admin"))
            findNavController().navigate(R.id.action_adminLogin_to_adminMainPage)
        }
        return binding.root
    }


}