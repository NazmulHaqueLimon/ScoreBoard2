package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.objects.MatchTeamTeamScore
import com.example.scoreboard.databinding.MatchItemBinding
import com.example.scoreboard.fragments.MatchListFragmentDirections


class MatchListAdapter : ListAdapter<MatchTeamTeamScore, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MatchViewHolder(
            MatchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val match = getItem(position)
        (holder as MatchViewHolder).bind(match)
    }

    class MatchViewHolder(
        private val binding: MatchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MatchTeamTeamScore) {
            binding.apply {
                matchInfo = item.match

                executePendingBindings()
            }
        }


        private fun navigateToMatchDetails(
            matchId: String,
            view: View
        ) {
            val direction = MatchListFragmentDirections.actionMatchListFragmentToMatchDetailsFragment(matchId)
            view.findNavController().navigate(direction)
        }


    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<MatchTeamTeamScore>() {

    override fun areItemsTheSame(oldItem: MatchTeamTeamScore, newItem: MatchTeamTeamScore): Boolean {
        return oldItem.match.matchId == newItem.match.matchId
    }

    override fun areContentsTheSame(oldItem: MatchTeamTeamScore, newItem: MatchTeamTeamScore): Boolean {
        return oldItem == newItem
    }
}
