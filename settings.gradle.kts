pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Projet1DIIAGE"
include(":app")

include(":core:ui")
include(":core:domain")
include(":core:data")

include(":features:home:api")
include(":features:home:ui")
include(":features:home:domain")
include(":features:home:data")

include(":features:profile:api")
include(":features:profile:ui")
include(":features:profile:domain")
include(":features:profile:data")
