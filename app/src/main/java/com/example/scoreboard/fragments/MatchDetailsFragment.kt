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

    private  var teamAPlayers = mutableListOf<Player>()
    private  var teamBPlayers = mutableListOf<Player>()

    private  var teamAPlayersAndScores = mutableListOf<PlayerScoreAndPlayer>()
    private  var teamBPlayersAndScores = mutableListOf<PlayerScoreAndPlayer>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMatchDetailsBinding.inflate(inflater,container,false).apply {
            viewModel = detailsViewModel
            lifecycleOwner=viewLifecycleOwner

        }
        detailsViewModel.setMatchId(args.matchId)

        batsmanScoreAdapter = BatsmanScoreAdapter()
        bowlerScoreAdapter = BowlerScoreAdapter()
        binding.batsmanRecyclerView.adapter = batsmanScoreAdapter
        //binding.bowlersRecyclerView.adapter =bowlerScoreAdapter


        detailsViewModel.teamAWithPlayers.observe(viewLifecycleOwner){teamWithPlayers ->
            teamWithPlayers.team.isBat.let {
                if (!it){
                    detailsViewModel._bowlingTeamWithPlayers.value=teamWithPlayers
                }
                else{
                    detailsViewModel._battingTeamWithPlayers.value=teamWithPlayers
                }
            }
        }
        detailsViewModel.teamBWithPlayers.observe(viewLifecycleOwner){teamWithPlayers ->
            teamWithPlayers.team.isBat.let {
                if (!it){
                    detailsViewModel._bowlingTeamWithPlayers.value=teamWithPlayers
                }
                else{
                    detailsViewModel._battingTeamWithPlayers.value=teamWithPlayers
                }
            }
        }


        /**observing the match-team-teamScore to extract batting and bowling team scores*/
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
        detailsViewModel.matchPlayerScoreAndPlayer.observe(viewLifecycleOwner){
            it.playerScoreAndPlayer.let { playerAndScores ->
                detailsViewModel.selectBatsmanAndBowlerScores(playerAndScores)
            }
        }
        detailsViewModel.batsmanAndScores.observe(viewLifecycleOwner){
            batsmanScoreAdapter.submitList(it)
        }
        detailsViewModel.bowlerAndScores.observe(viewLifecycleOwner){
            bowlerScoreAdapter.submitList(it)
        }


        return binding.root
    }


    companion object {
        private const val MATCH_ID_KEY = "MATCH_ID"
    }
}