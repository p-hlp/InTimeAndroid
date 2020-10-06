package com.example.intimesimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.viewModels
import com.example.intimesimple.ui.composables.WorkoutListScreen
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

    }
}