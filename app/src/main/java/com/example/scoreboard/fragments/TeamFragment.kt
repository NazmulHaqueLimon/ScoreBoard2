package com.example.scoreboard.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.Team
import com.example.scoreboard.databinding.FragmentTeamBinding
import com.example.scoreboard.viewmodels.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *this fragment adds or delete players for a match.edit team information
 */
@AndroidEntryPoint
class TeamFragment : Fragment() {

    private lateinit var binding: FragmentTeamBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private lateinit var adapter: TeamPlayersAdapter

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
            val newPlayer =Player(name = playerName)
            playerList.add(newPlayer)

            adapter.submitList(playerList.toMutableList())
            //adapter.submitList(playerList)
            binding.playerName.text = null
            hideSoftKeyboard(it)
        }

        binding.saveTramFab.setOnClickListener {
            val teamName = binding.teamName.text.toString()
            val team = Team(name = teamName)
            viewModel.collectTeamAndPlayers(team , playerList)
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        false
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }


}
