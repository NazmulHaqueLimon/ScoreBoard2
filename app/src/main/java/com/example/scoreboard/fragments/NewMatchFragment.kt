package com.example.scoreboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.scoreboard.R
import com.example.scoreboard.databinding.FragmentNewMatchBinding
import com.example.scoreboard.viewmodels.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * A fragment to create new matches
 * takes team information and other inputs to start a match
 */
@AndroidEntryPoint
class NewMatchFragment : Fragment() {

    private lateinit var binding: FragmentNewMatchBinding
    private val matchViewModel: MatchViewModel by activityViewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with the binding object
        binding= FragmentNewMatchBinding.inflate(inflater,container,false).apply {
            viewModel=matchViewModel
            lifecycleOwner=viewLifecycleOwner

            teamACv.setOnClickListener {
                navigateToTeamFragment("A")
            }

            teamBCv.setOnClickListener {
                navigateToTeamFragment("B")
            }
            matchViewModel._ground.value = textInputGround.editText?.text.toString()


        }
        binding.createMatchButton.setOnClickListener {
            matchViewModel.createMatch()
            matchViewModel.savePlayers()
            matchViewModel.saveTeams()
            matchViewModel.saveTeamPlayers()
            matchViewModel.saveMatch()
            matchViewModel.createScoreSheet()
            navigateToScoringFragment()


        }


        return binding.root
    }


    override fun onResume() {
        super.onResume()
        setDropDowns()
    }

    private fun navigateToTeamFragment(flag: String) {
        matchViewModel._teamFlag.value =flag
        findNavController().navigate(R.id.action_newMatchFragment_to_teamFragment)
    }
    private fun navigateToScoringFragment() {
        matchViewModel.match.value?.let {
            val action = NewMatchFragmentDirections.actionNewMatchFragmentToScoringFragment(it.matchId)
            findNavController().navigate(action)
        }

    }


    private fun setDropDowns() {
        setOverSelection()
        val teamA = matchViewModel.teamA.value?.name.toString()
        val teamB= matchViewModel.teamB.value?.name.toString()
        //setting the dropdown options
        val teams = listOf(teamA, teamB)
        val teamsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, teams)
        (binding.tossSelection as? AutoCompleteTextView)?.setAdapter(teamsAdapter)

        binding.tossSelection.setOnItemClickListener{_, _, i, _ ->
            if(teamsAdapter.getItem(i).toString() == teamA){
                matchViewModel._toss.value =teamA
            }
            else if (teamsAdapter.getItem(i).toString() == teamB){
                matchViewModel._toss.value =teamB
            }
        }

        (binding.batFirst as? AutoCompleteTextView)?.setAdapter(teamsAdapter)
        binding.batFirst.setOnItemClickListener{_, _, i, _ ->
            if(teamsAdapter.getItem(i).toString() == teamA){
                matchViewModel._teamA.value?.isBat=true
            }
            else if(teamsAdapter.getItem(i).toString() == teamB){
                matchViewModel.teamB.value?.isBat=true
            }

        }
    }

    private fun setOverSelection() {
        val formats = listOf("10_Overs", "12_Overs", "15_Overs", "T_20")
        val oversAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, formats)
        (binding.formatSelection as? AutoCompleteTextView)?.setAdapter(oversAdapter)

        binding.formatSelection.setOnItemClickListener { _, _, i, _ ->
            when(oversAdapter.getItem(i)){
                "10_Overs" ->
                    matchViewModel._over.value = 10
                "12_Overs" ->
                    matchViewModel._over.value = 12
                "15_Overs" ->
                    matchViewModel._over.value  = 15
                "T_20" ->
                    matchViewModel._over.value = 20
            }
        }
        //parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
    }



}







