package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.entityObjects.MatchState
import com.example.scoreboard.databinding.ScoreItemBinding

class MatchStateAdapter : ListAdapter<MatchState, RecyclerView.ViewHolder>(MatchStateDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            ScoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val state = getItem(position)
        (holder as PlayerViewHolder).bind(state)
    }



    class PlayerViewHolder(
        private val binding: ScoreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MatchState) {
            binding.apply {
                currentScoreItem.text = item.getScoreString()
                //player = item
                executePendingBindings()
            }
        }
    }

}

private class MatchStateDiffUtilCallback : DiffUtil.ItemCallback<MatchState>() {

    override fun areItemsTheSame(oldItem: MatchState, newItem: MatchState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MatchState, newItem: MatchState): Boolean {
        return oldItem == newItem
    }
}