package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.objects.PlayerScoreAndPlayer
import com.example.scoreboard.databinding.BatsmanScoreItemBinding
import com.example.scoreboard.databinding.BowlerScoreItemBinding


class BatsmanScoreAdapter : ListAdapter<PlayerScoreAndPlayer, RecyclerView.ViewHolder>(BatsManScoreDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerScoreViewHolder(
            BatsmanScoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as PlayerScoreViewHolder).bind(item)
    }



    class PlayerScoreViewHolder(
        private val binding: BatsmanScoreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayerScoreAndPlayer) {
            binding.apply {
                batsman =item.player
                batsmanScore =item.score
                executePendingBindings()
            }
        }
    }

}

private class BatsManScoreDiffUtilCallback : DiffUtil.ItemCallback<PlayerScoreAndPlayer>() {

    override fun areItemsTheSame(oldItem: PlayerScoreAndPlayer, newItem: PlayerScoreAndPlayer): Boolean {
        return oldItem.player.playerId == newItem.player.playerId
    }

    override fun areContentsTheSame(oldItem: PlayerScoreAndPlayer, newItem: PlayerScoreAndPlayer): Boolean {
        return oldItem == newItem
    }
}