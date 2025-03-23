plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader

    // The false implies it is loaded but not applied until the individual modules apply it
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
//    alias(libs.plugins.kotlin.plugin.serialization) apply false

}