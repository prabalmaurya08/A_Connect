package com.example.a_connect.alumni.alumniCommunity.mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a_connect.R
import com.example.a_connect.SharedPreferencesHelper
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.example.a_connect.databinding.CommunityPostBinding
import com.google.firebase.Timestamp

class AlumniPostAdapter(
    private val onLikeClick: (String) -> Unit,
    private val onCommentClick: (String) -> Unit
) : PagingDataAdapter<AlumniPostDataClass, AlumniPostAdapter.PostViewHolder>(POST_COMPARATOR) {

    inner class PostViewHolder(private val binding: CommunityPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: AlumniPostDataClass, onLikeClick: (String) -> Unit, onCommentClick: (String) -> Unit) {

            binding.description.text = post.description
            binding.name.text = post.name
            binding.likeCount.text = post.likes.size.toString()
            binding.commentCount.text = post.comments.size.toString()
            fun formatTimeAgo(timestamp: Timestamp): String {
                val diffInMillis = System.currentTimeMillis() - timestamp.toDate().time
                val diffInHours = diffInMillis / (1000 * 60 * 60)
                return if (diffInHours < 24) {
                    "$diffInHours hours ago"
                } else {
                    val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                    "$diffInDays days ago"
                }
            }

            // ✅ Get userId dynamically (replace this with actual userId)
            val userId = SharedPreferencesHelper.getCurrentUserEmail()
            val isLiked = post.likes.contains(userId)
            updateLikeButtonState(binding.likeImageButton, isLiked)

            // ✅ Load first image if available
            val imageUrl = post.media.firstOrNull()
            Glide.with(binding.root.context)
                .load(imageUrl ?: R.drawable.ic_app_icon) // Placeholder
                .into(binding.postImageView)

            // ✅ Handle Like button click with animation
            binding.likeImageButton.setOnClickListener {
                val newIsLiked = !post.likes.contains(userId) // Toggle like status
                animateLike(binding.likeImageButton, newIsLiked)

                // Update the likes in the post object
                val updatedLikes = userId?.let { it1 -> updateLikesList(post, it1, newIsLiked) }
                if (updatedLikes != null) {
                    post.likes = updatedLikes
                }

                // Update like count text
                if (updatedLikes != null) {
                    binding.likeCount.text = updatedLikes.size.toString()
                }

                // Trigger ViewModel to persist the change
                onLikeClick(post.postId)
            }

            // ✅ Handle Comment button click
            binding.commentImageButton.setOnClickListener {
                onCommentClick(post.postId)
            }
        }

        private fun updateLikesList(post: AlumniPostDataClass, userId: String, addLike: Boolean): List<String> {
            val updatedLikes = post.likes.toMutableList()
            if (addLike) {
                updatedLikes.add(userId) // Add user to likes list
            } else {
                updatedLikes.remove(userId) // Remove user from likes list
            }
            return updatedLikes
        }

        private fun updateLikeButtonState(button: ImageView, isLiked: Boolean) {
            val likeDrawableRes = if (isLiked) {
                R.drawable.like_blue_fill_icon // Liked state icon
            } else {
                R.drawable.like_blue_icon // Unliked state icon
            }
            button.setImageResource(likeDrawableRes)
        }

        // ✅ Add Like animation logic
        private fun animateLike(view: ImageView, isLiked: Boolean) {
            if (isLiked) {
                view.setImageResource(R.drawable.like_blue_fill_icon)
                view.scaleX = 0.7f
                view.scaleY = 0.7f
                view.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
            } else {
                view.setImageResource(R.drawable.like_blue_icon)
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
