# StackEm (Stack 'Em)

![Mod Logo](https://github.com/tracystacktrace/StackEm/raw/main/src/main/resources/assets/stackem/icon.png)

[![Available on - Modrinth](https://img.shields.io/badge/Available_on-Modrinth-4bab62?logo=modrinth&logoColor=white)](https://modrinth.com/mod/stack-em) [![GitHub release](https://img.shields.io/github/release/tracystacktrace/StackEm?include_prereleases=&sort=semver&color=success)](https://github.com/tracystacktrace/StackEm/releases/)

A FoxLoader (ReIndev) modification which allows to load and use several texturepacks simultaneously and adds customization features!

This is achieved by replacing a standard `ITexturePack` instance with custom one that handles multiple zip files `I/O` operations. All injections & source code manipulations are done within Mixins.

### Features:
- **Texturepack stacking and ordering:** several texturepacks can be enabled simultaneously and ordered, and the game will respect the order during resource fetching.
- **Additional description file:** The mod adds support for own additional texturepack description file `stackem.json` that aims to add more information on top of `pack.txt`. [Check the guide page for more information!](https://github.com/tracystacktrace/StackEm/tree/main/docs/Additional%20Description)
- **Custom item textures swap:** Like in `Optifine`, you can put a different texture into an item based either on its' display name or its' metadata value. Also, you can change the armor texture with this way. [Check the guide page for a tutorial and more information!](https://github.com/tracystacktrace/StackEm/blob/main/docs/Custom%20ItemStack%20Textures/README.md)
- **Texture _gluing_:** the mod will try to combine textures that's been changed by texturepacks by comparing against vanilla texture (see list of textures below). This is very helpful for micro texturepacks that change HUD textures, such as `Transgender Hearts Texturepack` and `Square (Aim) Dot`, etc., as they change the same texture file!
  - `/textures/gui/effects.png`
  - `/textures/gui/gui.png`
  - `/textures/gui/icons.png`
  - `/textures/gui/web_buttons.png`
  - `/textures/gui/hud/hud.png`
  - `/textures/gui/stats/slot.png`
  - `/textures/environment/particles.png`
- **Expanded moon and sun textures:** The mod provides an expansion for moon and sun textures, allowing for custom cycles, cycle ordering and a celestial scaling. [Check the guide page for more info!](https://github.com/tracystacktrace/StackEm/blob/main/docs/Expanded%20Moon%20Textures/README.md) 
- **Patched some GUI textures:** `Forge` and `Dimensional Chest` now have their own dedicated GUI textures:
  - `/textures/gui/container/forge.png`
  - `/textures/gui/container/dimensional_chest.png`
- **Patched sound reloading:** Now sound files withing texturepacks are fully reloaded on update, which fixes some audio related bugs.

### Screenshots:

![Texturepacks menu GUI](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_1.png)
![Texture gluing system](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_2.png)
![Custom texture swap](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_4.png)
![Custom armor texture](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_5.png)
![CharMap](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_3.png)

## Installation

Ensure you have [FoxLoader](https://github.com/Fox2Code/FoxLoader) installed. Click at the link and follow instructions (`Installation` section). Or in a nutshell, grab `*-mmc.zip` file and export it to MultiMC/PrismLauncher.

Simply download a `.jar` file and put it inside `mods` folder. That's all.

Want to compile by yourself? Just download the sources and run the following command:
```shell
./gradlew build
```

The output file will be located in `build/libs` folder.

## License

This mod is licensed under [Apache License 2.0](https://github.com/tracystacktrace/StackEm/blob/main/LICENSE)