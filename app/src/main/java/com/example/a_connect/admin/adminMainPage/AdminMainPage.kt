package com.example.a_connect.admin.adminMainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminMainpageBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

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

        setupViewPager()
        setupBottomNavigation()
        setupNavigationDrawer()
        setupNotificationIcon()

        return view
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
                R.id.bottom_nav_job -> switchToJobSection(3)
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

    private fun switchToJobSection(position: Int) {
        viewPager.currentItem = position
        defaultTopBar.visibility = View.VISIBLE
        searchTopBar.visibility = View.VISIBLE
        defaultTopBar.title = "Jobs"
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            binding.defaultTopBar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuClick(menuItem)
            true
        }
    }

    private fun handleMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.profile -> { /* Handle Profile Navigation */ }
            R.id.events -> { /* Handle Events Navigation */ }
            R.id.news_and_announcements -> { /* Handle News Navigation */ }
            R.id.explore -> { /* Handle Explore Navigation */ }
            R.id.community -> { /* Handle Community Navigation */ }
            R.id.saved_jobs -> { /* Handle Saved Jobs Navigation */ }
            R.id.donation -> { /* Handle Donation Navigation */ }
            R.id.about_us -> { /* Handle About Us Navigation */ }
            R.id.report -> { /* Handle Report Navigation */ }
            R.id.feedback -> { /* Handle Feedback Navigation */ }
            R.id.logout -> { /* Handle Logout */ }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupNotificationIcon() {
        binding.notificationIcon1.setOnClickListener {
            showNotificationBottomSheet()
        }
    }

    private fun showNotificationBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.admin_notification, null)
        dialog.setContentView(sheetView)

        val clearAllButton = sheetView.findViewById<View>(R.id.clear_all_button)
        val markAsReadButton = sheetView.findViewById<View>(R.id.mark_as_read_button)

        clearAllButton.setOnClickListener {
            // Logic to clear notifications
        }

        markAsReadButton.setOnClickListener {
            // Logic to mark notifications as read
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
