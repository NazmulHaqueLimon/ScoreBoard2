package com.example.scoreboard.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.Team
import com.example.scoreboard.databinding.FragmentTeamBinding
import com.example.scoreboard.viewmodels.MatchViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 *this fragment adds or delete players for a match.edit team information
 */
@AndroidEntryPoint
class TeamFragment : Fragment() {

    private lateinit var binding: FragmentTeamBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private lateinit var adapter: TeamPlayersAdapter
    private val isTeamAdded =false
    private val args:TeamFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentTeamBinding.inflate(inflater,container,false)
        context ?: return binding.root

        adapter = TeamPlayersAdapter()
        binding.playerListRecycler.adapter = adapter

        val playerList = ArrayList<Player>()
        //val playerList = mutableListOf<Player>()

        binding.buttonAdd.setOnClickListener {
            val playerName = binding.playerName.text.toString()
            if (playerName.isNullOrEmpty()){
                showToastMessage("please provide a player name")
            }
            else{
                val newPlayer =Player(name = playerName)
                playerList.add(newPlayer)
            }
            adapter.submitList(playerList.toMutableList())
            binding.playerName.text = null
            hideSoftKeyboard(it)
        }

        binding.saveTramFab.setOnClickListener {
            val teamName = binding.teamName.text.toString()
            if (teamName.isNullOrEmpty()){
                showToastMessage("please add a team name")
            }else{
                val team = Team(name = teamName)
                if (playerList.size in 2..11){
                    viewModel.collectTeamAndPlayers(team , playerList)
                    when(args.teamCode){
                        "A" -> viewModel.teamAadded =true
                        "B" -> viewModel.teamBadded =true
                    }

                    findNavController().navigateUp()
                }else{
                    showToastMessage("teams must contain at least 6 players")
                }
            }

        }

        return binding.root
    }


    private fun showMessage(message: String?) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            "" + message,
            Snackbar.LENGTH_LONG
        ).show()
    }
    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }


}
