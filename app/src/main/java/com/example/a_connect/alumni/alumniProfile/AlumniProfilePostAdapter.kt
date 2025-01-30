package com.example.a_connect.alumni.alumniProfile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass

class AlumniProfilePostAdapter(private val onPostClick: (AlumniPostDataClass) -> Unit) :
    ListAdapter<AlumniPostDataClass, AlumniProfilePostAdapter.PostViewHolder>(DiffCallback()) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.postImageView)

        fun bind(post: AlumniPostDataClass, onPostClick: (AlumniPostDataClass) -> Unit) {
            Glide.with(itemView.context)
                .load(post.media.firstOrNull()) // Load the first image
                .into(imageView)

            itemView.setOnClickListener { onPostClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_grid, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        Log.d("PostAdapter", "Binding post: $post")
        holder.bind(getItem(position), onPostClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<AlumniPostDataClass>() {
        override fun areItemsTheSame(oldItem: AlumniPostDataClass, newItem: AlumniPostDataClass) = oldItem.postId == newItem.postId
        override fun areContentsTheSame(oldItem: AlumniPostDataClass, newItem: AlumniPostDataClass) = oldItem == newItem
    }
}
