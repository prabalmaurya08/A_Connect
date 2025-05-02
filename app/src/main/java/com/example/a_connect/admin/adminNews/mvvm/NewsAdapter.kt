package com.example.a_connect.admin.adminNews.mvvm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.databinding.AdminNewsCardBinding

import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private var newsList: List<NewsDataClass>,
    private val onItemClick: (NewsDataClass) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: AdminNewsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsDataClass) {
            binding.newsHeading.text = news.heading
            binding.newsDetail.text = news.description
            binding.time.text = formatTime(news.createdAt)
            binding.Date.text = formatDate(news.createdAt)
            Log.d("NewsAdapter", "Loading image: ${news.headlinePhotoUrl}")



            Glide.with(binding.universityImage.context)
                .load(news.headlinePhotoUrl)
                .placeholder(R.drawable.college_image)
                .into(binding.universityImage)

            binding.root.setOnClickListener {
                onItemClick(news)
            }

            binding.delete.setOnClickListener {
                onDeleteClick(news.newsId)
            }
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        private fun formatTime(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} mins ago"
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hrs ago"
                else -> "${diff / (24 * 60 * 60 * 1000)} days ago"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = AdminNewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNewsList(newList: List<NewsDataClass>) {
        newsList = newList
        notifyDataSetChanged()
    }
}

