package com.example.a_connect.student.studentProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.UserSessionManager
import com.example.a_connect.databinding.FragmentStudentProfileBinding


class StudentProfile : Fragment() {

    private lateinit var binding: FragmentStudentProfileBinding

    private lateinit var profileToolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileToolbar.inflateMenu(R.menu.profile_toolbar)

        profileToolbar.setOnMenuItemClickListener { menuItem ->
            handleMenuItemClick(menuItem)
        }
    }

    private fun handleMenuItemClick(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> false

            }
        }

    private fun logout() {
        val sessionManager = UserSessionManager(requireContext())

        sessionManager.logout()

        findNavController().navigate(R.id.action_studentMainPage_to_mainLogin, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.mainLogin, true)
                .build())

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}








