package com.example.data.repository.implementation

import com.example.data.model.Music
import com.example.data.repository.contract.MusicRepository
import com.example.data.source.file.MusicFileDataSource
import com.example.data.source.file.MusicFileDataSource.MusicSortOrder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MusicRepositoryImpl @Inject constructor(
    private val coroutineContext: CoroutineContext,
    private val musicFileDataSource: MusicFileDataSource
): MusicRepository {

    override fun getAll(@MusicSortOrder sortOrder: String?): Flow<List<Music>> = flow {
        coroutineScope {
            musicFileDataSource.observeAllMusicItems(
                sortOrder = sortOrder,
                onStartObserve = {
                    emit(it)
                },
                onChanged = {
                    emit(it)
                }
            )
        }
    }.flowOn(coroutineContext)

    override fun getAllSortByTitle(): Flow<List<Music>> = getAll(MusicSortOrder.SORT_BY_TITLE)

    override fun getAllSortByDate(): Flow<List<Music>> = getAll(MusicSortOrder.SORT_BY_DATE)

}