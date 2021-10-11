package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.scoreboard.R
import com.example.scoreboard.databinding.FragmentScoringBinding
import com.example.scoreboard.viewmodels.ScoringViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that takes and display live scoring inputs
 */
@AndroidEntryPoint
class ScoringFragment : Fragment() {

    private lateinit var binding : FragmentScoringBinding
    private val scoringViewModel : ScoringViewModel by activityViewModels()
    private val args:ScoringFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding =DataBindingUtil.inflate<FragmentScoringBinding>(inflater, R.layout.fragment_scoring, container, false).apply {
             viewModel =scoringViewModel
        }
        binding.lifecycleOwner =this
        args.matchId.let { id ->
            if (id == "NO_ID"){
                savedInstanceState?.getString(MATCH_ID_KEY)?.let {
                    scoringViewModel.setMatchId(it)
                }
            }else{
                scoringViewModel.setMatchId(args.matchId)
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MATCH_ID_KEY,args.matchId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoringViewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
            val batsmanNames = it.playerList.map { player ->
                player.name
            }
            setBatsmansDropDown(batsmanNames)
        }
        scoringViewModel.bowlingTeamWithPlayers.observe(viewLifecycleOwner){
            //val bowlersNames = mutableListOf<String>()
            val bowlersNames=it.playerList.map { player ->
                player.name
            }
            setBowlersDropDown(bowlersNames)
        }

    }

    private fun setBatsmansDropDown(nameList: List<String>) {
        val batsmanAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nameList)

        (binding.batsmanSelection1 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)
        binding.batsmanSelection1.setOnItemClickListener{_, _, i, _ ->

        }
        (binding.batsmanSelection2 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)
        binding.batsmanSelection2.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.battingTeamWithPlayers.value?.playerList?.forEach { player ->
                if (batsmanAdapter.getItem(i).equals(player.name)){
                    scoringViewModel._nonStrike.value=player
                }
            }
        }
    }
    private fun setBowlersDropDown(nameList: List<String>) {
        val bowlersAdapter =ArrayAdapter(requireContext(),R.layout.dropdown_item,nameList)
        (binding.bowlerSelection as? AutoCompleteTextView)?.setAdapter(bowlersAdapter)

        binding.bowlerSelection.setOnItemClickListener{_, _, i, _ ->
            //scoringViewModel.bowlingTeam.value?.playerList?.forEach {player ->
                //if (bowlersAdapter.getItem(i).equals(player.name)){
                   // scoringViewModel._bowler.value=player
                //}
            //}
        }
    }

    companion object {
        private const val MATCH_ID_KEY = "MATCH_ID"
    }

}