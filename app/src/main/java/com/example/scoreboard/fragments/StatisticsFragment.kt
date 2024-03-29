package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.scoreboard.adapters.PlayersAdapter
import com.example.scoreboard.databinding.FragmentStatisticBinding
import com.example.scoreboard.viewmodels.ScoringViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val viewModel:ScoringViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStatisticBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter =PlayersAdapter()
        binding.playerList.adapter =adapter

        viewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
            adapter.submitList(it.playerList)
        }

        return binding.root
    }

}