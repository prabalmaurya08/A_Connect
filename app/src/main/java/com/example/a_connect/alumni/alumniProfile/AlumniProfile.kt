package com.example.a_connect.alumni.alumniProfile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.a_connect.R

import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.UserSessionManager

import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostViewmodel
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniEditProfileViewModelFactory
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileRepository
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileViewModel
import com.example.a_connect.databinding.FragmentAlumniProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.getValue


class AlumniProfile : Fragment() {

    private lateinit var userSessionManager: UserSessionManager


    private var _binding: FragmentAlumniProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var profileViewModel: AlumniPostViewmodel

    private val viewModel: AlumniProfileViewModel by viewModels {
        AlumniEditProfileViewModelFactory(AlumniProfileRepository())
    }
    private lateinit var currentUserEmail: String
    private var profileImageUri: Uri? = null


    private var listener: OnAlumniProfileItemClicked? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnAlumniProfileItemClicked -> {
                listener=context


            }



            else -> {
                throw ClassCastException("$context must implement OnSignupClickListener")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentAlumniProfileBinding.inflate(inflater,container,false)
        profileViewModel = ViewModelProvider(this)[AlumniPostViewmodel::class.java]
        currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail() ?: return binding.root
        swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {

            profileViewModel.loadUserPosts(currentUserEmail) // Refresh posts
            swipeRefreshLayout.isRefreshing = false // Stop refresh animation
        }

        binding.editProfileButton.setOnClickListener {
            listener?.onEditProfileClicked()
        }
       setUpViewPagerAdapter()

        //for data to load
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail() ?: return binding.root
        viewModel.fetchUserProfile(currentUserEmail)
        // Observe the profilePic LiveData and update the image view if it changes
        viewModel.profilePic.observe(viewLifecycleOwner, Observer { profilePicUrl ->
            profilePicUrl?.let {
                // Load image from URL (or locally stored URI) into the ImageView
                // Assuming you are using an image loading library like Glide or Picasso
                Glide.with(requireContext())
                    .load(profilePicUrl)  // If the URL is stored in Firestore, this should work
                    .into(binding.editProfileCircleImageView)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSessionManager = UserSessionManager(requireContext())

        setupLogoutButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    //private functions

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {

            userSessionManager.logout()

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_aluminiMainPage_to_mainLogin,
                null,

                NavOptions.Builder()
                    .setPopUpTo(R.id.mainLogin, true)
                    .build()
            )
        }
    }

    private fun setUpViewPagerAdapter() {
        val adapter = AlumniProfileViewPagerAdapter(requireActivity())
        binding.alumniProfileViewPager.adapter = adapter

        // Attach TabLayout to ViewPager
        TabLayoutMediator(binding.alumniProfileTabLayout, binding.alumniProfileViewPager) { tab, position ->
            tab.text = when (position) {

                0-> "Posts"
                else -> null
            }
        }.attach()
    }

    interface OnAlumniProfileItemClicked {
        fun onEditProfileClicked()
        fun onUniqueIdClicked()
        fun onShareProfileClicked()

    }

}