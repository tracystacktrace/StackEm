# StackEm (Stack 'Em)

A FoxLoader (ReIndev mod loader) modification that adds an ability to use several texturepacks at once!

No need to extract and combine several texturepacks into one big `.zip` file, just install the mod and select any of texturepacks you want to use within menu.

This is achieved by replacing a standard `ITexturePack` instance with custom one that handles multiple zip files `I/O` operations. All injections & source code manipulations are done within Mixins.

![Texturepacks menu GUI](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_1.png)
![Showcase of gluing system](https://github.com/tracystacktrace/StackEm/raw/main/docs/showcase_2.png)

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