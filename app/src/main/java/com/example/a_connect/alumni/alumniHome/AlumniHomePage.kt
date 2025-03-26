package com.example.a_connect.alumni.alumniHome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAlumniHomePageBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class AlumniHomePage : Fragment() {

    private var _binding: FragmentAlumniHomePageBinding? = null
    private val binding get() = _binding!!


    private var listener: OnItemClickedInsideViewPager? = null

    companion object {
        private const val REQUEST_CODE_AUDIO = 1001
    }

    interface OnItemClickedInsideViewPager {
        fun onChatButtonClicked()
        fun onNotificationButtonClicked()
        fun onSearchClicked()
        fun onVoiceInputClicked()  // Add this method to handle the FAB click
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemClickedInsideViewPager) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnItemClickedInsideViewPager interface")
        }
    }

    interface VoiceInputListener {
        fun onChatCommandReceived()
        fun onNotificationCommandReceived()
        fun onSearchCommandReceived()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlumniHomePageBinding.inflate(inflater, container, false)

        // Request permissions before starting voice input
        checkPermissions()

        // Set up UI elements and actions
        drawerSetUp()
        setupFAB()


        // Handle Search Bar click
        binding.aluminiHomePageSearchBar.setOnClickListener {
            listener?.onSearchClicked()
        }

        return binding.root
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_AUDIO)
        }
    }

    // Handle the permission result
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with the voice input setup
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private fun drawerSetUp() {
        binding.aluminiHomePageChat.setOnClickListener {
            listener?.onChatButtonClicked()
        }
        binding.aluminiHomePageNotification.setOnClickListener {
            listener?.onNotificationButtonClicked()
        }
    }

    private fun setupFAB() {
        binding.fabVoiceInput.setOnClickListener {
            // Show the Voice Input BottomSheet when the FAB is clicked
            val voiceInputBottomSheet = VoiceInputBottomSheetFragment.newInstance()
            voiceInputBottomSheet.show(childFragmentManager, voiceInputBottomSheet.tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}
