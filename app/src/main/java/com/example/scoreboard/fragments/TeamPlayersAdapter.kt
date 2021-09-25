package com.example.scoreboard.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.databinding.PlayerItemBinding

class TeamPlayersAdapter : ListAdapter<Player, RecyclerView.ViewHolder>(PlayerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val playerItem = getItem(position)
        (holder as PlantViewHolder).bind(playerItem)
    }

    class PlantViewHolder(
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

private class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {

    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }
}
