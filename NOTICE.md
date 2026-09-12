# NOTICE

## Upstream project

This repository is a port of **JurassiCraft** to Minecraft Forge **1.20.1**.
It is based on the JurassiCraft source code — most directly on
[Gegy/JurassiCraft2](https://github.com/Gegy/JurassiCraft2) — which is licensed under the
**GNU Lesser General Public License v2.1**.

- Original project: https://github.com/Gegy/JurassiCraft2
- Original authors: iLexiconn, Gegy1000, TheLarsinator, BobMowzie, Rafamv, JTGhawk137 and contributors.

## License

This project is distributed under the **GNU Lesser General Public License v2.1** — see
[LICENSE.md](LICENSE.md). All original JurassiCraft code and assets keep their original copyright and
are used here under the same LGPL-2.1 terms. All original credit belongs to the JurassiCraft authors
and contributors.

## What this port changed

The 1.20.1 port is adaptation work forced by the newer modding platform:

- build system rebuilt for ForgeGradle 6 / Gradle 8 / Java 17 (Forge 1.20.1, official mappings)
- the `llibrary` classes used by the mod were vendored into this repository, removing the external
  `llibrary` dependency
- 1.12.2 → 1.20.1 API migration: registries, blocks/items, `TileEntity` → `BlockEntity`, entities and
  AI, entity/block renderers, `GuiScreen` → `Screen`/`GuiGraphics`, `SimpleNetworkWrapper` →
  `SimpleChannel`, world generation, recipes, `en_US.lang` → `en_us.json`
- resources converted to the 1.13+ formats (blockstates / models / recipes JSON)
- rendering rework, including GeckoLib based models for some machines and the security fence
- gameplay and UI fixes (fence build/repair/power and cascading removal, DinoPad creature previews,
  egg hatching progress synchronisation, pregnancy data for the DinoPad)

## Third-party components

- **GeckoLib** (`software.bernie.geckolib`) — MIT License
- vendored **llibrary** code — see the upstream LLibrary project for its license terms
- Minecraft and Forge are property of Mojang Studios and the MinecraftForge team respectively
