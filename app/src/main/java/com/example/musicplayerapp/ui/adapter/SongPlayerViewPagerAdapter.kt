package com.example.musicplayerapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.data.model.Music
import com.example.musicplayerapp.ui.fragment.SongPlayerItemFragment

class SongPlayerViewPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val musicList: List<Music>,
    private var player: ExoPlayer
): FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return SongPlayerItemFragment(musicList[position], player)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}