package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.scoreboard.R
import com.example.scoreboard.adapters.PlayersAdapter
import com.example.scoreboard.databinding.FragmentTopPlayersBinding
import com.example.scoreboard.viewmodels.MatchInfoViewModel
import com.example.scoreboard.viewmodels.ScoringViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopPlayersFragment : Fragment() {

    private val viewModel:ScoringViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTopPlayersBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter =PlayersAdapter()
        binding.playerList.adapter =adapter

        viewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
            adapter.submitList(it.playerList)
        }

        return binding.root
    }

}