package com.example.data.repository.contract

import com.example.data.model.Music
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getAll(sortOrder: String?): Flow<List<Music>>

    fun getAllSortByTitle(): Flow<List<Music>>

    fun getAllSortByDate(): Flow<List<Music>>

}