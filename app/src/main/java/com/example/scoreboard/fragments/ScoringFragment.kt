package com.example.scoreboard.fragments

import android.annotation.SuppressLint
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
import androidx.appcompat.widget.PopupMenu
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
        setListPopupOut()
        setListPopupLb()
        setListPopupBye()
        setListPopupNb()
        setListPopupWide()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MATCH_ID_KEY,args.matchId)
    }

    @ExperimentalCoroutinesApi
    private fun observeTeamAndPlayers(){
        scoringViewModel.battingTeamWithPlayers.observe(viewLifecycleOwner){
            val batsmanNames = it.playerList.map { player ->
                if (!player.isOut){
                    player.name
                }
                else{
                    "Out"
                }

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

            //create the scoreSheet if not created once
            if (scoringViewModel.isScoreSheetCreated.value == false){
                it.playerList.map { player->
                    scoringViewModel.openPlayerScoreSheet(player.id)
                }
                scoringViewModel.openTeamScoreSheet(it.team.teamId)

            }

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

    private fun setListPopupOut(){
        val listPopupWindowOut = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        val items = listOf("Bold", "LBW", "Caught","RunOut")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        listPopupWindowOut.anchorView =binding.popupOut
        listPopupWindowOut.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowOut.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position).toString()){
                "Bold"->
                    scoringViewModel.onStrikerOut()
                "LBW" ->
                    scoringViewModel.onStrikerOut()
                "Caught" ->
                    scoringViewModel.onStrikerOut()
                "RunOut" ->
                    openPlayersPopup()
            }
            // Dismiss popup.
           // listPopupWindowOut.dismiss()
        }
        binding.popupOut.setOnClickListener { listPopupWindowOut.show() }
    }
    private fun openPlayersPopup(){
        val popupPlayers = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        // Set list popup's content
        var batsmanA :String ="BatsmanA"
        scoringViewModel.batsmanA.observe(viewLifecycleOwner){
            batsmanA =it.name
        }
        var batsmanB :String ="BatsmanB"
        scoringViewModel.batsmanB.observe(viewLifecycleOwner){
            batsmanB=it.name
        }
        // Set list popup's content
        val items = listOf(batsmanA, batsmanB)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        popupPlayers.anchorView =binding.popupOut
        popupPlayers.setAdapter(adapter)

        // Set list popup's item click listener
        popupPlayers.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position)){
                batsmanA->
                    scoringViewModel.onBatsmanAOut()
                batsmanB ->
                    scoringViewModel.onBatsmanBOut()
            }
            // Dismiss popup.
            popupPlayers.dismiss()
        }
        popupPlayers.show()
    }
    @SuppressLint("ResourceAsColor")
    private fun openScoringOptions(view: View, type:String){
        val listPopupScores = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        // Set list popup's content
        val items = listOf("+1", "+2", "+3","+4","+6")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        when(type){
            "nbBAT" ->{
                listPopupScores.anchorView =binding.popupNb
            }
            "nbBYE" ->{
                listPopupScores.anchorView =binding.popupBye
            }
            "nbLB" ->{
                listPopupScores.anchorView =binding.popupLb
            }
            "LB" ->{
                listPopupScores.anchorView =binding.popupLb
            }

            "wideBYE" ->{
                listPopupScores.anchorView =binding.popupBye
            }
            "BYE" ->{
                listPopupScores.anchorView =binding.popupBye
            }

        }

        listPopupScores.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupScores.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position)){
                "+1" ->
                    scoringViewModel.updateExtra(1,type)
                "+2" ->
                    scoringViewModel.updateExtra(2,type)
                "+3" ->
                    scoringViewModel.updateExtra(3,type)
                "+4" ->
                    scoringViewModel.updateExtra(4,type)
                "+6" ->
                    scoringViewModel.updateExtra(6,type)

            }
            // Dismiss popup.
            listPopupScores.dismiss()
        }

       listPopupScores.show()

    }


    private fun setListPopupWide(){
        val listPopupWindowWide = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        // Set list popup's content
        val items = listOf("+O", "+Bye")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        listPopupWindowWide.anchorView =binding.popupWide
        listPopupWindowWide.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowWide.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position)){
                "+0" ->
                    scoringViewModel.updateExtra(1,"EXTRA")
                "Bye" ->
                    view?.let { openScoringOptions(it,"wideBYE") }

            }
            // Dismiss popup.
            listPopupWindowWide.dismiss()
        }

        binding.popupWide.setOnClickListener { listPopupWindowWide.show() }
    }

    private fun setListPopupBye(){
        binding.popupBye.setOnClickListener {
            openScoringOptions(it,"BYE")
        }
    }
    private fun setListPopupLb(){

        binding.popupLb.setOnClickListener {
            openScoringOptions(it ,"LB")
        }

    }
    private fun setListPopupNb(){
        val listPopupWindowNb = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)

        // Set button as the list popup's anchor
        listPopupWindowNb.anchorView = binding.popupNb

        // Set list popup's content
        val items = listOf("+O", "+Lb", "+bat")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        listPopupWindowNb.setAdapter(adapter)
        // Set list popup's item click listener
        listPopupWindowNb.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position)){
                "+O" ->
                    scoringViewModel.updateExtra(1,"EXTRA")
                "+Lb" ->
                    view?.let { openScoringOptions(it,"nbLB") }
                "+bat" ->
                    view?.let { openScoringOptions(it,"nbBAT") }


            }
            // Dismiss popup.
            listPopupWindowNb.dismiss()
        }

        // Show list popup window on button click.
        binding.popupNb.setOnClickListener { listPopupWindowNb.show() }


    }

    companion object {
        private const val MATCH_ID_KEY = "MATCH_ID"
    }

}