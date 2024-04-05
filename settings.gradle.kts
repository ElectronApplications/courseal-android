pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    val kotlinVersion: String by settings
    val hiltVersion: String by settings

    plugins {
        kotlin("android") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version "$kotlinVersion-1.0.20"
        id("com.google.dagger.hilt.android") version hiltVersion
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Courseal"
include(":app")
