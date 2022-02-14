package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.scoreboard.R
import com.example.scoreboard.adapters.MatchListAdapter
import com.example.scoreboard.databinding.FragmentMatchListBinding
import com.example.scoreboard.databinding.FragmentNewMatchBinding
import com.example.scoreboard.viewmodels.MatchInfoViewModel

/**
 * A Fragment to display matchList that the user organized to score
 */
class MatchListFragment : Fragment() {
    private val viewModel: MatchInfoViewModel by activityViewModels()
    private lateinit var binding: FragmentMatchListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMatchListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        //viewModel.createMatchInfoList()

        val adapter = MatchListAdapter()
        binding.matchList.adapter = adapter
        viewModel.matchTeamScoreList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }



        return binding.root
    }


}