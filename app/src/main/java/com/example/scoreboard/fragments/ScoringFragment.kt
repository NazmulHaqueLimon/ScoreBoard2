package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContentProviderCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.scoreboard.R
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.databinding.FragmentNewMatchBinding
import com.example.scoreboard.databinding.FragmentScoringBinding
import com.example.scoreboard.viewmodels.ScoringViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

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
        binding= FragmentScoringBinding.inflate(inflater,container,false).apply {
            viewModel=scoringViewModel
            lifecycleOwner=viewLifecycleOwner
            args.matchId.let { id ->
                if (id == "NO_ID"){
                    savedInstanceState?.getString(MATCH_ID_KEY)?.let {
                        scoringViewModel.setMatchId(it)
                    }
                }else{
                    scoringViewModel.setMatchId(args.matchId)

                }
            }

        }


        return binding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeTeamAndPlayers()
        setNoBallPopup()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MATCH_ID_KEY,args.matchId)
    }

    @ExperimentalCoroutinesApi
    fun observeTeamAndPlayers(){
        scoringViewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
            val batsmanNames = it.playerList.map { player ->
                player.name
            }
            setBatsmanDropDown(batsmanNames)
            //create the scoreSheet if not created once
            if (scoringViewModel.isScoreSheetCreated.value == false){
                it.playerList.map { player->
                    scoringViewModel.openPlayerScoreSheet(player.id)
                }
                scoringViewModel.openTeamScoreSheet(it.team.teamId)

            }

        }
        scoringViewModel.bowlingTeamWithPlayers.observe(viewLifecycleOwner){
            //val bowlersNames = mutableListOf<String>()
            val bowlersNames=it.playerList.map { player ->
                player.name
            }
            setBowlerDropDown(bowlersNames)

            it.playerList.map { player->
                scoringViewModel.openPlayerScoreSheet(player.id)
            }
            scoringViewModel.openTeamScoreSheet(it.team.teamId)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setBatsmanDropDown(playerList: List<String>) {

        val batsmanAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, playerList)

        (binding.batsmanSelection1 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)

        binding.batsmanSelection1.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
                it.playerList.map { player ->
                    if (batsmanAdapter.getItem(i).equals(player.name)){
                        scoringViewModel._batsmanA.value=player
                        Toast.makeText(requireContext(),"player updated", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }

        (binding.batsmanSelection2 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)

        binding.batsmanSelection2.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
                it.playerList.map { player ->
                    if (batsmanAdapter.getItem(i).equals(player.name)){
                        scoringViewModel._batsmanB.value=player
                        Toast.makeText(requireContext(),"another batsman steps on the crease ready", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
    }
    @ExperimentalCoroutinesApi
    private fun setBowlerDropDown(nameList: List<String>) {
        val bowlersAdapter =ArrayAdapter(requireContext(),R.layout.dropdown_item,nameList)
        (binding.bowlerSelection as? AutoCompleteTextView)?.setAdapter(bowlersAdapter)

        binding.bowlerSelection.setOnItemClickListener{_, _, i, _ ->
            scoringViewModel.bowlingTeamWithPlayers.observe(viewLifecycleOwner){
                it.playerList.map { player ->
                    if (bowlersAdapter.getItem(i).equals(player.name)){
                        scoringViewModel._bowler.value=player
                        Toast.makeText(requireContext(),"Bowler ready to deliver", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
    }

    fun setNoBallPopup(){
        // val listPopupWindowButton = view.findViewById<Button>(R.id.list_popup_button)
        val listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        // Set button as the list popup's anchor
        listPopupWindow.anchorView = binding.popupNb
        listPopupWindow.anchorView =binding.popupLb
        listPopupWindow.anchorView =binding.popupOut
        listPopupWindow.anchorView =binding.popupWide

        // Set list popup's content
        val items = listOf("+O", "+Lb", "+bat")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        listPopupWindow.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.

            // Dismiss popup.
            // listPopupWindow.dismiss()
        }
        // Show list popup window on button click.
        binding.popupNb.setOnClickListener { listPopupWindow.show() }
        binding.popupLb.setOnClickListener { listPopupWindow.show() }
        binding.popupOut.setOnClickListener { listPopupWindow.show() }
        binding.popupWide.setOnClickListener { listPopupWindow.show() }
        //listPopupWindowButton.setOnClickListener { v: View? -> listPopupWindow.show() }

    }

    companion object {
        private const val MATCH_ID_KEY = "MATCH_ID"
    }

}