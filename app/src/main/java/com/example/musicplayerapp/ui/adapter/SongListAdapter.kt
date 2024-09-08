package com.example.musicplayerapp.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.Music
import com.example.musicplayerapp.databinding.ItemMusicBinding

class SongListAdapter(
    private val songList: MutableList<Music>,
    private val onItemClicked: (position: Int) -> Unit
):  RecyclerView.Adapter<SongListAdapter.SongItemViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        return SongItemViewHolder(
            ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: SongItemViewHolder, position: Int) {
        holder.bind(position)
    }

    fun updateSongList(newSongList: List<Music>){
        songList.clear()
        songList.addAll(newSongList)
        notifyDataSetChanged()
    }



    inner class SongItemViewHolder(private val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val music = songList[position]
            val formattedDuration = "${(music.duration/(1000*60)).toInt()} : ${((music.duration/1000)%60).toInt()}"

            music.coverImage?.let {
                binding.musicCoverImgView.setImageBitmap(
                    BitmapFactory.decodeByteArray(it, 0, it.size)
                )
            }
            binding.musicTitleTxv.text = music.title
            binding.musicArtistTxv.text = music.artist
            binding.musicDurationTxv.text = formattedDuration
            binding.root.setOnClickListener {
                onItemClicked(position)
            }

        }
    }

}