rootProject.name = "platform"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "common",
    "core",
    "bootstrap:web",
    "bootstrap:scheduler",
    "bootstrap:kafka"
)
