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
        scoringViewModel.setMatchId(args.matchId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoringViewModel.p.observe(viewLifecycleOwner){
            val batsmanNames = it.playerList.map { player ->
                player.name
            }
            setBatsmansDropDown(batsmanNames)
        }
        scoringViewModel.p2.observe(viewLifecycleOwner){
            val batsmanNames = it.playerList.map { player ->
                player.name
            }
            setBatsmansDropDown(batsmanNames)
        }

    }

    private fun setBatsmansDropDown(nameList: List<String>) {
        val batsmanAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nameList)

        (binding.batsmanSelection1 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)
        binding.batsmanSelection1.setOnItemClickListener{_, _, i, _ ->

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



}