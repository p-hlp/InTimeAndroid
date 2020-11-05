package com.example.intimesimple.ui.animations

import android.annotation.SuppressLint
import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import com.example.intimesimple.ui.theme.DarkBlue900
import com.example.intimesimple.ui.theme.Green500


object AnimationDefinitions{

    val sizeState = FloatPropKey()
    val alphaState = FloatPropKey()
    val colorState = ColorPropKey()

    enum class FabState{
        Idle, Exploded
    }


    @SuppressLint("Range")
    val explodeTransitionDefinition = transitionDefinition<FabState>{
        state(FabState.Idle){
            this[sizeState] = 60f
            this[colorState] = Green500
        }

        state(FabState.Exploded){
            this[sizeState] = 4000f
            this[colorState] = DarkBlue900
        }

        transition(fromState = FabState.Idle, toState = FabState.Exploded){
            sizeState using keyframes {
                durationMillis = 700
                60f at 0
                30f at 120
                4000f at 700
            }
            colorState using tween(durationMillis = 300)
        }
    }
}