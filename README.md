[![Universal Ores](https://dawnteammc.github.io/universal_ores/images/header.png)](https://dawnteammc.github.io/universal_ores)

# 💎 Universal Ores

[![Latest release](https://img.shields.io/github/release/DawnTeamMC/UniversalOres.svg?label=Latest%20version&style=for-the-badge)](https://github.com/DawnTeamMC/UniversalOres/releases/latest)
[![CurseForge downloads](http://cf.way2muchnoise.eu/full_489884_downloads.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/universal-ores-fabric)

[![Discord user count](https://img.shields.io/discord/504608980799062036?label=&color=424549&labelColor=7289da&style=for-the-badge&logo=Discord&logoColor=DDE4EF)](https://discord.gg/8ksTVJu)
[![X (formerly Twitter) Follow](https://img.shields.io/twitter/follow/DawnTeamMC?style=for-the-badge&labelColor=424549&color=424549&logo=x)](https://twitter.com/DawnTeamMC)

[![License (Polyform Shield 1.0.0)](https://img.shields.io/badge/code%20license-Polyform%20Shield%201.0.0-green.svg?style=for-the-badge)](https://polyformproject.org/licenses/shield/1.0.0/)

Universal Ores is a mod for the latest version of Minecraft that adds new vanilla ores variants for andesite, 
diorite, granite, tuff, calcite, blackstone, basalt and smooth basalt that generate naturally where they should precisely be.

**You can visit the [official website for Universal Ores for more information](https://dawnteammc.github.io/universal_ores), or [explore the wiki »](https://dawnteammc.github.io/universal_ores/wiki)**

## 📦 Download
We use [CurseForge](https://www.curseforge.com/minecraft/mc-mods/universal-ores-fabric) and [Modrinth](https://modrinth.com/mod/universal_ores) to publish **stable builds** of Universal Ores for Minecraft.

Universal Ores is available for both **Fabric** and **NeoForge**.

⚠ On Fabric, Universal Ores **requires** Fabric API to be installed: [GitHub](https://github.com/FabricMC/fabric) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api) / [Modrinth](https://modrinth.com/mod/fabric-api)

On NeoForge, no other mod is required.

## 🧩 Adding stones and ores

Universal Ores makes a variant of every ore it knows of in every stone it knows of, with its own texture, drops, tags, recipes and world generation, all copied from the original ore when the game starts.

Any mod or modpack can add its stones and ores with a small JSON file, without code or dependency:

```json
{
  "stones": {
    "mymod:marble": "minecraft:stone"
  },
  "ores": {
    "mymod:tin_ore": "minecraft:stone"
  }
}
```

Mods put it in a `universal_ores/` folder at the root of their jar, modpacks in `config/universal_ores/`. The [wiki](https://dawnteammc.github.io/universal_ores/wiki) explains [the whole format](https://moddedmc.wiki/project/universal-ores/docs/adding_stones_and_ores) and [how the textures are drawn](https://moddedmc.wiki/project/universal-ores/docs/textures).

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
