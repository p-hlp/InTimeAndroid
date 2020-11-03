package com.example.intimesimple

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.intimesimple.data.local.AppDatabase
import com.example.intimesimple.data.local.WorkoutDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class WorkoutDatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var workoutDao: WorkoutDao
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(IOException::class)
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()
        workoutDao = db.workoutDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb(){
        db.close()
    }

    @Test
    fun basicFlowTest() = testScope.runBlockingTest {
        val workouts = async(testDispatcher) {
            workoutDao.getAllWorkouts().collect {
                assertThat(it).isEqualTo(listOf(TestUtil.WORKOUT_1,
                    TestUtil.WORKOUT_2,
                    TestUtil.WORKOUT_3)
                )
            }
        }

        workoutDao.insertWorkout(TestUtil.WORKOUT_1)
        workoutDao.insertWorkout(TestUtil.WORKOUT_2)
        workoutDao.insertWorkout(TestUtil.WORKOUT_3)
    }
}