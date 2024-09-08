package com.example.musicplayerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.FragmentPlayBinding
import com.example.musicplayerapp.ui.adapter.SongPlayerViewPagerAdapter
import com.example.musicplayerapp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PlayFragment : Fragment() {

    private lateinit var binding: FragmentPlayBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    @Inject
    lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        lifecycleScope.launch {
            mainViewModel.uiState.collect{
                val viewPagerAdapter = SongPlayerViewPagerAdapter(requireActivity(), it.musicList, player)
                binding.songViewPager.adapter = viewPagerAdapter
                binding.songViewPager.setCurrentItem(it.currentMusicPosition, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navController.navigate(R.id.navigation_songs)
        }
    }

}