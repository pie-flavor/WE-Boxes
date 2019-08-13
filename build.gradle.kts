import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    kotlin("kapt") version "1.3.31"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    id("flavor.pie.promptsign") version "1.1.0"
    id("maven-publish")
    id("net.minecraftforge.gradle.forge") version "2.3-SNAPSHOT"
}

group = "flavor.pie"
version = "0.1.0"

repositories {
    mavenCentral()
    maven {
        name = "sponge"
        url = uri("https://repo.spongepowered.org/maven/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io/")
    }
    maven {
        name = "bstats"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    maven {
        name = "sk89q"
        url = uri("https://maven.sk89q.com/repo/")
    }
}

dependencies {
    val sponge = create(group = "org.spongepowered", name = "spongeapi", version = "7.1.0")
    api(sponge)
    kapt(sponge)
    val kotlin = kotlin("stdlib-jdk8")
    api(kotlin)
    shadow(kotlin)
    val kludge = create(group = "com.github.pie-flavor", name = "kludge", version = "477392a")
    implementation(kludge)
    shadow(kludge)
    val bstats = create(group = "org.bstats", name = "bstats-sponge-lite", version = "1.4")
    implementation(bstats)
    shadow(bstats)
    implementation(group = "com.sk89q.worldedit", name = "worldedit-core", version = "7.0.0")
}

tasks.named<Jar>("jar") {
    enabled = false
}

val shadowJar = tasks.named<ShadowJar>("shadowJar") {
    configurations = listOf(project.configurations.named("shadow").get())
    classifier = ""
    relocate("kotlin", "flavor.pie.boxes.runtime.kotlin")
    relocate("flavor.pie.kludge", "flavor.pie.boxes.util.kludge")
    minimize()
}

tasks.named<Task>("build") {
    dependsOn(shadowJar)
}

tasks.named<Task>("signArchives") {
    dependsOn(shadowJar)
}

tasks.named<Task>("reobfJar") {
    dependsOn(shadowJar)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create("sponge", MavenPublication::class.java) {
            project.shadow.component(this)
            pom {
                name.set("WE-Boxes")
                description.set("Adds BlingEdit-style editor boxes to WorldEdit.")
                url.set("https://ore.spongepowered.org/pie_flavor/we-boxes/")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/pie-flavor/WE-Boxes/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("pie_flavor")
                        name.set("Adam Spofford")
                        email.set("aspofford.as@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/pie-flavor/WE-Boxes.git")
                    developerConnection.set("scm:git:ssh://github.com/pie-flavor/WE-Boxes.git")
                    url.set("https://github.com/pie-flavor/WE-Boxes/")
                }
            }
        }
        repositories {
            maven {
                val spongePublishingUri: String by project
                val spongePublishingUsername: String by project
                val spongePublishingPassword: String by project
                url = uri(spongePublishingUri)
                credentials {
                    username = spongePublishingUsername
                    password = spongePublishingPassword
                }
            }
        }
    }
}

minecraft {
    mappings = "stable_39"
    version = "1.12.2-14.23.5.2838"
}

