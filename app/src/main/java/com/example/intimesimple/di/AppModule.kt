package com.example.intimesimple.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.intimesimple.data.local.AppDatabase
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    lateinit var database: AppDatabase
    val PREPOPULATE_DATA = listOf(
            Workout(0, "ExampleWorkout1", 35000L, 10000L, 4),
            Workout(0, "ExampleWorkout2", 45000L, 15000L, 3),
            Workout(0, "ExampleWorkout3", 20000L, 5000L, 6)
    )

    @Singleton
    @Provides
    fun provideAppDatabase(
            @ApplicationContext app: Context
    ): AppDatabase {
        database = Room.databaseBuilder(
                app,
                AppDatabase::class.java,
                DATABASE_NAME
        ).addCallback(object: RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    for (workout in PREPOPULATE_DATA){
                        database.workoutDao().insertWithTimestamp(workout)
                    }
                }
            }
        }).build()
        return database
    }

    @Singleton
    @Provides
    fun provideWorkoutDao(db: AppDatabase) = db.workoutDao()
}