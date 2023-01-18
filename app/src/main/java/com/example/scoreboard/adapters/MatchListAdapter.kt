package com.example.scoreboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboard.data.entityObjects.MatchTeamScoreTeam
import com.example.scoreboard.databinding.MatchItemBinding
import com.example.scoreboard.fragments.MatchListFragmentDirections


class MatchListAdapter : ListAdapter<MatchTeamScoreTeam, RecyclerView.ViewHolder>(PlantDiffCallback()) {

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


        fun bind(item: MatchTeamScoreTeam) {
            binding.apply {
                matchInfo = item.match
                scoreAndTeamA = item.teamScoreAndTeam[0]
                scoreAndTeamB =item.teamScoreAndTeam[1]
                setClickListener {
                    item.match.matchId.let { id->
                        navigateToMatchDetails(id,it)
                    }
                }
                executePendingBindings()
            }
        }


        private fun navigateToMatchDetails(
            id: String,
            view: View
        ) {
            val direction = MatchListFragmentDirections.actionMatchListFragmentToMatchDetailsFragment(id)
            view.findNavController().navigate(direction)
        }


    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<MatchTeamScoreTeam>() {

    override fun areItemsTheSame(oldItem: MatchTeamScoreTeam, newItem: MatchTeamScoreTeam): Boolean {
        return oldItem.match.matchId == newItem.match.matchId
    }

    override fun areContentsTheSame(oldItem: MatchTeamScoreTeam, newItem: MatchTeamScoreTeam): Boolean {
        return oldItem == newItem
    }
}
