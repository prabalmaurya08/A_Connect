package com.example.a_connect.admin.adminMainPage


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.a_connect.R
import com.example.a_connect.admin.adminAboutUs.AdminAboutUS
import com.example.a_connect.admin.adminAboutUs.AdminFeedback
import com.example.a_connect.admin.adminAboutUs.AdminReport
import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.admin.adminDonation.AdminDonation
import com.example.a_connect.admin.adminEvent.AdminEvent
import com.example.a_connect.admin.adminExplore.AdminExplore
import com.example.a_connect.admin.adminHome.AdminHome
import com.example.a_connect.admin.adminJob.AdminJob
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

    private lateinit var bottomNavigationView: BottomNavigationView
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
        bottomNavigationView = binding.bottomNav
        defaultTopBar = binding.defaultTopBar
        searchTopBar = binding.searchTopBar
        searchInput = binding.searchInput
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        setupHamburgerMenu()
        setupBottomNavigation()
        setupNotificationIcon()

        // Load default fragment (Dashboard)
        replaceFragment(AdminHome())



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()

                if (!navController.navigateUp()) {
                    requireActivity().finish() // Exit app if no back stack
                }
            }
        })
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

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START) // Close drawer if open
            when (item.itemId) {
                R.id.bottom_nav_home -> replaceFragment(AdminHome(), "Dashboard")
                R.id.bottom_nav_announcement -> replaceFragment(AdminNewsAnnouncement(), "Announcements & News")
                R.id.bottom_nav_events -> replaceFragment(AdminEvent(), "Events")
                R.id.bottom_nav_job -> replaceFragment(AdminJob(), "Jobs", showSearchBar = true)
                R.id.bottom_nav_college -> replaceFragment(AdminCollegeProfile(), "College Profile")
                else -> false
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment, title: String = "", showSearchBar: Boolean = false) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()

        defaultTopBar.visibility = View.VISIBLE
        searchTopBar.visibility = if (showSearchBar) View.VISIBLE else View.GONE
        defaultTopBar.title = title
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
        dialog.show()
    }

    private fun handleMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.profile -> {
                replaceFragment(AdminCollegeProfile(), "College Profile")
                bottomNavigationView.selectedItemId = R.id.bottom_nav_college
            }
            R.id.events -> {
                replaceFragment(AdminEvent(), "Events")
                bottomNavigationView.selectedItemId = R.id.bottom_nav_events
            }
            R.id.news_and_announcements -> {
                replaceFragment(AdminNewsAnnouncement(), "News & Announcements")
                bottomNavigationView.selectedItemId = R.id.bottom_nav_announcement
            }
            R.id.explore -> {
               findNavController().navigate(R.id.action_adminMainPage_to_adminExplore)
                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.community -> {
                replaceFragment(AdminCommunity(), "Community")
                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.saved_jobs -> {
                replaceFragment(SavedJob(), "Saved Jobs")
                bottomNavigationView.selectedItemId = R.id.bottom_nav_job
            }
            R.id.donation -> {
              findNavController().navigate(R.id.action_adminMainPage_to_adminDonation)

                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.about_us -> {
            findNavController().navigate(R.id.action_adminMainPage_to_adminAboutUS)
                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.report -> {
               findNavController().navigate(R.id.action_adminMainPage_to_adminReport)


                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.feedback -> {
              findNavController().navigate(R.id.action_adminMainPage_to_adminFeedback)
                // Set bottom navigation item to a corresponding ID if exists
            }
            R.id.logout -> logoutUser()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
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
