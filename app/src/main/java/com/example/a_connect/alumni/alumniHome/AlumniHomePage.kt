package com.example.a_connect.alumni.alumniHome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.admin.adminNews.mvvm.NewsRepository
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModel
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModelFactory
import com.example.a_connect.alumni.alumniHome.mvvm.AlumniHomeViewModel
import com.example.a_connect.databinding.FragmentAlumniHomePageBinding
import com.example.a_connect.student.studentHomePage.SecondNewsAdapter
import com.example.a_connect.student.studentHomePage.StudentHomePage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlumniHomePage : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var _binding: FragmentAlumniHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var alumniHomeViewModel: AlumniHomeViewModel
    private val repository = NewsRepository()
    private val viewModel: NewsViewModel by viewModels { NewsViewModelFactory(repository) }
    private lateinit var adapter: SecondNewsAdapter


    private var listener: OnItemClickedInsideViewPager? = null

    companion object {
        private const val REQUEST_CODE_AUDIO = 1001
    }

    interface OnItemClickedInsideViewPager {
        fun onNewsClicked(newsId: String)
        fun onChatButtonClicked()
        fun onNotificationButtonClicked()
        fun onMapClick()
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
        alumniHomeViewModel = ViewModelProvider(this)[AlumniHomeViewModel::class.java]



        // Request permissions before starting voice input
        checkPermissions()
        if(SharedPreferencesHelper.getCurrentUserName().isNullOrEmpty()){

            binding.userName.text="Welcome, Alumni"
        }
        binding.userName.text="Hi ,"+SharedPreferencesHelper.getCurrentUserName()

        // Set up UI elements and actions
        drawerSetUp()
        setupFAB()

        loadImage()


        // Handle Search Bar click
        binding.alumniHomePageSearchBar.setOnClickListener {
            listener?.onSearchClicked()
        }

        return binding.root
    }
    private fun loadImage(){
        val collegeId = "collegeId123"

        // Fetch the image URL when the fragment is created
        alumniHomeViewModel.fetchImageUrl(collegeId)

        // Observe the imageUrl LiveData and load the image into the ImageView
        alumniHomeViewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Log.d("AlumniHomePage", "Image URL: $imageUrl")

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.alumni_job_detail_bg)  // Placeholder while loading
                    .error(R.drawable.alumni_job_detail_bg)      // Error image if loading fails
                    .into(binding.alumniHomePageCollegeImage)   // Your ImageView
            } else {
                // Set default image if no image URL is available
                binding.alumniHomePageCollegeImage.setImageResource(R.drawable.alumni_job_detail_bg)
            }
        }
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
//        binding.aluminiHomePageChat.setOnClickListener {
//            listener?.onChatButtonClicked()
//        }
        /*binding.aluminiHomePageNotification.setOnClickListener {
            listener?.onNotificationButtonClicked()
        }

         */
    }

    private fun setupFAB() {
        binding.fabVoiceInput.setOnClickListener {
            // Show the Voice Input BottomSheet when the FAB is clicked
            val voiceInputBottomSheet = VoiceInputBottomSheetFragment.newInstance()
            voiceInputBottomSheet.show(childFragmentManager, voiceInputBottomSheet.tag)
        }
    }
    private fun setupRecyclerView() {
        adapter = SecondNewsAdapter(
            emptyList(),
            onItemClick = {newsId ->


                listener?.onNewsClicked(newsId.newsId)
            },

            )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AlumniHomePage.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.newsList.collectLatest { newsList ->
                Log.d("NewsAdapter", "Recyclerview size : $newsList")

                adapter.updateNewsList(newsList)
            }
        }

        lifecycleScope.launch {
            viewModel.loadingState.collectLatest {
                if(it){
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }else{
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                }



            }
        }

        lifecycleScope.launch {
            viewModel.errorState.collectLatest { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.loadNews()

        // Load news initially
        viewModel.apply {
            clearError()
            // deleteNews("") // Optional: Remove if not needed for initial state
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapPreviewContainer) as? SupportMapFragment
            ?: SupportMapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.mapPreviewContainer, it).commit()
            }

        mapFragment.getMapAsync(this)

       binding.viewFullMap.setOnClickListener {
          listener?.onMapClick()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(
                    MarkerOptions().position(userLatLng).title("Your Location")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
            }
        }
    }


}
