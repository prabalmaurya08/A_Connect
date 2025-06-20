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

import com.example.a_connect.admin.adminCollegeProfile.AdminCollegeProfile
import com.example.a_connect.admin.adminHome.AdminHome
import com.example.a_connect.admin.adminJob.AdminJob
import com.example.a_connect.admin.adminJob.ExpiredJob
import com.example.a_connect.admin.adminJob.OngoingJob
import com.example.a_connect.admin.adminMainPage.AdminMainPageDirections
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement

import com.example.a_connect.alumni.alumniHome.AlumniHomePage
import com.example.a_connect.alumni.alumniJob.AlumniJob
import com.example.a_connect.alumni.alumniMainPage.AluminiMainPageDirections

import com.example.a_connect.alumni.alumniProfile.AlumniProfile
import com.example.a_connect.databinding.ActivityMainBinding
import com.example.a_connect.login.AlumniLogin
import com.example.a_connect.login.StudentLogin
import com.example.a_connect.student.studentHomePage.StudentHomePage
import com.example.a_connect.student.studentJob.StudentJob
import com.example.a_connect.student.studentMainPage.StudentMainPageDirections
import com.example.a_connect.student.studentProfile.StudentProfile

class MainActivity : AppCompatActivity() , AlumniHomePage.OnItemClickedInsideViewPager, AlumniLogin.OnAlumniScreenClicked,StudentLogin.OnStudentScreenClicked,OngoingJob.OnAdminJobClickListener
,ExpiredJob.OnJobClickListener, AdminHome.ViewMoreListener,AdminNewsAnnouncement.OnAdminNewsClicked, StudentHomePage.OnStudentHomePageItemClicked,AdminCollegeProfile.OnGoToEditProfileClickListener, AlumniJob.OnAlumniJobClickListener,StudentJob.OnStudentJobClickListener,AlumniProfile.OnAlumniProfileItemClicked,AdminJob.OnGoToCreateJobClickListener,AlumniHomePage.VoiceInputListener

, StudentProfile.OnStudentProfileItemClicked{

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



        SharedPreferencesHelper.init(applicationContext)





        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController




    }

    override fun onNewsClicked(newsId: String) {
        TODO("Not yet implemented")
    }

    override fun onChatButtonClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_aluminiChat)


    }

    override fun onNotificationButtonClicked() {

    }
    override fun onStudentEditProfileClicked(){
        findNavController(R.id.fragment).navigate(R.id.action_studentMainPage_to_studentEditProfile)
    }

    override fun onMapClick() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_alumniMap)
    }

    override fun onSearchClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_alumniSearchScreen)
    }

    override fun onVoiceInputClicked() {

    }

    override fun onAlumniSubmitClicked() {
       findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_aluminiMainPage)
    }

    override fun onAlumniAdminClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_adminLogin)
    }
    override fun onAdminJobClicked(jobId: String) {
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

    override fun onAlumniJobClicked(jobId: String) {
        val action = AluminiMainPageDirections.actionAluminiMainPageToAlumniJobDetail(jobId)
        findNavController(R.id.fragment).navigate(action)
    }
    override fun onStudentJobClicked(jobId: String) {
        val action = StudentMainPageDirections.actionStudentMainPageToStudentJobDetail(jobId)
        findNavController(R.id.fragment).navigate(action)
    }

    override fun onJobClicked(jobId: String) {
        TODO("Not yet implemented")
    }

    override fun onEditProfileClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_alumniEditProfile)
    }

    override fun onUniqueIdClicked() {
        TODO("Not yet implemented")
    }

    override fun onShareProfileClicked() {
        TODO("Not yet implemented")
    }

    override fun onAddAlumniMilestone() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_alumniAddMilestone)
    }

    override fun onAddMilestoneClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_studentMainPage_to_addMilestone)
    }

    override fun onGoToCreateJobClicked() {
       findNavController(R.id.fragment).navigate(R.id.action_adminMainPage_to_adminAddJob)
    }

    override fun onGoToEditProfileClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_adminMainPage_to_adminEditProfile)


    }

    override fun onChatCommandReceived() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_aluminiChat)

    }

    override fun onNotificationCommandReceived() {
        TODO("Not yet implemented")
    }

    override fun onSearchCommandReceived() {
        findNavController(R.id.fragment).navigate(R.id.action_aluminiMainPage_to_alumniSearchScreen)

    }

    override fun onAddNewsClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_adminMainPage_to_adminAddNewsAnnouncement)

    }

    override fun onAdminNewsClicked(newsId:String) {
        val action = AdminMainPageDirections.actionAdminMainPageToAdminNewsAnnouncementDescription(newsId)
        findNavController(R.id.fragment).navigate(action)
    }



    override fun onStudentSearchClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_studentMainPage_to_alumniSearchScreen)


    }

    override fun onViewMoreClicked(section: String) {

    }
}