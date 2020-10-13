package com.example.intimesimple.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainActivityPendingIntent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CancelActionPendingIntent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PauseActionPendingIntent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ResumeActionPendingIntent
