package com.example.a_connect.alumni.alumniCommunity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
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
            onLikeClick = { postId -> viewModel.likePost(postId, "USER_ID") },
            onCommentClick = { postId -> openCommentDialog(postId) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
            itemAnimator = null // ✅ Prevent flickering issues
        }

        // ✅ Observe and submit data to adapter
        lifecycleScope.launch {
            viewModel.pagedPosts.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
            }
        }

        // ✅ Show empty state if no data
        lifecycleScope.launch {
            postAdapter.loadStateFlow.collectLatest {
                binding.emptyTextView.visibility = if (postAdapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }

    private fun openCommentDialog(postId: String) {
        // Handle opening a comment dialog
    }

}