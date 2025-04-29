package com.example.a_connect.student.studentHomePage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.UserSessionManager
import com.example.a_connect.admin.adminNews.AdminNewsAnnouncement
import com.example.a_connect.admin.adminNews.mvvm.NewsAdapter
import com.example.a_connect.admin.adminNews.mvvm.NewsRepository
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModel
import com.example.a_connect.admin.adminNews.mvvm.NewsViewModelFactory
import com.example.a_connect.alumni.alumniHome.AlumniHomePage.OnItemClickedInsideViewPager
import com.example.a_connect.alumni.alumniHome.mvvm.AlumniHomeViewModel
import com.example.a_connect.databinding.FragmentStudentHomePageBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue


class StudentHomePage : Fragment() {
    private var _binding: FragmentStudentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: UserSessionManager
    private lateinit var currentUserEmail: String
    private lateinit var alumniHomeViewModel: AlumniHomeViewModel
    private val repository = NewsRepository()
    private val viewModel: NewsViewModel by viewModels { NewsViewModelFactory(repository) }
    private lateinit var adapter: SecondNewsAdapter


    private var listener2: OnStudentNewsClicked? = null

    interface OnStudentNewsClicked{


        fun onStudentNewsClicked(newsId:String)
    }




    private var listener: OnStudentHomePageItemClicked? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStudentHomePageItemClicked) {
            listener = context
        }
        else if (context is OnStudentNewsClicked) {
            listener2 = context

        } else{
            throw ClassCastException("$context must implement OnItemClickedInsideViewPager interface")
        }
    }
    interface OnStudentHomePageItemClicked{



        fun onStudentSearchClicked()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentHomePageBinding.inflate(inflater, container, false)
        alumniHomeViewModel = ViewModelProvider(this)[AlumniHomeViewModel::class.java]
        loadImage()
        // Handle Search Bar click
        binding.studentHomePageSearchBar.setOnClickListener {
            listener?.onStudentSearchClicked()
        }

        sessionManager = UserSessionManager(requireContext())
        currentUserEmail=sessionManager.getCurrentUserEmail().toString()
        binding.userName.text="Hi ,"+SharedPreferencesHelper.getStudentName()
        return binding.root
    }
    private fun loadImage(){
        val collegeId = "collegeId123"

        // Fetch the image URL when the fragment is created
        alumniHomeViewModel.fetchImageUrl(collegeId)

        // Observe the imageUrl LiveData and load the image into the ImageView
        alumniHomeViewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Log.d("AlumniHomePage", "Image URL: $imageUrl")

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.alumni_job_detail_bg)  // Placeholder while loading
                    .error(R.drawable.alumni_job_detail_bg)      // Error image if loading fails
                    .into(binding.alumniHomePageCollegeImage)   // Your ImageView
            } else {
                // Set default image if no image URL is available
                binding.alumniHomePageCollegeImage.setImageResource(R.drawable.alumni_job_detail_bg)
            }
        }
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
        adapter = SecondNewsAdapter(
            emptyList(),
            onItemClick = {newsId ->


                listener2?.onStudentNewsClicked(newsId.newsId)
            },

        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StudentHomePage.adapter
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