<div align="center">

# 🔍 WhatsMyClient

### High-Performance Client Brand Identification & Feature Integration SDK

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Minestom](https://img.shields.io/badge/Minestom-Native-2196F3?style=for-the-badge&logo=minecraft&logoColor=white)](https://minestom.net/)
[![Lunar Client](https://img.shields.io/badge/Lunar_Client-Apollo_Hooks-00E5FF?style=for-the-badge&logo=starship&logoColor=black)](https://lunarclient.com/)
[![LabyMod](https://img.shields.io/badge/LabyMod-Server_API-1E88E5?style=for-the-badge&logo=wolfpack&logoColor=white)](https://dev.labymod.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

<p align="center">
  <b>WhatsMyClient</b> is a lightweight, zero-allocation Kotlin / Java library designed to identify Minecraft client software during handshake and broadcast rich visual capabilities (such as Lunar Client WorldEdit CUI, Apollo waypoints, and LabyMod economy HUDs) with zero client mods required.
</p>

</div>

---

## 🎯 Key Capabilities

* **🔍 Accurate Brand Identification**: Intercepts plugin messaging channels (`minecraft:brand`, `lunarclient:pm`, `labymod3:main`, `fml:handshake`, etc.) during login to accurately identify Vanilla, Lunar Client, LabyMod, Badlion, Feather, Fabric, Forge, and Geyser (Bedrock) clients.
* **📐 Native Selection Visuals (WorldEdit CUI)**: Wireframe selection bounding boxes sent directly over `worldedit:cui` plugin messaging channels, rendered natively for Lunar Client and CUI-enabled users without requiring any client-side mod installations.
* **🐺 LabyMod 3 & 4 Server Protocol Bridge**: Direct integration with LabyMod features including native Economy HUDs, server gamemode badges, floating player subtitles, markers, and feature permission controls.
* **⚡ High-Throughput & Zero Overhead**: Non-blocking packet interception designed for high-frequency server engines (Minestom, Fabric, Paper).

---

## 📦 Installation

### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.terrorify:whatsmyclient:1.0.0")
}
```

---

## 🚀 Quick Start

### 1. Initialize on Server Startup
```kotlin
import dev.terrorify.whatsmyclient.WhatsMyClient

fun main() {
    val server = MinecraftServer.init()
    
    // Initialize WhatsMyClient channels and listeners
    WhatsMyClient.init()
    
    server.start("0.0.0.0", 25565)
}
```

### 2. Lunar Client Selection Visuals
```kotlin
// Show a 3D bounding box to a player (rendered natively in Lunar Client CUI)
WhatsMyClient.showSelection(player, minX, minY, minZ, maxX, maxY, maxZ)

// Clear selection
WhatsMyClient.clearSelection(player)
```

### 3. LabyMod Economy HUD & Server Displays
```kotlin
// Update the player's balance in LabyMod's native Economy HUD
WhatsMyClient.sendLabyEconomy(player, balance = 12500.50, visible = true, decimal = true)

// Set the active gamemode indicator under the server header
WhatsMyClient.setLabyGamemode(player, "Competitive Ranked 1v1")
```

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
