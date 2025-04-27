package com.example.a_connect

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.a_connect.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private lateinit var sessionManager: UserSessionManager
    private var handler: Handler? = null

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize session manager
        sessionManager = UserSessionManager(requireContext())

        binding.splashAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        binding.splashAnimationView.playAnimation()

        // Use safe handler approach
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            // Check if fragment is still attached before proceeding
            if (isAdded) {
                checkUserLoginStatus()
            }
        }, 3000)  // 3 seconds delay
    }

    private fun checkUserLoginStatus() {
        // First check if fragment is attached
        if (!isAdded) return

        try {
            if (sessionManager.isLoggedIn()) {
                val navOptions = navOptions {
                    popUpTo(R.id.splashScreenFragment) {
                        inclusive = true
                    }
                }
                when (sessionManager.getUserType()) {
                    UserSessionManager.USER_TYPE_ALUMNI -> {

                        findNavController().navigate(R.id.action_splashScreenFragment_to_aluminiMainPage, null, navOptions)

                        if (isAdded) {
                            Toast.makeText(requireContext(), "Welcome back, Alumni!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    UserSessionManager.USER_TYPE_STUDENT -> {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_studentMainPage, null, navOptions)
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Welcome back, Student!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    UserSessionManager.USER_TYPE_ADMIN -> {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_adminMainPage, null, navOptions)
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Welcome back, Admin!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                val navOptions = navOptions {
                    popUpTo(R.id.splashScreenFragment) {
                        inclusive = true
                    }
                }
                if (isAdded && findNavController().currentDestination?.id == R.id.splashScreenFragment) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_mainLogin, null, navOptions)
                }
            }
        } catch (e: Exception) {
            Log.e("SplashScreenFragment", "Error during navigation: ${e.message}")
        }
    }

    override fun onDestroyView() {
        // Remove any pending messages to avoid crashes
        handler?.removeCallbacksAndMessages(null)
        handler = null

        binding.splashAnimationView.cancelAnimation()
        super.onDestroyView()
        _binding = null
    }
}
