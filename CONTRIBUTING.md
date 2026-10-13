### Submitting translations
Localization of the Dawn Team mods is managed through the [Crowdin](https://crowdin.com/project/dawnteam) project.

### Submitting ideas
You can submit ideas for new features over on the [issue tracker](https://github.com/DawnTeamMC/UniversalOres/issues).

### Getting started for code modification
We're excited to hear that you're interested in contributing to Universal Ores!

Before getting started, you'll need to install the latest 64-bit version of the OpenJDK 8 for your environment.
- Windows users: We **strongly** recommend you use the Hotspot OpenJDK 8 builds provided by the [AdoptOpenJDK project](https://adoptopenjdk.net/) instead of the builds provided by Oracle.
- macOS and Linux users: If you are already using a package manager, OpenJDK builds should be present in your software repositories. If not, we recommend using [SDKMan](https://sdkman.io/) to install the Hotspot OpenJDK 8 builds provided by the [AdoptOpenJDK](https://adoptopenjdk.net/) project.

We strongly recommend you use [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) when making code contributions. While other IDEs may work (in theory, anyway), you will often run into issues and other roadblocks. If you're not familiar with setting up IntelliJ IDEA for use with Fabric projects, the community of Fabric has created a wiki which runs over a lot of the basics of Fabric [here](https://fabricmc.net/wiki/doku.php).

### Project layout

Universal Ores is built for Fabric and NeoForge from the same source:

- `common` holds the shared code and resources, compiled against vanilla Minecraft only. Its `client` source set holds client-only resources.
- `fabric` and `neoforge` hold each loader's entrypoints, metadata and implementation of `UniversalOresPlatform`, the interface through which the shared code reaches loader-specific APIs.
- The stones and ores Universal Ores knows of are listed in `common/src/main/resources/universal_ores/`, one file per mod (see [the wiki](https://moddedmc.wiki/project/universal-ores/docs/adding_stones_and_ores) for the format). Their variants' blocks are registered at startup for the stones and ores that are installed, and their textures are drawn at load time from the original ores.
- The data of the variants that always exist (vanilla ores in vanilla stones) is generated with Fabric's data generation (`./gradlew :fabric:runDatagen`) into `common/src/main/generated`, and shipped with both loaders, like the English translations. The variants of other mods get the same data at load time, since they depend on the installed mods.
- `./gradlew :common:test` checks the generated textures, `./gradlew :fabric:runGameTest` (add `-PcompatMods` to include the supported mods) checks the variants in game, and `./gradlew :common:previewOreTextures -Pmods=<jars>` draws the textures any mod would get, without starting the game.
- `-Pshaders` adds Sodium and Iris to `runClient`, to try the PBR maps of the variants with a shader pack and a PBR resource pack.
- The wiki lives in `docs`, in the format of the [Modded Minecraft Wiki](https://docs.moddedmc.wiki/). `./gradlew previewDocs` shows it locally, and `./gradlew :common:wikiImages` redraws the images of its textures page with the current generator.

Access wideners (Fabric) and access transformers (NeoForge) must be kept in sync: `fabric/src/main/resources/universal_ores.accesswidener` and `neoforge/src/main/resources/META-INF/accesstransformer.cfg`.

If you have any questions or issues, or would just like to discuss Universal Ores development, feel free to [join us on Discord](https://discord.gg/8ksTVJu).

### Creating pull requests
Please make sure before opening a pull request that:

- Your pull request has an overview of the changes it makes, along with a link to the open issue(s) it resolves, if applicable.
- Your changes include appropriate documentation and conform to our style guidelines.
- If your merge request contains multiple commits, that you squash them before submitting.
- You state in the description of your merge request that you agree to the Contributor License Agreement (CLA) found below.

### Contributor License Agreement
By submitting code, assets, or documentation to the repository you are hereby agreeing that:

- You grant Hugman the right to use your contributions under the [PolyForm Shield License 1.0.0](https://polyformproject.org/licenses/shield/1.0.0) license.
- Your contributions are of your own work and are free of legal restrictions (such as patents or copyrights).

If you have any questions about these terms, please get in contact with us.  
**If you do not agree to these terms, please do not submit contributions to this repository.**