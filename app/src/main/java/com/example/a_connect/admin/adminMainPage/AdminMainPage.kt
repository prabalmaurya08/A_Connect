package com.example.a_connect.admin.adminMainPage

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
import com.example.a_connect.R.id.about_us
import com.example.a_connect.R.id.community
import com.example.a_connect.R.id.donation
import com.example.a_connect.R.id.events
import com.example.a_connect.R.id.explore
import com.example.a_connect.R.id.feedback
import com.example.a_connect.R.id.logout
import com.example.a_connect.R.id.news_and_announcements
import com.example.a_connect.R.id.profile
import com.example.a_connect.R.id.report
import com.example.a_connect.R.id.saved_jobs
import com.example.a_connect.admin.adminAboutUs.AdminAboutUS
import com.example.a_connect.admin.adminAboutUs.AdminFeedback
import com.example.a_connect.admin.adminAboutUs.AdminReport
import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.admin.adminDonation.AdminDonation
import com.example.a_connect.admin.adminEvent.AdminEvent
import com.example.a_connect.admin.adminExplore.AdminExplore
import com.example.a_connect.admin.adminJob.SavedJob
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement
import com.example.a_connect.admin.admincommunity.AdminCommunity
import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.example.a_connect.login.AlumniLogin
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

@Suppress("UNUSED_EXPRESSION")
class AdminMainPage : Fragment() {

    private var _binding: FragmentAdminMainpageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: AdminMainPageViewPagerAdapter

    private lateinit var defaultTopBar: MaterialToolbar
    private lateinit var searchTopBar: View
    private lateinit var searchInput: EditText
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMainpageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Views
        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav
        defaultTopBar = binding.defaultTopBar
        searchTopBar = binding.searchTopBar
        searchInput = binding.searchInput
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
        adapter = AdminMainPageViewPagerAdapter(this)
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
                R.id.bottom_nav_announcement -> switchToDefaultBar("Announcements & News", 1)
                R.id.bottom_nav_events -> switchToDefaultBar("Events", 2)
                R.id.bottom_nav_job -> 3.switchToJobSection()
                R.id.bottom_nav_college -> switchToDefaultBar("College Profile", 4)
                else -> false
            }
            true
        }
    }

    private fun switchToDefaultBar(title: String, position: Int) {
        viewPager.currentItem = position
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar.visibility = View.GONE
        defaultTopBar.title = title
    }

    private fun Int.switchToJobSection() {
        viewPager.currentItem = this
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar.visibility = View.VISIBLE
        defaultTopBar.title = "Jobs"
    }

    private fun setupNotificationIcon() {
        binding.notificationIcon1.setOnClickListener {
            showNotificationBottomSheet()
        }
    }

    @SuppressLint("InflateParams")
    private fun showNotificationBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.admin_notification, null)
        dialog.setContentView(sheetView)
        dialog.show()
    }

    private fun handleMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            profile -> loadFragment(AdminCollegeProfile())
            events -> loadFragment(AdminEvent())
            news_and_announcements -> loadFragment(AdminNewsAnnouncement())
            explore -> loadFragment(AdminExplore())
            community -> loadFragment(AdminCommunity())
            saved_jobs -> loadFragment(SavedJob())
            donation -> loadFragment(AdminDonation())
            about_us -> loadFragment(AdminAboutUS())
            report -> loadFragment(AdminReport())
            feedback -> loadFragment(AdminFeedback())
            logout -> logoutUser()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.drawer_layout, fragment)
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
