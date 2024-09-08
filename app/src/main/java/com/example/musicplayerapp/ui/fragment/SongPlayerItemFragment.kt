package com.example.musicplayerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.model.Music
import com.example.musicplayerapp.databinding.FragmentSongPlayerItemBinding


class SongPlayerItemFragment(private val music: Music, private val player: ExoPlayer) : Fragment() {

    private lateinit var binding: FragmentSongPlayerItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongPlayerItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerView.player = player
        player.setMediaItem(MediaItem.fromUri(music.filePath))
        player.prepare()
        player.play()
    }
}