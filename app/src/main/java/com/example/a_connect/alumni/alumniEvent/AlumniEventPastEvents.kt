package com.example.a_connect.alumni.alumniEvent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentUpcommingEventsBinding


class AlumniEventPastEvents : Fragment() {

    private var _binding: Fragment? = null
    private val binding get() = _binding!!
    private lateinit var eventadaptor: AlumniEventRecyclerAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcommingEventsBinding.inflate(inflater, container, false)
        setuprecyclerview()
        return binding.root
    }
    private fun setuprecyclerview(){
        val carditem = listOf(AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon),
            AlumniEventDataItem(R.drawable.alumnieventcardimageexamle,"Jo Malone's London's Lorem Ipsum is simply dummy text that will wrap correctly inside the card","06 Nov -WED","4:00PM -7:00 PM",R.drawable.bookmark_blue_fill_icon)
        )
        eventadaptor= AlumniEventRecyclerAdaptor(carditem)
        binding.recyclerViewUpcommingEvent.apply { layoutManager= LinearLayoutManager(context)
            adapter=eventadaptor}
    }
}