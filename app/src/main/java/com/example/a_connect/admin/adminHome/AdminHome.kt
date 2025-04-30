package com.example.a_connect.admin.adminHome
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.a_connect.R
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardRepository
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardViewModelFactory
import com.example.a_connect.admin.adminHome.mvvm.AdminDashboardViewmodel
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
    private lateinit var viewModel: AdminDashboardViewmodel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val repository = AdminDashboardRepository()
        val factory = AdminDashboardViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AdminDashboardViewmodel::class.java] // ✅ Proper assignment


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE


        viewModel.dashboardStats.observe(viewLifecycleOwner) { stats ->
            binding.progressBar.visibility = View.GONE

            // Donut chart update
            setupDonutChart(binding.donutChart, stats.alumniCount, stats.studentCount)
            Log.d("AdminHome", "Received stats: $stats")
            animateCount(binding.postsCount, stats.postCount)
            animateCount(binding.newsCount, stats.newsCount)
            animateCount(binding.jobsCount, stats.jobCount)


            // Update Stat Cards
            binding.postsCount.text = stats.postCount.toString()
            binding.newsCount.text = stats.newsCount.toString()
            binding.jobsCount.text = stats.jobCount.toString()
        }

        viewModel.fetchDashboardStats() // Trigger fetching
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




}