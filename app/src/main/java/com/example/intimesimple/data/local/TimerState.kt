package com.example.intimesimple.data.local

enum class TimerState(val value: Int) {
    RUNNING(1), PAUSED(2), EXPIRED(3);
    companion object {
        fun fromValue(value: Int): TimerState? {
            for (state in TimerState.values()) {
                if (state.value == value) {
                    return state
                }
            }
            return null
        }
    }
}