import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.3.4"
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "wtf.nucker"
version = "1.0"
description = "Infinite parkour plugin for your server"

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  maven(url = "https://repo.aikar.co/content/groups/aikar/")
  maven(url = "https://jitpack.io")
}

dependencies {
  paperDevBundle("1.18.1-R0.1-SNAPSHOT")
  implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
  implementation("com.github.simplix-softworks:simplixstorage:3.2.4")
}

tasks {
  assemble {
    dependsOn(reobfJar)
    dependsOn(shadowJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(17)

  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name()
  }

  shadowJar {
    relocate("co.aikar.commands", "wtf.nucker.infiniteparkour.acf")
    relocate( "co.aikar.locales", "wtf.nucker.infiniteparkour.locales")
  }
}

bukkit {
  load = BukkitPluginDescription.PluginLoadOrder.STARTUP
  main = "wtf.nucker.infiniteparkour.InfiniteParkour"
  apiVersion = "1.18"
  authors = listOf("Nucker")
  description = "An infinite parkour plugin for your server"
  name = "InfiniteParkour"
  website = "nucker.me"
}
