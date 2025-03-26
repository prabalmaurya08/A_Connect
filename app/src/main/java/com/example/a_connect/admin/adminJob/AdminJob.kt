package com.example.a_connect.admin.adminJob

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminJobBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.a_connect.admin.adminJob.mvvm.AdminJobAdapter

class AdminJob : Fragment() {

    private lateinit var binding: FragmentAdminJobBinding
    private lateinit var navController: NavController
    private var listener: OnGoToCreateJobClickListener? = null

    interface OnGoToCreateJobClickListener {
        fun onGoToCreateJobClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Check if the activity or parent fragment implements OnGoToCreateJobClickListener
        if (activity is OnGoToCreateJobClickListener) {
            listener = activity as OnGoToCreateJobClickListener
        } else {
            throw RuntimeException("$context must implement OnGoToCreateJobClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminJobBinding.inflate(inflater, container, false)

        // Set click listener on Create Job button
        binding.createJobButton.setOnClickListener {
            listener?.onGoToCreateJobClicked()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewPager2 with the adapter
        val adapter = JobTabAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        // Attach TabLayout to ViewPager
        TabLayoutMediator(binding.jobTabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing"
                1 -> "Expired"
                else -> null
            }
        }.attach()
    }
}
