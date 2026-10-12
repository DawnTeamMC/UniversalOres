[![Universal Ores](https://dawnteammc.github.io/universal_ores/images/header.png)](https://dawnteammc.github.io/universal_ores)

# 💎 Universal Ores

[![Latest release](https://img.shields.io/github/release/DawnTeamMC/UniversalOres.svg?label=Latest%20version&style=for-the-badge)](https://github.com/DawnTeamMC/UniversalOres/releases/latest)
[![CurseForge downloads](http://cf.way2muchnoise.eu/full_489884_downloads.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/universal-ores-fabric)

[![Discord user count](https://img.shields.io/discord/504608980799062036?label=&color=424549&labelColor=7289da&style=for-the-badge&logo=Discord&logoColor=DDE4EF)](https://discord.gg/8ksTVJu)
[![X (formerly Twitter) Follow](https://img.shields.io/twitter/follow/DawnTeamMC?style=for-the-badge&labelColor=424549&color=424549&logo=x)](https://twitter.com/DawnTeamMC)

[![License (Polyform Shield 1.0.0)](https://img.shields.io/badge/code%20license-Polyform%20Shield%201.0.0-green.svg?style=for-the-badge)](https://polyformproject.org/licenses/shield/1.0.0/)

Universal Ores is a mod for the latest version of Minecraft that adds new vanilla ores variants for andesite, 
diorite, granite, tuff, calcite, blackstone and basalt that generate naturally where they should precisely be.

**You can visit the [official website for Universal Ores for more information](https://dawnteammc.github.io/universal_ores).**

## 📦 Download
We use [CurseForge](https://www.curseforge.com/minecraft/mc-mods/universal-ores-fabric) and [Modrinth](https://modrinth.com/mod/universal_ores) to publish **stable builds** of Universal Ores for Minecraft.

Universal Ores is available for both **Fabric** and **NeoForge**.

⚠ On Fabric, Universal Ores **requires** Fabric API to be installed: [GitHub](https://github.com/FabricMC/fabric) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api) / [Modrinth](https://modrinth.com/mod/fabric-api)

On NeoForge, no other mod is required.

## 🧩 Adding stones and ores

Universal Ores makes a variant of every ore it knows of in every stone it knows of, with its own texture, drops, tags, recipes and world generation, all copied from the original ore when the game starts.

Stones and ores are listed in JSON files, which are read when the game starts:
- **Mods** can ship any number of them in a `universal_ores/` folder at the root of their jar. No code or dependency is needed: entries for blocks that aren't there are simply ignored.
- **Modpacks** can put them in `config/universal_ores/`. These files are read last, in alphabetical order, so they can change or remove anything.

```json
{
  "stones": {
    "mymod:marble": "minecraft:stone",
    "mymod:dark_marble": {
      "ores_of": "minecraft:stone",
      "except": ["minecraft:coal_ore"],
      "strength": 1.5,
      "texture": "mymod:block/dark_marble_side"
    },
    "minecraft:calcite": false
  },
  "ores": {
    "mymod:tin_ore": "minecraft:stone",
    "mymod:nether_tin_ore": {
      "in": "minecraft:netherrack",
      "texture": "mymod:block/nether_tin_ore"
    }
  }
}
```

- A **stone** gets the ores found in the stones it gets the ores of (`ores_of`, one stone or a list). With `only` or `except`, it only gets some of them. `strength` sets the hardness of its ores, and `texture` the texture they are drawn on (or `{"side": ..., "end": ...}` for pillars like basalt).
- An **ore** is found in the stone given by `in`. `texture` is its texture, if it isn't `<namespace>:block/<path>`.
- `false` removes a stone or an ore listed by an earlier file.

Variants that a mod already adds itself, named `<stone>_<ore>` (like `mymod:marble_tin_ore`), aren't added twice. A resource pack can replace any generated texture by providing `universal_ores:textures/block/<variant>.png`.

## 🐛 Reporting bugs
If you're running into bugs or other problems, feel free to open an issue on our [issue tracker](https://github.com/DawnTeamMC/UniversalOres/issues).

## 🔧 Contributing
Please refer to the [contributing guide](https://github.com/DawnTeamMC/UniversalOres/blob/master/CONTRIBUTING.md) for more information.

### Translations

The Dawn Team mods makes use of crowd-sourced translations using Crowdin.  
You can help translate the Dawn Team mods to any language supported in Minecraft by joining our team of avid translators [here](https://crowdin.com/project/dawnteam).

## ❤️ Support

[![Patreon supporters](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3DHugman%26type%3Dpatrons&style=for-the-badge)](https://patreon.com/Hugman)

You can support Universal Ores on the [Patreon page of the founder, main developer and maintainer of the Dawn Team mods (Hugman)](https://patreon.com/Hugman).

By supporting Hugman, you can get access to the following:

- Vote for the next features to be added to the Dawn Team mods
- Get exclusive screenshots of the next features to be added to Dawn Team mods
- Get early access to the latest beta versions of Dawn Team mods with new features
- Get early access to new mods from the Dawn Team mods

**We do not want to lock any in-game feature of the Dawn Team mods behind a paywall, because we believe that any Minecraft mod should forever remain free to download and fully exploit/use.**  
Supporting via Patreon is a more of way to help Hugman to continue to improve the mods and show the gratitude you might have towards Hugman's work.
Some money you donate may be used to pay for new features, such as music or art, but not all of it.

## 📜 License

Except where otherwise stated (see [third-party license notices](thirdparty/NOTICE.txt)), the content of this repository is provided
under the [Polyform Shield 1.0.0](LICENSE.md) license by [Hugman](https://github.com/Hugman76).
