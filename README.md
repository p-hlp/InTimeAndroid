<img src="https://github.com/p-hlp/InTimeAndroid/blob/master/screenshots/preview.gif" align="right" height="500">

## What's InTime? :hourglass_flowing_sand:

InTime is an interval timer application using android jetpack components and a long running service.

The purpose of this project is to learn and implement new android technologies and libraries.

## Used Libraries
 - [Compose](https://developer.android.com/jetpack/compose) 
 - [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) 
 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  
 - [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) 
 - [Dagger Hilt](https://dagger.dev/hilt/) 
 - [Room](https://developer.android.com/topic/libraries/architecture/room) 
 - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

<br/>

<img src="https://github.com/p-hlp/InTimeAndroid/blob/master/screenshots/mvvm.png" align="left" width="350">

## Architecture
This application uses the MVVM (Model View ViewModel) architecture and unidirectional data flow.

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

## Navigation
Furthermore it is a single-activity application with compose-navigation handling the 
[navigation graph](https://github.com/p-hlp/InTimeAndroid/blob/master/app/src/main/java/com/example/intimesimple/ui/composables/navigation/Navigation.kt).
```kt
    NavHost(navController, startDestination = Screen.WorkoutListScreen.route) {
        // NavGraph
        composable(Screen.WorkoutListScreen.route) {
            WorkoutListScreen(
                ...
            )
        }
        composable(Screen.WorkoutAddScreen.route) {
            WorkoutAddScreen(
                ...
            )
        }
        composable(Screen.WorkoutDetailScreen.route) {
            WorkoutDetailScreen(
                ...
            )
        }
    }
```

## :construction: In development :construction:
The API of compose/compose-navigation is likely to change, since they are still 
in alpha. Because of that this project is also subject to change.

## License
This project is published under the GPLv3 license. 
```
Copyright (C) 2020 github.com/p-hlp

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
