val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.conventionalCommits)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.naqswell"
version = "0.0.1"

application {
    mainClass.set("com.naqswell.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

/*  accepted types: feat, fix, build, chore, ci, docs, style, refactor, perf, test, tmp
    simplified commit format: 'type(scope): message'
    example commit: 'feat(android): Added app widget'
    see https://www.conventionalcommits.org/en/v1.0.0/ for more details on conventional commits
*/
conventionalCommits {
    warningIfNoGitRoot = true
    successMessage = "Commit message meets Conventional Commit standards..."
    failureMessage = "The commit message does not meet the Conventional Commit standard"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.bundles.hoplite)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(libs.mongodb.coroutines)
}
