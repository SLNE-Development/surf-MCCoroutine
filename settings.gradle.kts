plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "surf-MCCoroutine"

if (file("MCCoroutine/build.gradle.kts").exists()) {
    includeBuild("MCCoroutine")
}