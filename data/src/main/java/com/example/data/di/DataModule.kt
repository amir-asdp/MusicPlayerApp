package com.example.data.di

import android.content.ContentResolver
import android.content.Context
import com.example.data.repository.contract.MusicRepository
import com.example.data.repository.implementation.MusicRepositoryImpl
import com.example.data.source.file.MusicFileDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Singleton
    @Provides
    fun provideMusicFileDataSource(contentResolver: ContentResolver): MusicFileDataSource {
        return MusicFileDataSource(contentResolver)
    }

    @Singleton
    @Provides
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideMusicRepository(
        coroutineContext: CoroutineContext,
        musicFileDataSource: MusicFileDataSource
    ): MusicRepository {
        return MusicRepositoryImpl(coroutineContext, musicFileDataSource)
    }

}