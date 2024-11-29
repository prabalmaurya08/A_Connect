package com.example.a_connect.alumni.alumniHome

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a_connect.databinding.FragmentAlumniHomePageBinding
import com.google.android.material.navigation.NavigationView


class AluminiHomePage : Fragment() {
    private lateinit var binding: FragmentAlumniHomePageBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerButton: ImageButton

    private var listener: OnItemClickedInsideViewPager? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnItemClickedInsideViewPager -> {
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlumniHomePageBinding.inflate(layoutInflater)

        drawerSetUp()
        goToChatScreen()


        return binding.root
    }
    private fun goToChatScreen() {
       // findNavController().navigate(R.id.action_aluminiMainPage_to_aluminiChat)

    }

    private fun drawerSetUp() {
        // Set up Navigation Drawer
        drawerLayout = binding.aluminiHomeDrawerLayout
        navView = binding.aluminiHomeNavigationView

        // Set up the toolbar and drawer Button
        toolbar = binding.aluminiHomeToolbar
        drawerButton = binding.aluminiHomePageDrawer


        binding.aluminiHomePageChat.setOnClickListener {
            listener?.onChatButtonClicked()
        }
        binding.aluminiHomePageNotification.setOnClickListener {
            listener?.onNotificationButtonClicked()
        }


        drawerButton.setOnClickListener {
            drawerLayout.open()
        }
}
    override fun onDestroyView() {
        super.onDestroyView()

    }
    interface OnItemClickedInsideViewPager {
        fun onChatButtonClicked()
        fun onNotificationButtonClicked()




    }

}