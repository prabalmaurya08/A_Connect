package com.example.a_connect.student.studentEvent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.databinding.StudentEventCardBinding


class StudentEventRecyclerAdaptor(private val cardList: List<StudentEventDataItem>) : RecyclerView.Adapter<StudentEventRecyclerAdaptor.CardViewHolder>() {

    inner class CardViewHolder(private val binding: StudentEventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: StudentEventDataItem) {
            binding.eventcardimage.setImageResource(card.imageResId)
            binding.eventcardtext.text=card.eventTitle
            binding.eventcarddate.text=card.eventDate
            binding.eventcardtime.text=card.eventTime
            binding.eventcardbookmark.setImageResource(card.bookmark)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = StudentEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int = cardList.size
}
