package com.example.a_connect.alumni.alumniHome.ai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a_connect.R

class ChatAdapter(private val messages: MutableList<ChatMessageDataClass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> UserViewHolder(inflater.inflate(R.layout.user_message, parent, false))
            VIEW_TYPE_BOT -> BotViewHolder(inflater.inflate(R.layout.bot_message, parent, false))
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is BotViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUserMessage) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserMessage: TextView = itemView.findViewById(R.id.tvUserMessage)

        fun bind(message: ChatMessageDataClass) {
            tvUserMessage.text = message.text
        }
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBotMessage: TextView = itemView.findViewById(R.id.tvBotMessage)

        fun bind(message: ChatMessageDataClass) {
            tvBotMessage.text = message.text
        }
    }

    // ✅ FIX: Append messages correctly without splitting
    fun addMessage(newMessage: ChatMessageDataClass) {
        messages.add(newMessage)
        notifyItemInserted(messages.size - 1)
    }
}
