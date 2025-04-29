package com.example.a_connect.admin.adminNews

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.a_connect.admin.adminNews.mvvm.NewsAdapter
import com.example.a_connect.admin.adminNews.mvvm.NewsRepository
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModel
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModelFactory

import com.example.a_connect.databinding.FragmentAdminNewsAnnouncementBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AdminNewsAnnouncement : Fragment() {
    private var _binding: FragmentAdminNewsAnnouncementBinding? = null

    private val binding get() = _binding!!

    private val repository = NewsRepository()
    private val viewModel: NewsViewModel by viewModels { NewsViewModelFactory(repository) }
    private lateinit var adapter: NewsAdapter


    private var listener: OnAdminNewsClicked? = null

    interface OnAdminNewsClicked{
        fun onAddNewsClicked()
        fun onAdminNewsClicked(newsId:String)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnAdminNewsClicked -> {
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
    ): View {


        _binding = FragmentAdminNewsAnnouncementBinding.inflate(inflater, container, false)

        binding.goToAddNews.setOnClickListener {
                    listener?.onAddNewsClicked()
        }


        return binding.root


    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setupRecyclerView()
            observeViewModel()
            viewModel.loadNews()

            // Load news initially
            viewModel.apply {
                clearError()
               // deleteNews("") // Optional: Remove if not needed for initial state
            }
        }

        private fun setupRecyclerView() {
            adapter = NewsAdapter(
                emptyList(),
                onItemClick = {newsId ->


                    listener?.onAdminNewsClicked(newsId.newsId)
                },
                onDeleteClick = { newsId ->
                    viewModel.deleteNews(newsId)
                }
            )

            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@AdminNewsAnnouncement.adapter
            }
        }

        private fun observeViewModel() {
            lifecycleScope.launch {
                viewModel.newsList.collectLatest { newsList ->
                    Log.d("NewsAdapter", "Recyclerview size : $newsList")

                    adapter.updateNewsList(newsList)
                }
            }

            lifecycleScope.launch {
                viewModel.loadingState.collectLatest {
                    if(it){
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                    }else{
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                    }



                }
            }

            lifecycleScope.launch {
                viewModel.errorState.collectLatest { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.clearError()
                    }
                }
            }
        }




}