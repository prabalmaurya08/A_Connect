package com.example.a_connect.admin.adminHome

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardRepository
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardViewModelFactory
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardViewmodel
import com.example.a_connect.admin.adminHome.mvvm.AlumniAdapter
import com.example.a_connect.admin.adminHome.mvvm.StudentAdapter
import com.example.a_connect.databinding.FragmentAdminHomeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class AdminHome : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminDashboardViewmodel by viewModels {
        AdminDashboardViewModelFactory(AdminDashboardRepository())
    }
    private lateinit var alumniAdapter: AlumniAdapter
    private lateinit var studentAdapter: StudentAdapter
    // Define the interface
    interface ViewMoreListener {
        fun onViewMoreClicked(section: String)
    }

    private var listener: ViewMoreListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure the parent activity implements the listener
        if (context is ViewMoreListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ViewMoreListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        // Setup Pie Chart and observe ViewModel
        viewModel.dashboardStats.observe(viewLifecycleOwner) { stats ->
            binding.progressBar.visibility = View.GONE

            // Donut chart update
            setupDonutChart(binding.donutChart, stats.alumniCount, stats.studentCount)
            Log.d("AdminHome", "Received stats: $stats")

            // Animate counters
            animateCount(binding.postsCount, stats.postCount)
            animateCount(binding.newsCount, stats.newsCount)
            animateCount(binding.jobsCount, stats.jobCount)

            // Update stat cards
            binding.postsCount.text = stats.postCount.toString()
            binding.newsCount.text = stats.newsCount.toString()
            binding.jobsCount.text = stats.jobCount.toString()
        }

        // Setup RecyclerViews for Alumni and Students
        alumniAdapter = AlumniAdapter(emptyList())
        studentAdapter = StudentAdapter(emptyList())
        binding.alumniRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.alumniRecyclerView.adapter = alumniAdapter
        binding.studentRecyclerView.adapter = studentAdapter

// Initial data loading
        viewModel.loadAlumni()
        viewModel.loadStudent()

// Observe Alumni list
        lifecycleScope.launchWhenStarted {
            viewModel.alumniList.collect { list ->
                if (list.isEmpty()) {
                    Log.e("AdminHome", "Alumni list is empty or null")
                    return@collect
                }

                // Show all alumni without "View More" functionality
                alumniAdapter.updateList(list)
            }
        }

// Observe Student list
        lifecycleScope.launchWhenStarted {
            viewModel.studentList.collect { list ->
                if (list.isEmpty()) {
                    Log.e("AdminHome", "Student list is empty or null")
                    return@collect
                }

                // Show all students without "View More" functionality
                studentAdapter.updateStudentList(list)
            }
        }




        // Trigger fetching of dashboard stats
        viewModel.fetchDashboardStats()
    }

    private fun setupDonutChart(pieChart: PieChart, alumniCount: Int, studentCount: Int) {
        if (alumniCount == 0 && studentCount == 0) {
            pieChart.clear()
            pieChart.centerText = "No Data"
            pieChart.setCenterTextColor(Color.GRAY)
            pieChart.setCenterTextSize(16f)
            return
        }

        val entries = listOf(
            PieEntry(studentCount.toFloat(), "Students"),
            PieEntry(alumniCount.toFloat(), "Alumni")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            resources.getColor(R.color.blue1, null),
            resources.getColor(R.color.green1, null)
        )
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val data = PieData(dataSet)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        data.setValueTextSize(14f)
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data
        pieChart.setUsePercentValues(false) // Important!
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.transparentCircleRadius = 65f
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        pieChart.legend.isWordWrapEnabled = true
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun animateCount(targetView: TextView, targetValue: Int) {
        val animator = ValueAnimator.ofInt(0, targetValue)
        animator.duration = 1000
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            targetView.text = animatedValue.toString()
        }
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
