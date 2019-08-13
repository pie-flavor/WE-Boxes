rootProject.name = "WE-Boxes"
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "net.minecraftforge.gradle") {
                useModule("net.minecraftforge.gradle:ForgeGradle:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        maven {
            name = "Forge"
            url = uri("https://files.minecraftforge.net/maven/")
        }
    }
}
