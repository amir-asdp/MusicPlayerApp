package com.example.data.source.file

import android.content.Context
import android.database.ContentObserver
import android.media.MediaMetadataRetriever
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.example.data.model.Music

class MusicFileDataSource(private val context: Context) {

    fun fetchAllMusicItems(sortOrder: String?): List<Music> {
        val musicList = mutableListOf<Music>()

        val musicQuery = context.contentResolver.query(
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


    fun observeAllMusicItems(
        sortOrder: String?,
        onStartObserve: (snapshot: List<Music>) -> Unit,
        onChanged: (newMusicList: List<Music>) -> Unit
    ) {
        onStartObserve(fetchAllMusicItems(sortOrder))

        context.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            true,
            object: ContentObserver(Handler(Looper.getMainLooper())){
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    onChanged(fetchAllMusicItems(sortOrder))
                }
            }
        )
    }


    private fun getCoverImageUri(musicFilePath: String): ByteArray? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(musicFilePath)
        val coverImage = mediaMetadataRetriever.embeddedPicture
        mediaMetadataRetriever.release()
        return coverImage
    }

}