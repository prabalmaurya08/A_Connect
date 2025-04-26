package com.example.a_connect.student.studentExplore


import com.example.a_connect.alumni.alumniExplore.AlumniDetailBottomSheet
import com.example.a_connect.alumni.alumniExplore.LocationRepository
import com.example.a_connect.alumni.alumniExplore.LocationViewModel
import com.example.a_connect.alumni.alumniExplore.LocationViewModelFactory
import com.example.a_connect.databinding.FragmentStudentExploreBinding


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.databinding.FragmentAlumniMapBinding
import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileDataClass

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap

class StudentExplore : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentStudentExploreBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var googleMap: GoogleMap
    private val DEFAULT_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/aconnect-fe762.firebasestorage.app/o/5856.jpg?alt=media&token=87b29ed2-7f32-4e87-a000-02ebfd92f098"

    private var userEmail: String =
        SharedPreferencesHelper.getCurrentUserEmail().toString() // Replace with actual user email retrieval
    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStudentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationRepository = LocationRepository()
        val factory = LocationViewModelFactory(locationRepository)
        locationViewModel = ViewModelProvider(this, factory)[LocationViewModel::class.java]

        // Load the map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Fetch Alumni locations


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel.fetchAllAlumniLocations()

        checkLocationPermissionAndFetch()
    }

    private suspend fun getMarkerBitmapFromUrl(imageUrl: String?): BitmapDescriptor {
        return withContext(Dispatchers.IO) {
            try {
                val finalUrl = if (imageUrl.isNullOrEmpty()) DEFAULT_PROFILE_PIC else imageUrl // Use default if empty
                val inputStream = URL(finalUrl).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val resizedBitmap = bitmap.scale(150, 150, false)

                // Create a circular bitmap with a border
                val output = createBitmap(170, 170)
                val canvas = Canvas(output)
                val paint = Paint()
                val rect = Rect(0, 0, 150, 150)

                paint.isAntiAlias = true
                canvas.drawARGB(0, 0, 0, 0)

                // Draw white border
                paint.color = Color.WHITE
                canvas.drawCircle(85f, 85f, 85f, paint)

                // Draw the circular image inside the border
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(resizedBitmap, null, rect, paint)

                // Add drop shadow
                paint.xfermode = null
                paint.color = Color.GRAY
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 6f
                canvas.drawCircle(85f, 85f, 80f, paint)

                return@withContext BitmapDescriptorFactory.fromBitmap(output)
            } catch (e: Exception) {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            }
        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchAndUpdateLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchAndUpdateLocation()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission is required to update location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun fetchAndUpdateLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Location permission not granted.", Toast.LENGTH_SHORT).show()
            return;
        }

        fusedLocationClient.lastLocation.addOnSuccessListener(OnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                locationViewModel.updateUserLocation(userEmail, latitude, longitude) { success ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Location updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to update location.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Location not available.", Toast.LENGTH_SHORT).show()
            }
        }).addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to get location.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancellationTokenSource.cancel()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        locationViewModel.alumniLocations.observe(viewLifecycleOwner) { locations ->
            googleMap.clear()
            lifecycleScope.launch {
                val alumniMap = mutableMapOf<Marker, AlumniProfileDataClass>()

                for (alumni in locations) {
                    val latLng = LatLng(alumni.location?.get("latitude") ?: 0.0, alumni.location?.get("longitude") ?: 0.0)
                    val markerIcon = getMarkerBitmapFromUrl(alumni.profilePic)

                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(alumni.name)
                            .icon(markerIcon)
                    )

                    marker?.let { alumniMap[it] = alumni }
                }

                googleMap.setOnMarkerClickListener { marker ->
                    alumniMap[marker]?.let { alumni ->
                        // Extract necessary data for BottomSheet
                        val name = alumni.name
                        val profilePic = alumni.profilePic
                        val phone = alumni.phoneNumber

                        // Pass the data to the BottomSheet using newInstance() method
                        val bottomSheet = AlumniDetailBottomSheet.newInstance(name, profilePic, phone)
                        bottomSheet.show(parentFragmentManager, "AlumniDetailBottomSheet")
                    }
                    true
                }
            }
        }
    }

}