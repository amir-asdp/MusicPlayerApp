package com.example.musicplayerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.model.Music
import com.example.data.repository.contract.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val exoPlayer: ExoPlayer,
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeAllSongs()
    }


    fun observeAllSongs(){
        viewModelScope.launch{

            _uiState.update { it.copy(isLoading = true) }

            musicRepository.getAll(null).collect{ fetchedMusicList ->
                _uiState.update {
                    it.copy(isLoading = false, musicList = fetchedMusicList)
                }
            }

        }
    }

    fun getAllSongs(): List<Music>{
        return _uiState.value.musicList
    }

    fun getCurrentSelectedSong(): Music{
        return _uiState.value.musicList[_uiState.value.currentMusicPosition]
    }

    fun setCurrentSongPosition(position: Int){
        _uiState.update {
            it.copy(currentMusicPosition = position)
        }
    }

}

data class UiState(
    val isLoading: Boolean = false,
    val musicList: List<Music> = emptyList(),
    val currentMusicPosition: Int = 0
)