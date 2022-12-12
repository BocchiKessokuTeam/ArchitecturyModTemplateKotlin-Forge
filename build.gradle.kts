plugins {
    java
    `kotlin-dsl`

    alias(libs.plugins.loom)
    //alias(libs.plugins.loom.quiltflower)

    alias(libs.plugins.github.release)
    alias(libs.plugins.machete)
    `maven-publish`
}

val mavenGroup: String by project
val modVersion: String by project

group = mavenGroup
version = modVersion

loom{
    forge{
        mixinConfigs("mod-template.mixins.json")
    }
}

repositories {
    mavenCentral()
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

val minecraftVersion = libs.versions.minecraft.get()

dependencies {
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:${libs.versions.yarn.get()}:v2")
    forge(libs.forge)
    modImplementation(libs.kotlin.forge)

}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
    
    remapJar {
        archiveClassifier.set("forge-$minecraftVersion")
    }
    
    remapSourcesJar {
        archiveClassifier.set("forge-$minecraftVersion-sources")
    }

    register("releaseMod") {
        group = "mod"

        dependsOn("publish")
    }
}

java {
    withSourcesJar()   
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            from(components["java"])
        }
    }

    repositories {
    }
}
