# StackEm (Stack 'Em)

[![Available on - Modrinth](https://img.shields.io/badge/Available_on-Modrinth-4bab62?logo=modrinth&logoColor=white)](https://modrinth.com/mod/stack-em) [![GitHub release](https://img.shields.io/github/release/tracystacktrace/StackEm?include_prereleases=&sort=semver&color=success)](https://github.com/tracystacktrace/StackEm/releases/)

A FoxLoader (ReIndev mod loader) modification that adds an ability to use several texturepacks at once!

No need to extract and combine several texturepacks into one big `.zip` file, just install the mod and select any of texturepacks you want to use within menu.

This is achieved by replacing a standard `ITexturePack` instance with custom one that handles multiple zip files `I/O` operations. All injections & source code manipulations are done within Mixins.

![Texturepacks menu GUI](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_1.png)
![Showcase of gluing system](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_2.png)

## Features

- Texturepack stacking: several texturepacks can be enabled simultaneously
- Stack order: allows you to order texturepacks and the game will respect it when loading resources
- Some textures gluing system: the game will attempt to automatically glue several textures "together", making most hud texturepacks work together
    - `/textures/gui/effects.png`
    - `/textures/gui/gui.png`
    - `/textures/gui/icons.png`
    - `/textures/gui/web_buttons.png`
    - `/textures/gui/hud/hud.png`
    - `/textures/gui/stats/slot.png`
    - `/textures/environment/particles.png`
- Additional description file: you can [create an additional description file](https://github.com/tracystacktrace/StackEm/tree/main/docs/additional_description) in your texturepack to include authorship, website and categories!

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