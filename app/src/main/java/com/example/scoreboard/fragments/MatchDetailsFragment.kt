package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.scoreboard.adapters.BatsmanScoreAdapter
import com.example.scoreboard.adapters.BowlerScoreAdapter
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.PlayerScoreAndPlayer
import com.example.scoreboard.databinding.FragmentMatchDetailsBinding
import com.example.scoreboard.viewmodels.MatchDetailsViewmodel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class MatchDetailsFragment : Fragment() {

    private lateinit var binding : FragmentMatchDetailsBinding
    private val detailsViewModel : MatchDetailsViewmodel by activityViewModels()
    private val args:MatchDetailsFragmentArgs by navArgs()
    private lateinit var batsmanScoreAdapter: BatsmanScoreAdapter
    private lateinit var bowlerScoreAdapter: BowlerScoreAdapter

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMatchDetailsBinding.inflate(inflater,container,false).apply {
            viewModel = detailsViewModel
            lifecycleOwner=viewLifecycleOwner

        }
        args.matchId.let {
            detailsViewModel.setMatchId(it)

        }

        batsmanScoreAdapter = BatsmanScoreAdapter()
        bowlerScoreAdapter = BowlerScoreAdapter()
        binding.batsmanRecyclerView.adapter = batsmanScoreAdapter
        binding.bowlersRecyclerView.adapter =bowlerScoreAdapter

        var teamAplayers = listOf<Player>()
        var teamBplayers = listOf<Player>()

        detailsViewModel.teamA.observe(viewLifecycleOwner){
            it.team.isBat.let { Bat->
                if (Bat){
                    detailsViewModel._battingTeamWithPlayers.value=it
                }
                else{
                    detailsViewModel._bowlingTeamWithPlayers.value =it
                }
            }
            teamAplayers =it.playerList
        }

        detailsViewModel.teamB.observe(viewLifecycleOwner){
            it.team.isBat.let { Bat->
                if (Bat){
                    detailsViewModel._battingTeamWithPlayers.value=it
                }
                else{
                    detailsViewModel._bowlingTeamWithPlayers.value=it
                }
            }
            teamBplayers =it.playerList
        }


        setAdapters(teamAplayers,teamBplayers)
        binding.materialCardView.setOnClickListener {
            setAdapters(teamAplayers,teamBplayers)
        }
        binding.materialCardView2.setOnClickListener {
            setAdapters(teamBplayers,teamAplayers)
        }


        detailsViewModel.matchTeamScoreTeam.observe(viewLifecycleOwner){
            it.teamScoreAndTeam.map { teamScoreTeam ->
                when(teamScoreTeam.team.isBat){
                    true -> {
                        detailsViewModel._battingTeamAndScore.value =teamScoreTeam
                    }
                    false -> {
                        detailsViewModel._bowlingTeamAndScore.value =teamScoreTeam
                    }
                }
            }
        }

        return binding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setAdapters(batsman: List<Player>,bowler:List<Player>) {

        val batsmanAndScore = mutableListOf<PlayerScoreAndPlayer>()
        val bowlerAndScore = mutableListOf<PlayerScoreAndPlayer>()
        detailsViewModel.matchPlayerScoreAndPlayers.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                it.playerScoreAndPlayer.map { playerAndScore->
                    batsman.map {player->
                        if (playerAndScore.player.id == player.id){
                            batsmanAndScore.add(playerAndScore)
                        }
                    }
                    bowler.map { player ->
                        if (playerAndScore.player.id == player.id){
                            bowlerAndScore.add(playerAndScore)
                        }
                    }

                }

            }
            batsmanScoreAdapter.submitList(batsmanAndScore)
            bowlerScoreAdapter.submitList(bowlerAndScore)
        }


    }
    companion object {
        private const val MATCH_ID_KEY = "MATCH_ID"
    }
}