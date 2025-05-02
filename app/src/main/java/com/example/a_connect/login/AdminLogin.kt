package com.example.a_connect.login

import android.app.Dialog
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
    private lateinit var loadingDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentAdminLoginBinding.inflate(layoutInflater)
        initLoadingDialog()

        //toolbar implementation in appBarLayout in admin login fragment
        val toolbar: androidx.appcompat.widget.Toolbar = binding.adminLoginToolbar

        toolbar.setNavigationOnClickListener {

            findNavController().navigate(R.id.action_adminLogin_to_mainLogin)
        }

        binding.submitButton.setOnClickListener {
            loadingDialog.show()

            if (binding.adminLoginEmail.text.toString() == "admin@gmail.com" && binding.adminLoginPassword.text.toString() == "1234") {

                findNavController().navigate(R.id.action_adminLogin_to_adminMainPage)
                loadingDialog.dismiss()
                Toast.makeText(context, "Welcome Admin !!", Toast.LENGTH_SHORT).show()
            }

            if ( binding.adminLoginPassword.text.toString() != "1234") {
                loadingDialog.dismiss()

                Toast.makeText(context, "wrong password please try again !!", Toast.LENGTH_SHORT).show()

            }
            if (binding.adminLoginEmail.text.toString() != "admin@gmail.com") {
                loadingDialog.dismiss()

                Toast.makeText(context, "wrong credentials please try again !!", Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }
    // Initialize the loading dialog with Lottie animation
    private fun initLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)  // Prevent user interaction during loading
    }
}