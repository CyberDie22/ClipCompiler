
rootProject.name = "Compiler"


include(":IR")
include(":JVM")

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

