package com.example.scoreboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scoreboard.R
import com.example.scoreboard.adapters.MatchListAdapter
import com.example.scoreboard.adapters.MatchStateAdapter
import com.example.scoreboard.data.MatchState
import com.example.scoreboard.databinding.FragmentScoringBinding
import com.example.scoreboard.viewmodels.ScoringViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Fragment that takes and display live scoring inputs
 */
@AndroidEntryPoint
class ScoringFragment : Fragment() {

    private lateinit var binding : FragmentScoringBinding
    private val scoringViewModel : ScoringViewModel by activityViewModels()
    private val args:ScoringFragmentArgs by navArgs()
    private lateinit var matchStateAdapter: MatchStateAdapter

    val stateList = ArrayList<MatchState>()
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentScoringBinding.inflate(inflater,container,false).apply {
            viewModel=scoringViewModel
            lifecycleOwner=viewLifecycleOwner

            if (batsmanSelection1.isSelected){
                scoringViewModel._strikerActive.value=true
            }
            if (batsmanSelection2.isSelected){
                scoringViewModel._nonStrikerActive.value=true
            }
            if (bowlerSelection.isSelected){
                scoringViewModel._bowlerActive.value=true
            }
        }
        args.matchId.let { id ->
            if (id == "NO_ID"){
                savedInstanceState?.getString(MATCH_ID_KEY)?.let {
                    scoringViewModel.setMatchId(it)
                }
            }else{
                scoringViewModel.setMatchId(args.matchId)

            }
        }
        /**
         *setting the batsmans and bowlers dropdown with value and click events */
        setBatsmanDropDown()
        setBowlerDropDown()
        //scoringViewModel.createScoreSheet()
        /**
         * */
        scoringViewModel.teamA.observe(viewLifecycleOwner){
            it.team.isBat.let { Bat->
                if (Bat){
                    scoringViewModel._battingTeamWithPlayers.value=it
                }
                else{
                    scoringViewModel._bowlingTeamWithPlayers.value =it
                }
            }
        }
        scoringViewModel.teamB.observe(viewLifecycleOwner){
            it.team.isBat.let { Bat->
                if (Bat){
                    scoringViewModel._battingTeamWithPlayers.value=it
                }
                else{
                    scoringViewModel._bowlingTeamWithPlayers.value=it
                }
            }
        }
        scoringViewModel.isFirstInningsDone.observe(viewLifecycleOwner){
            if (it){
                scoringViewModel.inningsBreak()
                setBatsmanDropDown()
                setBowlerDropDown()
            }
        }
        scoringViewModel.battingTeamScore.observe(viewLifecycleOwner){
            val over =it.ballPlayed/6
            scoringViewModel.match.value?.let { match ->
                if ( over == match.format ){
                    scoringViewModel._isFirstInningsDone.value=true
                }
            }

            //Log.d("LaunchList", "Success $over")
        }
        binding.arrowBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        matchStateAdapter = MatchStateAdapter()
        binding.matchStateRv.adapter = matchStateAdapter
        scoringViewModel.stateList.let {
            if(it.isNotEmpty()){
                matchStateAdapter.submitList(scoringViewModel.stateList)
            }
        }




        return binding.root
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListPopupOut()
        setListPopupLb()
        setListPopupBye()
        setListPopupNb()
        setListPopupWide()
        showDialog("please add striker,non striker and a bowler to start scoring")
        displayMatchState()

    }

    private fun displayMatchState() {


    }
    private fun showDialog(msg:String){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(msg)
            .setMessage(msg)

            .setPositiveButton("Okey") { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MATCH_ID_KEY,args.matchId)
    }

    @ExperimentalCoroutinesApi
    private fun setBatsmanDropDown() {

        scoringViewModel.batsmans.observe(viewLifecycleOwner){ players ->
            val batsmanNames = mutableListOf<String>()
            players.map { player ->
                if (!player.isOut){
                    batsmanNames.add(player.name)
                }
                else{
                    binding.batsmanSelection1.showDropDown()
                }
            }
            val batsmanAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, batsmanNames)
            (binding.batsmanSelection1 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)
            (binding.batsmanSelection2 as? AutoCompleteTextView)?.setAdapter(batsmanAdapter)

            binding.batsmanSelection1.setOnItemClickListener{_, _, i, _ ->
                players.map { player ->
                    if (batsmanAdapter.getItem(i).equals(player.name)){
                        scoringViewModel._batsmanA.value=player
                        scoringViewModel._strikerActive.value =true
                        Toast.makeText(requireContext(),"player updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.batsmanSelection2.setOnItemClickListener{_, _, i, _ ->
                players.map { player ->
                    if (batsmanAdapter.getItem(i).equals(player.name)){
                        scoringViewModel._batsmanB.value=player
                        scoringViewModel._nonStrikerActive.value =true
                        Toast.makeText(requireContext(),"another batsman steps on the crease ready", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
    @ExperimentalCoroutinesApi
    private fun setBowlerDropDown() {
        scoringViewModel.bowlers.observe(viewLifecycleOwner){ players ->
            val nameList = mutableListOf<String>()
            players.map { player ->
                nameList.add(player.name)
            }
            val bowlersAdapter =ArrayAdapter(requireContext(),R.layout.dropdown_item,nameList)
            (binding.bowlerSelection as? AutoCompleteTextView)?.setAdapter(bowlersAdapter)

            binding.bowlerSelection.setOnItemClickListener{_, _, i, _ ->
                players.map { player ->
                    if (bowlersAdapter.getItem(i).equals(player.name)) {
                        scoringViewModel._bowler.value = player
                        scoringViewModel._bowlerActive.value =true
                        Toast.makeText(requireContext(), "Bowler ready to deliver", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setListPopupOut(){
        val listPopupWindowOut = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)
        val items = listOf("Bold", "LBW", "Caught","RunOut")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)

        binding.popupOut.setOnClickListener {

            listPopupWindowOut.setAdapter(adapter)
            listPopupWindowOut.anchorView =binding.popupOut
            listPopupWindowOut.show()

            if (listPopupWindowOut.isShowing){
                listPopupWindowOut.dismiss()
            }
        }

        // Set list popup's item click listener
        listPopupWindowOut.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            when(adapter.getItem(position).toString()){
                "Bold"->{
                    scoringViewModel.onStrikerOut()
                    setBatsmanDropDown()
                }

                "LBW" ->{
                    scoringViewModel.onStrikerOut()
                    setBatsmanDropDown()
                }
                "Caught" ->{
                    scoringViewModel.onStrikerOut()
                    setBatsmanDropDown()
                }
                "RunOut" ->{
                    openPlayersPopup()
                }

            }

            listPopupWindowOut.dismiss()
        }

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun openPlayersPopup(){
        val popupPlayers = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)
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
                batsmanA->{
                    scoringViewModel.onBatsmanAOut()
                    setBatsmanDropDown()
                }
                batsmanB ->{
                    scoringViewModel.onBatsmanBOut()
                    setBatsmanDropDown()
                }
            }
            // Dismiss popup.
            popupPlayers.dismiss()
        }
        popupPlayers.show()
    }

    private fun openScoringOptions(state: MatchState){
        val listPopupScores = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)
        // Set list popup's content
        val items = listOf("+1", "+2", "+3","+4","+6")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        var newState: MatchState? =null

        if (state.nb){
            if (state.lb){
                newState = MatchState(nb = true, lb = true, extra = 1)
                listPopupScores.anchorView =binding.popupNb
            }
            else if(state.bye){
                newState =MatchState(nb = true, bye = true, extra = 1)
                listPopupScores.anchorView =binding.popupNb
            }
            else if (state.bat){
                newState =MatchState(nb = true, bat = true, extra = 1)
                listPopupScores.anchorView =binding.popupNb
            }else{
                newState = MatchState(nb = true, extra = 1)
            }
        }
         // counted the ball number in the viewmodel
         else if (state.lb){
            newState =MatchState(lb = true)
            listPopupScores.anchorView =binding.popupLb

        }
         else if (state.wide){
             newState =state
             listPopupScores.anchorView =binding.popupBye
        }
        //counted the ball number in the viewmodel
        else if (state.bye){
            newState =MatchState(bye = true)
            listPopupScores.anchorView =binding.popupBye
        }

        listPopupScores.setAdapter(adapter)
        // Set list popup's item click listener
        listPopupScores.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            when(adapter.getItem(position)){
                "+1" -> newState?.let { updateState(it,1) }
                "+2" -> newState?.let { updateState(it,2) }
                "+3" -> newState?.let { updateState(it,3) }
                "+4" -> newState?.let { updateState(it,4) }
                "+6" -> newState?.let { updateState(it,6) }
            }

            listPopupScores.dismiss()
        }
       listPopupScores.show()
    }
    private fun updateState(state: MatchState, run:Int){
        val  lastState = if (state.bat){
                state.copy(run_bat = run)
            }else {
                 state.copy(run = run)
            }
        scoringViewModel.stateList.add(lastState)
        scoringViewModel.updateMatchState(lastState)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setListPopupWide(){
        val listPopupWindowWide = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)

        // Set list popup's content
        val items = listOf("+O", "+Bye")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        listPopupWindowWide.anchorView =binding.popupWide
        listPopupWindowWide.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowWide.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            val state :MatchState
            when(adapter.getItem(position)){
                "+0" ->{
                    state = MatchState( wide = true, extra = 1)
                    scoringViewModel.updateMatchState(state)
                }
                "+Bye" -> {
                    state = MatchState( bye = true, wide = true, extra = 1)
                    openScoringOptions(state)
                }
            }

            // Dismiss popup.
            listPopupWindowWide.dismiss()
        }
        binding.popupWide.setOnClickListener {
            if (listPopupWindowWide.isShowing){
                listPopupWindowWide.dismiss()
            }else{
                listPopupWindowWide.show()
            }

        }
    }

    private fun setListPopupBye(){
        binding.popupBye.setOnClickListener {
            val state = MatchState( bye = true, ballCount = true)
            openScoringOptions(state)
        }
    }
    private fun setListPopupLb(){
        binding.popupLb.setOnClickListener {
            val state = MatchState( lb = true, ballCount = true)
            openScoringOptions(state)
        }
    }
    @ExperimentalCoroutinesApi
    private fun setListPopupNb(){
        val listPopupWindowNb = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)

        // Set button as the list popup's anchor
        listPopupWindowNb.anchorView = binding.popupNb

        // Set list popup's content
        val items = listOf("+O", "+Lb","+bye", "+bat")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        listPopupWindowNb.setAdapter(adapter)
        // Set list popup's item click listener
        listPopupWindowNb.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            val state :MatchState
            when(adapter.getItem(position)){
                "+O" ->{
                    state = MatchState(nb = true,extra = 1)
                    scoringViewModel.updateMatchState(state)
                    //scoringViewModel.updateExtra(1,"EXTRA")
                }
                "+Lb" ->{
                    state = MatchState(nb = true,extra = 1, lb = true)
                    openScoringOptions(state)
                }
                "+bye" ->{
                    state = MatchState(nb = true,extra = 1, bye = true)
                    openScoringOptions(state)
                }
                "+bat" ->{
                    state = MatchState(nb = true, extra = 1, bat = true)
                    openScoringOptions(state)
                }

            }
            // Dismiss popup.
           // listPopupWindowNb.dismiss()
        }
        // Show list popup window on button click.
        binding.popupNb.setOnClickListener {
            if (listPopupWindowNb.isShowing){
                listPopupWindowNb.dismiss()
            }else{
                listPopupWindowNb.show()
            }

        }

    }

    companion object {
        private const val MATCH_ID_KEY = "match_id"
    }

}