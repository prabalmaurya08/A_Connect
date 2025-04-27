package com.example.a_connect.alumni.alumniCommunity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.alumni.alumniCommunity.mvvm.AlumniCommunityRepository
import com.example.a_connect.alumni.alumniCommunity.mvvm.AlumniCommunityViewModel
import com.example.a_connect.alumni.alumniCommunity.mvvm.AlumniCommunityViewModelFactory
import com.example.a_connect.alumni.alumniCommunity.mvvm.AlumniPostAdapter
import com.example.a_connect.databinding.FragmentAlumniCommunityPageBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlumniCommunityPage : Fragment() {
    private lateinit var binding: FragmentAlumniCommunityPageBinding
    private val viewModel: AlumniCommunityViewModel by viewModels {
        AlumniCommunityViewModelFactory(AlumniCommunityRepository())
    }

    private lateinit var postAdapter: AlumniPostAdapter
    private val userId = SharedPreferencesHelper.getCurrentUserEmail()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentAlumniCommunityPageBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        postAdapter = AlumniPostAdapter(
            onLikeClick = { postId ->
                if (userId != null) {
                    viewModel.likePost(postId, userId)
                }
            },
            onCommentClick = { postId -> openCommentDialog(postId) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
            itemAnimator = null // Prevent flickering issues
        }

        // ✅ Setup Swipe-to-Refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            postAdapter.refresh() // Reloads posts
        }

        // ✅ Observe paged posts
        lifecycleScope.launch {
            viewModel.pagedPosts.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false // Stop refreshing
            }
        }

        // ✅ Observe Like Updates and refresh UI
        lifecycleScope.launch {
            viewModel.updatedPosts.collectLatest { updatedPosts ->
                postAdapter.snapshot().items.forEachIndexed { index, post ->
                    updatedPosts[post.postId]?.let {
                        postAdapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }


    private fun openCommentDialog(postId: String) {
        // Handle opening a comment dialog
    }

}