pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://github.com/miamtech/releaseMealz/raw/main")
        }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()

    }
    versionCatalogs {
        create("libs")
    }
}

rootProject.name = "MealzMarmiton"
include(":app")
include(":marmiton")

// FOR DEV PURPOSE
