package com.example.intimesimple.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.preference.PreferenceDataStore
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
            Workout( "ExampleWorkout1", 35000L, 10000L, 4),
            Workout( "ExampleWorkout2", 45000L, 15000L, 3),
            Workout( "ExampleWorkout3", 20000L, 5000L, 6)
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

    @Singleton
    @Provides
    fun provideDataStore(
            @ApplicationContext app: Context
    ): DataStore<Preferences> = app.createDataStore(
            name = "settings"
    )
}