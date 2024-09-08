package com.example.data.source.file

import android.content.ContentResolver
import android.database.ContentObserver
import android.media.MediaMetadataRetriever
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.StringDef
import com.example.data.model.Music
import com.example.data.source.file.MusicFileDataSource.MusicSortOrder.Companion.SORT_BY_DATE
import com.example.data.source.file.MusicFileDataSource.MusicSortOrder.Companion.SORT_BY_TITLE
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicFileDataSource @Inject constructor(private val contentResolver: ContentResolver) {

    @StringDef(SORT_BY_TITLE, SORT_BY_DATE)
    annotation class MusicSortOrder {
        companion object {
            const val SORT_BY_TITLE = "${MediaStore.Audio.Media.TITLE} ASC"
            const val SORT_BY_DATE = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        }
    }

    fun fetchAllMusicItems(sortOrder: String? = null): List<Music> {
        val musicList = mutableListOf<Music>()

        val musicQuery = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
            ),
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            sortOrder
        )

        musicQuery?.use {
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val filePathColumnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                musicList.add(
                    Music(
                        it.getLong(idColumnIndex),
                        it.getString(titleColumnIndex),
                        it.getString(artistColumnIndex),
                        it.getLong(durationColumnIndex),
                        it.getString(filePathColumnIndex),
                        getCoverImageUri(it.getString(filePathColumnIndex))
                    )
                )
            }
        }

        return musicList
    }


    suspend fun observeAllMusicItems(
        sortOrder: String? = null,
        onStartObserve: suspend (snapshot: List<Music>) -> Unit,
        onChanged: suspend (newMusicList: List<Music>) -> Unit
    ) {
        onStartObserve(fetchAllMusicItems(sortOrder))

        coroutineScope {
            contentResolver.registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                true,
                object: ContentObserver(Handler(Looper.getMainLooper())){
                    override fun onChange(selfChange: Boolean) {
                        super.onChange(selfChange)
                        launch {
                            onChanged(fetchAllMusicItems(sortOrder))
                        }
                    }
                }
            )
        }
    }


    private fun getCoverImageUri(musicFilePath: String): ByteArray? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(musicFilePath)
        val coverImage = mediaMetadataRetriever.embeddedPicture
        mediaMetadataRetriever.release()
        return coverImage
    }

}