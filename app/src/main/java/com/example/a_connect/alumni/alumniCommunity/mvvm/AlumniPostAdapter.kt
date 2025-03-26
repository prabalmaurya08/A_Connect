package com.example.a_connect.alumni.alumniCommunity.mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.example.a_connect.databinding.CommunityPostBinding

class AlumniPostAdapter(
    private val onLikeClick: (String) -> Unit, // Callback for like button
    private val onCommentClick: (String) -> Unit // Callback for comment button
) : PagingDataAdapter<AlumniPostDataClass, AlumniPostAdapter.PostViewHolder>(POST_COMPARATOR) {

    class PostViewHolder(private val binding: CommunityPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: AlumniPostDataClass, onLikeClick: (String) -> Unit, onCommentClick: (String) -> Unit) {
            binding.description.text = post.description
            binding.name.text = post.createdBy
            binding.likeCount.text = post.likes.size.toString()
            binding.commentCount.text = post.comments.size.toString()

            // ✅ Fix: Ensure post.media is a list and load the first image safely
            val imageUrl = post.media.firstOrNull() // Get first image or null
            Glide.with(binding.root.context)
                .load(imageUrl ?: R.drawable.ic_app_icon) // Use placeholder if no image
                .into(binding.postImageView)

            // Handle like button click
            binding.likeImageButton.setOnClickListener {
                onLikeClick(post.postId)
            }

            // Handle comment button click
            binding.commentImageButton.setOnClickListener {
                onCommentClick(post.postId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CommunityPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { post ->
            holder.bind(post, onLikeClick, onCommentClick)
        }
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<AlumniPostDataClass>() {
            override fun areItemsTheSame(oldItem: AlumniPostDataClass, newItem: AlumniPostDataClass): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: AlumniPostDataClass, newItem: AlumniPostDataClass): Boolean {
                return oldItem == newItem
            }
        }
    }
}
