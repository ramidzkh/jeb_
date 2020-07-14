rootProject.name = "jeb_"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}
