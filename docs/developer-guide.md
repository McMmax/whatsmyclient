# 🔍 WhatsMyClient: Client Branding & Channel Inspector

> **Passive Handshake Inspector and Plugin Message Channel Analyzer for Minecraft Client Identification.**  
> *Hard-proof documentation for `libs/whatsmyclient`.*

---

## ⚡ 1. Supported Client Detections

| Client / Mod | Channel / Signature |
| :--- | :--- |
| **Vanilla** | `minecraft:brand` = `"vanilla"` |
| **Lunar Client** | `lunar:client` / `lunarclient:pm` |
| **Badlion Client** | `badlion:mod` / `badlion:mods` |
| **Feather Client** | `feather:client` |
| **Fabric** | `fabric:registry/sync` |
| **Forge** | `FML|HS` / `fml:handshake` |
| **Bedrock (Geyser)** | Floodgate UUID prefix & XUID metadata |

---

## 📖 Canonical Specifications
* **[`.bible/09-shared-libraries-and-apis`](.bible/09-shared-libraries-and-apis)**
