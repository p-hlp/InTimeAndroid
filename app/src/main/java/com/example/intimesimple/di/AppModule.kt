package com.example.intimesimple.di

import android.content.Context
import androidx.room.Room
import com.example.intimesimple.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context,
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app_db"
    ).build()

    @Singleton
    @Provides
    fun provideWorkoutDao(db: AppDatabase) = db.workoutDao()
}