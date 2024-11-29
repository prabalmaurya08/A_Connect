package com.example.a_connect.alumini.alumniEvent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.databinding.AlumniEventCardBinding

class AlumniEventRecyclerAdaptor(private val cardList: List<AlumniEventDataItem>) : RecyclerView.Adapter<AlumniEventRecyclerAdaptor.CardViewHolder>() {

    inner class CardViewHolder(private val binding: AlumniEventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: AlumniEventDataItem) {
            binding.eventcardimage.setImageResource(card.imageResId)
            binding.eventcardtext.text=card.eventTitle
            binding.eventcarddate.text=card.eventDate
            binding.eventcardtime.text=card.eventTime
            binding.eventcardbookmark.setImageResource(card.bookmark)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = AlumniEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int = cardList.size
}
