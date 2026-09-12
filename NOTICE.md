# NOTICE — Port Notice

## Upstream project

This repository is a port of **JurassiCraft**, originally created by the **Timeless Modding Team**,
from Minecraft Forge 1.12.2 to **Minecraft Forge 1.20.1**.

- Original project: https://github.com/TimelessModdingTeam/JurassiCraft
- Original authors: iLexiconn, TheLarsinator, Click_Me, Gegy1000, RafaMv, JTGhawk137 and contributors.

## License of the original work

The original mod — its source code, binaries and assets — is distributed under the
**Timeless Modding Team - Minecraft Mod License v1.2**, which is the license file shipped in this
repository (`LICENSE.md`). That license stays in effect for every piece of original JurassiCraft
code, asset and name contained here. Nothing in this port notice grants extra rights to the
original work, and all original credit belongs to the Timeless Modding Team and the JurassiCraft
contributors.

## What this port changed

The 1.20.1 port is adaptation work forced by the newer modding platform:

- build system rebuilt for ForgeGradle 6 / Gradle 8 / Java 17 (Forge 1.20.1, official mappings)
- the `llibrary` classes used by the mod were vendored into this repository, removing the
  external `llibrary` dependency
- 1.12.2 → 1.20.1 API migration: registries, blocks/items, `TileEntity` → `BlockEntity`, entities
  and AI, entity/block renderers, `GuiScreen` → `Screen`/`GuiGraphics`, `SimpleNetworkWrapper` →
  `SimpleChannel`, world generation, recipes, `en_US.lang` → `en_us.json`
- resources converted to the 1.13+ formats (blockstates / models / recipes JSON)
- rendering rework, including GeckoLib based models for some machines and the security fence
- gameplay and UI bug fixes (fence build/repair/redstone and cascading removal, DinoPad creature
  previews, egg hatching progress synchronisation, pregnancy data for the DinoPad)

## Third-party components

- **GeckoLib** (`software.bernie.geckolib`) — MIT License
- vendored **llibrary** sources remain under their original terms
- Minecraft and Forge are property of Mojang Studios and the MinecraftForge team respectively

## Attribution

Ported to Minecraft Forge 1.20.1 by the JurassiCraft 1.20.1 port project.
