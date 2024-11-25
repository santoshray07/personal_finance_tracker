buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.realm.gradle.plugin)
    }
}

plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
}
