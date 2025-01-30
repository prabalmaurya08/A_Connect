package com.example.a_connect.alumni.alumniProfile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostViewmodel
import com.example.a_connect.databinding.FragmentAlumniProfilePostBinding


class AlumniProfilePost : Fragment() {
    private lateinit var binding:FragmentAlumniProfilePostBinding
    private lateinit var profileViewModel: AlumniPostViewmodel
    private lateinit var postAdapter: AlumniProfilePostAdapter
    private lateinit var currentUserEmail: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlumniProfilePostBinding.inflate(inflater, container, false)
        profileViewModel = ViewModelProvider(this)[AlumniPostViewmodel::class.java]

        currentUserEmail = SharedPreferencesHelper.getCurrentUserEmail() ?: return binding.root

        // Handle post clicks
        postAdapter = AlumniProfilePostAdapter { post ->
//            val intent = Intent(requireContext(), PostDetailActivity::class.java)
//            intent.putExtra("postId", post.postId)
//            startActivity(intent)
        }

        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = postAdapter
        }


        profileViewModel.userPost.observe(viewLifecycleOwner) { posts ->
            Log.d("PostFirestore", "Fetched ${posts.size} posts for user: $currentUserEmail")

            postAdapter.submitList(posts)
        }

        profileViewModel.loadUserPosts(currentUserEmail)
        return binding.root;
    }
    override fun onResume() {
        super.onResume()
        // Replace with dynamic user email
        profileViewModel.loadUserPosts(currentUserEmail)  // Refresh posts when returning
    }



}