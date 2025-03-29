# Compose Multiplatform Network Example App
It basically just sends log messages to the server

# Introduction

This project is an example of a Kotlin Multiplatform project that uses Compose for the UI on Android and the Desktop  .
The main purpose of this project is to gain experience:
* building a Kotlin Multiplatform project
* building a REST app based on Ktor
* that has both a client side and a server side.
* TODO Multi Module Multiplatform project
* Compose Screen Navigation

# Instructions for use

Load the project into Android Studio 

The server localhost is hardcoded. 
* look up your IO address with terminal command `ip addr show` or `ifconfig`
* You need to change the server IP address in the files:
  * `shared/src/androidMain/res/xml/network_security_confid.xml`
  *  `shared/src/commonMain/kotlin/com/emh/log/util/Constants.kt` in the constant `CLIENT_LOCAL_URL`.
* Go to the file `server/src/com/emh/log/Application` and start the server by running the function main
* You can use the Android emulator or a real device to run the app.
  * You can run it from the composeApp config on the top menu bar, or from the App class in `composApp/src/composeMain/kotlin/com/emh/log/app/App.kt`
  * If you are using the Android emulator, make sure that the server IP address is reachable from the emulator.
  * If you are using a real device, make sure that the server IP address is reachable from the device.

# Left to do
* Test the desktopApp
* Consolidate DataError and NetworkError
* hide keyboard when button pressed
* The application may be doing too much work on its main thread.
* Add local ROOM DB
* Automated testing
  * UI
  * Viewmodel
  * Repository
  * Network data source (Client)
  * Local data source
  * Server

# Architecture images
Diagrams illustrating the architecture are located in the top level directory /diagrams
* Ktor Clean Code Architecture
* Ktor Client Calls
* Ktor Client Calls through Layers

# Requirements
* Include a logger module that can be reused in other projects
* TODO LATER Include some problem domain concept model, eg sensor data

# Architecture Notes
The project uses State of the Art Android Architecture circa 2025. It uses:
* Compose Kotlin Multiplatform
* Kotlin 100%
* Compose for building UI
* Compose NavGraph navigation
* Koin for Dependency Injection
  * For now, the only classes that are injected are the HTTP Client, ViewModel, and Repository
* Ktor for Network calls
~~* TODO Coil for loading images from network URLs~~
* MVI to
  * persist UI local cache across orientation changes
  * UI state variables governing UI compose
    * Enable state hoisting
  * business logic to calculate values for UI display (i.e. state)
  * business logic for actions in response to UI events
* Repository to separate presentation & business logic from Data Source
  ~~* Examples of both localDataSource and remoteDataSource~~
  ~~* localDataSource is ROOM DB~~
  * remoteDataSource is Ktor
* Use Flows to move UI state data back to UI compose (via the ViewModel) from Repository
* Coroutines for both serial and parallel structured concurrency
* Unit Testing
  * Ktor unit tests
  * DB instrumented test
    * These need to run on a real device
    * best resource for room DB testing https://medium.com/@wambuinjumbi/unit-testing-in-android-room-361bf56b69c5
  * Compose UI testing
    * reference https://developer.android.com/codelabs/jetpack-compose-testing#0
*



# Compose Multiplatform
This is a Kotlin Multiplatform project targeting Android, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…