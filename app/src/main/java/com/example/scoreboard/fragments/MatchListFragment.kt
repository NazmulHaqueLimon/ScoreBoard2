package com.example.scoreboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.scoreboard.R
import com.example.scoreboard.viewmodels.MatchInfoViewModel

/**
 * A Fragment to display matchList that the user organized to score
 */
class MatchListFragment : Fragment() {
    private val viewModel: MatchInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // val binding =
        return inflater.inflate(R.layout.fragment_match_list, container, false)
    }

}