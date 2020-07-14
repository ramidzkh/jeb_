plugins {
    id("fabric-loom") version "0.4.30"
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    minecraft("net.minecraft", "minecraft", "20w28a")
    mappings("net.fabricmc", "yarn", "20w28a+build.20", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.8.9+build.203")
}
