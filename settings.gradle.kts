rootProject.name = "platform"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "common",
    "core",
    "bootstrap:api",
    "bootstrap:scheduler",
    "bootstrap:kafka"
)
