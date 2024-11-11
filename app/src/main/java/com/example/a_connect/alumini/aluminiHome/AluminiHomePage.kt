package com.example.a_connect.alumini.aluminiHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a_connect.databinding.FragmentAluminiHomePageBinding
import com.google.android.material.navigation.NavigationView


class AluminiHomePage : Fragment() {
    private lateinit var binding: FragmentAluminiHomePageBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerButton: ImageButton





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAluminiHomePageBinding.inflate(layoutInflater)

        drawerSetUp()


        return binding.root
    }

    private fun drawerSetUp() {
        // Set up Navigation Drawer
        drawerLayout = binding.aluminiHomeDrawerLayout
        navView = binding.aluminiHomeNavigationView

        // Set up the toolbar and drawer Button
        toolbar = binding.aluminiHomeToolbar
        drawerButton = binding.aluminiHomePageDrawer


        drawerButton.setOnClickListener {
            drawerLayout.open()
        }
}
}