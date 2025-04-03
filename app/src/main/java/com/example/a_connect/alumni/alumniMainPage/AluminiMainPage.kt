package com.example.a_connect.alumni.alumniMainPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniCommunity.AluminiCommunityPage
import com.example.a_connect.alumni.alumniJob.AlumniJob
import com.example.a_connect.alumni.alumniPost.AlumniPost
import com.example.a_connect.alumni.alumniProfile.AlumniProfile
import com.example.a_connect.databinding.FragmentAlumniMainPageBinding
import com.example.a_connect.login.AlumniLogin
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

class AluminiMainPage : Fragment() {

    private var _binding: FragmentAlumniMainPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: AlumniMainPageViewPagerAdapter

    private lateinit var defaultTopBar: MaterialToolbar
    private var searchTopBar: View? = null
    private var searchInput: EditText? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlumniMainPageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Views
        viewPager = binding.alumniMainPageViewPager
        bottomNavigationView = binding.alumniMainPageBottomNav
        defaultTopBar = binding.defaultTopBar


        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        setupHamburgerMenu()
        setupViewPager()
        setupBottomNavigation()
        setupNotificationIcon()

        return view
    }

    private fun setupHamburgerMenu() {
        toggle = ActionBarDrawerToggle(
            requireActivity(), drawerLayout, defaultTopBar,
            R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuClick(menuItem)
            true
        }
    }

    private fun setupViewPager() {
        adapter = AlumniMainPageViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> switchToDefaultBar("Dashboard", 0)
                R.id.bottom_nav_community -> switchToDefaultBar("Community", 1)
                R.id.bottom_nav_post -> switchToDefaultBar("Posts", 2)
                R.id.bottom_nav_job -> switchToJobSection()
                R.id.bottom_nav_profile -> switchToDefaultBar("Profile", 4)
                else -> false
            }
            true
        }
    }

    private fun switchToDefaultBar(title: String, position: Int) {
        viewPager.currentItem = position
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar?.visibility = View.GONE
        defaultTopBar.title = title
    }

    private fun switchToJobSection() {
        viewPager.currentItem = 3
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar?.visibility = View.VISIBLE
        defaultTopBar.title = "Jobs"
    }

    private fun setupNotificationIcon() {
        binding.notificationIcon.setOnClickListener {
            showNotificationBottomSheet()
        }
    }

    @SuppressLint("InflateParams")
    private fun showNotificationBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.alumini_notification, null)
        dialog.setContentView(sheetView)
        dialog.show()
    }

    private fun handleMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.profile -> loadFragment(AlumniProfile())
            R.id.community -> loadFragment(AluminiCommunityPage())
            R.id.aluminiPost -> loadFragment(AlumniPost())
            R.id.alumniJob -> loadFragment(AlumniJob())
            R.id.logout -> logoutUser()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.alumniMainPageViewPager, fragment) // Ensure correct container ID
            .addToBackStack(null)
            .commit()
    }

    private fun logoutUser() {
        val intent = Intent(requireContext(), AlumniLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
