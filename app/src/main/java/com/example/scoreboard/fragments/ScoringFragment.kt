package com.example.scoreboard.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
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
    private val scoringViewModel : ScoringViewModel by viewModels()
    private val args:ScoringFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding =DataBindingUtil.inflate<FragmentScoringBinding>(inflater, R.layout.fragment_scoring, container, false).apply {
             viewModel =scoringViewModel
        }
        binding.lifecycleOwner =this

        scoringViewModel._matchId.value=args.matchId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val batsmanNames  =scoringViewModel.collectBatsmanNames()
        if (batsmanNames!=null){
            setBatsmansDropDown(batsmanNames)
        }
        val bowlerNames =scoringViewModel.collectBowlerNames()
        if (bowlerNames!=null){
            setBowlersDropDown(bowlerNames)
        }
    }
    override fun onResume() {
        super.onResume()
    }

    private fun setBatsmansDropDown(batsmanNames: List<String>) {
        val batsmanAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, batsmanNames)
        (binding.batsmanSelection1 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)

        binding.batsmanSelection1.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.battingTeam.value?.playerList?.forEach {player ->
                if (batsmanAdapter.getItem(i).equals(player.name)){
                    scoringViewModel._onStrike.value =player
                }
            }

        }
        (binding.batsmanSelection2 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)
        binding.batsmanSelection2.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.battingTeam.value?.playerList?.forEach {player ->
                if (batsmanAdapter.getItem(i).equals(player.name)){
                    scoringViewModel._nonStrike.value=player
                }
            }

        }
    }
    private fun setBowlersDropDown(bowlerNames:List<String>) {
        val bowlersAdapter =ArrayAdapter(requireContext(),R.layout.dropdown_item,bowlerNames)
        (binding.bowlerSelection as? AutoCompleteTextView)?.setAdapter(bowlersAdapter)

        binding.bowlerSelection.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.bowlingTeam.value?.playerList?.forEach {player ->
                if (bowlersAdapter.getItem(i).equals(player.name)){
                    scoringViewModel._bowler.value=player
                }
            }
        }
    }



}