package com.example.a_connect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.a_connect.admin.adminJob.AdminJobDirections
import com.example.a_connect.admin.adminJob.ExpiredJob
import com.example.a_connect.admin.adminJob.OngoingJob
import com.example.a_connect.admin.adminMainPage.AdminMainPageDirections
import com.example.a_connect.alumni.alumniHome.AluminiHomePage
import com.example.a_connect.databinding.ActivityMainBinding
import com.example.a_connect.login.AlumniLogin
import com.example.a_connect.login.StudentLogin

class MainActivity : AppCompatActivity() , AluminiHomePage.OnItemClickedInsideViewPager, AlumniLogin.OnAlumniScreenClicked,StudentLogin.OnStudentScreenClicked,OngoingJob.OnJobClickListener
,ExpiredJob.OnJobClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController


    }

    override fun onChatButtonClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_aluminiChat)

    }

    override fun onNotificationButtonClicked() {

    }

    override fun onAlumniSubmitClicked() {
       findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_aluminiMainPage)
    }

    override fun onAlumniAdminClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_adminLogin)
    }
    override fun onJobClicked(jobId: String) {
        // Use Safe Args to navigate to the JobDetailsFragment and pass the jobId
        val action = AdminMainPageDirections.actionAdminMainPageToAdminJobDetail(jobId)
        findNavController(R.id.fragment).navigate(action)
    }

    override fun onStudentSubmitClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_studentMainPage)
    }

    override fun onStudentAdminClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_adminLogin)
    }
}