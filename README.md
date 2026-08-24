<div align="center">

# 🔍 WhatsMyClient

### High-Performance Client Brand Identification & Feature Integration SDK

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Minestom](https://img.shields.io/badge/Minestom-Native-2196F3?style=for-the-badge&logo=minecraft&logoColor=white)](https://minestom.net/)
[![Lunar Client](https://img.shields.io/badge/Lunar_Client-Apollo_Hooks-00E5FF?style=for-the-badge&logo=starship&logoColor=black)](https://lunarclient.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

<p align="center">
  <b>WhatsMyClient</b> is a lightweight, zero-allocation Kotlin / Java library designed to identify Minecraft client software during handshake and broadcast rich visual capabilities (such as Lunar Client WorldEdit CUI and Apollo waypoints) with zero client mods required.
</p>

</div>

---

## 🎯 Key Capabilities

* **🔍 Accurate Brand Identification**: Intercepts plugin messaging channels (`minecraft:brand`, `lunarclient:pm`, `fml:handshake`, etc.) during login to accurately identify Vanilla, Lunar Client, Badlion, Feather, Fabric, Forge, and Geyser (Bedrock) clients.
* **📐 Native Selection Visuals (WorldEdit CUI)**: Wireframe selection bounding boxes sent directly over `worldedit:cui` plugin messaging channels, rendered natively for Lunar Client and CUI-enabled users without requiring any client-side mod installations.
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

### 2. Render 3D Selection Visuals
```kotlin
// Show a 3D bounding box to a player (rendered natively in Lunar Client CUI)
WhatsMyClient.showSelection(player, minX, minY, minZ, maxX, maxY, maxZ)

// Clear selection
WhatsMyClient.clearSelection(player)
```

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
