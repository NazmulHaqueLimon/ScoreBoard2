package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.entityObjects.PlayerScoreAndPlayer
import com.example.scoreboard.databinding.BowlerScoreItemBinding


class BowlerScoreAdapter : ListAdapter<PlayerScoreAndPlayer, RecyclerView.ViewHolder>(BowlerScoreDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BowlerScoreViewHolder(
            BowlerScoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as BowlerScoreViewHolder).bind(item)
    }

    class BowlerScoreViewHolder(
        private val binding: BowlerScoreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayerScoreAndPlayer) {
            binding.apply {
                bowler =item.player
                bowlerScore =item.score
                executePendingBindings()
            }
        }
    }

}
private class BowlerScoreDiffUtilCallback : DiffUtil.ItemCallback<PlayerScoreAndPlayer>() {

    override fun areItemsTheSame(oldItem: PlayerScoreAndPlayer, newItem: PlayerScoreAndPlayer): Boolean {
        return oldItem.player.playerId == newItem.player.playerId
    }

    override fun areContentsTheSame(oldItem: PlayerScoreAndPlayer, newItem: PlayerScoreAndPlayer): Boolean {
        return oldItem == newItem
    }
}

