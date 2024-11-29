package com.example.a_connect.student.studentEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentPastEventsBinding


class StudentEventPastEvents : Fragment() {

    private var _binding: FragmentPastEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventadaptor: StudentEventRecyclerAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPastEventsBinding.inflate(inflater, container, false)
        setuprecyclerview()
        return binding.root
    }
    private fun setuprecyclerview(){
        val carditem = listOf(
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            StudentEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon)
        )
        eventadaptor= StudentEventRecyclerAdaptor(carditem)
        binding.recyclerViewUpcommingEvent.apply { layoutManager= LinearLayoutManager(context)
            adapter=eventadaptor}
    }
}