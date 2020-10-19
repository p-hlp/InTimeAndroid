package com.example.intimesimple.ui.animations

import android.annotation.SuppressLint
import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.ui.graphics.Color


object AnimationDefinitions{

    val sizeState = FloatPropKey()
    val alphaState = FloatPropKey()
    val colorState = ColorPropKey()

    enum class FabState{
        Idle, Exploded
    }


    @SuppressLint("Range")
    fun explodeTransitionDefinition() = transitionDefinition<FabState>{
        state(FabState.Idle){
            this[sizeState] = 80f
        }

        state(FabState.Exploded){
            this[sizeState] = 4000f
        }

        transition(fromState = FabState.Idle, toState = FabState.Exploded){
            sizeState using keyframes {
                durationMillis = 700
                50f at 0
                30f at 120
                4000f at 700
            }
        }
    }
}