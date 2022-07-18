package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.databinding.PlayerItemBinding


class PlayersAdapter :ListAdapter<Player, RecyclerView.ViewHolder>(PlayerDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            PlayerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val player = getItem(position)
        (holder as PlayerViewHolder).bind(player)
    }



    class PlayerViewHolder(
        private val binding: PlayerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Player) {
            binding.apply {
                player = item
                executePendingBindings()
            }
        }
    }

}

private class PlayerDiffUtilCallback : DiffUtil.ItemCallback<Player>() {

    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.playerId == newItem.playerId
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }
}