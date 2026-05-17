rootProject.name = "platform"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "common",
    "domain",
    "bootstrap:web",
    "bootstrap:scheduler",
    "bootstrap:kafka"
)
