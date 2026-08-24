<div align="center">

# 🔍 WhatsMyClient

### High-Performance Universal Client Identification & Cross-Client Mod Enforcer SDK

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Minestom](https://img.shields.io/badge/Minestom-Native-2196F3?style=for-the-badge&logo=minecraft&logoColor=white)](https://minestom.net/)
[![Lunar Client](https://img.shields.io/badge/Lunar_Client-Apollo_Hooks-00E5FF?style=for-the-badge&logo=starship&logoColor=black)](https://lunarclient.com/)
[![Badlion Client](https://img.shields.io/badge/Badlion_Client-ModAPI-FFA000?style=for-the-badge&logo=lion&logoColor=white)](https://github.com/BadlionClient/BadlionClientModAPI)
[![Feather Client](https://img.shields.io/badge/Feather_Client-Server_API-AA00FF?style=for-the-badge&logo=feather&logoColor=white)](https://docs.feathermc.com/)
[![Alpine Client](https://img.shields.io/badge/Alpine_Client-Ridge_API-00B0FF?style=for-the-badge&logo=alpinelinux&logoColor=white)](https://github.com/alpine-client/ridge)
[![NoRisk Client](https://img.shields.io/badge/NoRisk_Client-Server_API-E040FB?style=for-the-badge&logo=shield&logoColor=white)](https://docs.norisk.gg/)
[![LabyMod](https://img.shields.io/badge/LabyMod-Server_API-1E88E5?style=for-the-badge&logo=wolfpack&logoColor=white)](https://dev.labymod.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

<p align="center">
  <b>WhatsMyClient</b> is a lightweight, zero-allocation Kotlin / Java library designed to identify Minecraft client software during handshake and broadcast rich visual capabilities with zero client mods required.<br/>
  Features a <b>Universal Cross-Client Mod Enforcer</b> that automatically maps global feature rules across Lunar, Badlion, Feather, Alpine, NoRisk, and LabyMod.
</p>

</div>

---

## 🎯 Key Capabilities

* **🌐 Universal Global Mod Enforcer**: Declare a single list of disallowed client features (`MINIMAP`, `FREELOOK`, `WAYPOINTS`, `SCHEMATICA`, `FULLBRIGHT`, etc.). WhatsMyClient automatically maps and transmits the proprietary payload for whichever client the player is using.
* **🔍 Accurate Brand Identification**: Intercepts plugin messaging channels (`minecraft:brand`, `lunarclient:pm`, `badlion:mods`, `feather:client`, `ac:play`, `norisk:client`, `labymod3:main`) during login to accurately identify all major clients.
* **📐 Native Selection Visuals (WorldEdit CUI)**: Wireframe selection bounding boxes sent directly over `worldedit:cui` plugin messaging channels, rendered natively for Lunar Client and CUI-enabled users without requiring client-side mod installations.
* **🦁 Badlion Client ModAPI Bridge**: Enforce server-side rules by disabling unfair client mods, broadcast server-managed waypoints, and render HUD countdown timers.
* **🪶 Feather Client Server API**: Disallow unpermitted mods and manage dynamic Discord Rich Presence (RPC) profiles directly from the server.
* **🏔️ Alpine Client (Ridge) & 🛡️ NoRisk Client**: Full protocol support for in-game notifications, custom HUD cooldowns, and mod enforcement.
* **🐺 LabyMod 3 & 4 Server Protocol Bridge**: Direct integration with LabyMod features including native Economy HUDs, server gamemode badges, floating player subtitles, and permissions.
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
import dev.terrorify.whatsmyclient.features.ClientFeature

fun main() {
    val server = MinecraftServer.init()
    
    // 1. Initialize WhatsMyClient channels and listeners
    WhatsMyClient.init()
    
    // 2. Configure Global Mod Rules (Applies to ALL clients automatically)
    WhatsMyClient.disableFeatures(
        ClientFeature.MINIMAP,
        ClientFeature.FREELOOK,
        ClientFeature.WAYPOINTS,
        ClientFeature.SCHEMATICA,
        ClientFeature.FULLBRIGHT
    )
    
    server.start("0.0.0.0", 25565)
}
```

### 2. Auto-Enforce Rules on Player Join
```kotlin
// Automatically translates and sends the correct payload for Badlion, Feather, Alpine, NoRisk, or LabyMod
WhatsMyClient.enforceRules(player)
```

### 3. Lunar Client Selection Visuals
```kotlin
// Show a 3D bounding box to a player (rendered natively in Lunar Client CUI)
WhatsMyClient.showSelection(player, minX, minY, minZ, maxX, maxY, maxZ)

// Clear selection
WhatsMyClient.clearSelection(player)
```

### 4. Client-Specific Direct Feature Calls
```kotlin
// LabyMod Economy HUD
WhatsMyClient.sendLabyEconomy(player, balance = 12500.50, visible = true, decimal = true)

// Badlion Waypoints & Timers
WhatsMyClient.createBadlionWaypoint(player, name = "Boss Arena", x = 150, y = 64, z = -200)

// Alpine / NoRisk Notifications
WhatsMyClient.sendAlpineNotification(player, title = "Match Starting", message = "Ranked 1v1 starting in 5 seconds!")
```

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
