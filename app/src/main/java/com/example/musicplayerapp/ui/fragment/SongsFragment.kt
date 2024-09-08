package com.example.musicplayerapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.model.Music
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.FragmentSongsBinding
import com.example.musicplayerapp.ui.adapter.SongListAdapter
import com.example.musicplayerapp.ui.viewmodel.MainViewModel
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SongsFragment : Fragment() {

    private lateinit var binding: FragmentSongsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var songList: MutableList<Music> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val mainSongAdapter = SongListAdapter(mutableListOf()){
            mainViewModel.setCurrentSongPosition(it)
            navController.navigate(R.id.navigation_play)
        }
        val searchSongAdapter = SongListAdapter(mutableListOf()){
            mainViewModel.setCurrentSongPosition(it)
            navController.navigate(R.id.navigation_play)
        }


        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = mainSongAdapter
            mainSongAdapter.notifyDataSetChanged()
        }
        binding.searchRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = searchSongAdapter
            mainSongAdapter.notifyDataSetChanged()
        }
        binding.searchView.editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                searchSongAdapter.updateSongList(songList.filter { it.title.lowercase().contains(query) })
            }

            override fun afterTextChanged(s: Editable?) { }

        })
        binding.searchView.addTransitionListener{ searchView, _, newState ->
            if (newState == SearchView.TransitionState.HIDING) {
                searchView.clearText()
                searchSongAdapter.updateSongList(emptyList())
            }
        }


        lifecycleScope.launch {
            mainViewModel.uiState.collect{
                if (it.isLoading){
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                else {
                    binding.loadingLayout.visibility = View.GONE
                }
                mainSongAdapter.updateSongList(it.musicList)
                songList.clear()
                songList.addAll(it.musicList)
            }
        }
    }
}