
## [0.3.7] - TBA

**[View changes between 0.3.6 with 0.3.7](https://github.com/tracystacktrace/StackEm/compare/0.3.6...0.3.7)**

- Some internal code changes: now the JSON parsing code should be much more stable.
- Removed `"id"` tag from `/stackem.items.json` json file: the `"item"` tag replaces its' functionality and handles both cases.
- Added `Eternal Pride` option in configs (enabled by default), which forces some textures (at this moment reindev logo at the main menu) to use pride variants.
- Added support to change the `F10` (force default textures) key in `Controls` menu.

## [0.3.6] - 2025-07-06

**[View changes between 0.3.5 with 0.3.6](https://github.com/tracystacktrace/StackEm/compare/0.3.5...0.3.6)**

- Added patch to provide custom GUI textures for Forge and Dimensional Chest:
  - Forge: `/textures/gui/container/forge.png`
  - Dimensional Chest: `/textures/gui/container/dimensional_chest.png`
- Added `"priority"` parameter to item texture swapping

## [0.3.5] - 2025-07-06

**[View changes between 0.3.4 with 0.3.5](https://github.com/tracystacktrace/StackEm/compare/0.3.4...0.3.5)**

- Added support for custom texture swap of items based on either name or metadata value
- Fixed a bug where modifications occur even with disabled texturepacks
- Fixed a bug where the mod would literally crawl non .zip files and try to process them lmao
- Fixed a bug where the mod would diabolically dictate own order of texturepacks for no reason
- Optimized a code related to receiving info about texturepack folder zip files
- Enforced UTF-8, so color chars could be read correctly
- Updated toolchain to FoxLoader `2.0-alpha30` (ReIndev `2.9_03`)

## [0.3.4] - 2025-07-03

**[View changes between 0.3.3 with 0.3.4](https://github.com/tracystacktrace/StackEm/compare/0.3.3...0.3.4)**

- Experimental `stackem.moon.json` with the support of moon cycles!
- Also, there is an equivalent `stackem.sun.json` that modifies the Sun!
- Added CharMap interface that is... basically a list of possible standard font characters


- Refurbished resource debug output, now it looks fancier
- Fixed a bug where sometimes texturepacks aren't loaded in order
- Fixed a bug where a main menu texturepacks button wouldn't load modified gui
- Fixed a typo: it's generic resources, not only audio stuff (in debug)

## [0.3.3] - 2025-06-29

**[View changes between 0.3.2 with 0.3.3](https://github.com/tracystacktrace/StackEm/compare/0.3.2...0.3.3)**

- Added ability to toggle stacked textures by pressing **F10**. This is useful when you need to quickly toggle textures to see changes
- The texturepack selection GUI has been slightly redesigned, now it looks better and bigger
- Removed unnecessary stabs and code for "possible features" (they did not become possible)
- Added `stackem.json` file support! Check README.md for more information and guides

## [0.3.2] - 2025-06-18

**[View changes between 0.3.1 with 0.3.2](https://github.com/tracystacktrace/StackEm/compare/0.3.1...0.3.2)**

- Fixed a possible memory leak related to binding thumbnail images
- Added Russian language support
- Fancy logger, no System.out.println over the code

## [0.3.1] - 2025-06-17
- Fixed a problem related to loading/unloading audio files in stacked texturepacks
- Implemented force audio cleanup

## [0.3] - 2025-06-17
- Fixed a bug where the code doesn't use stack's File instance (causes crash while getting audio files)
- Added `/textures/environment/particles.png` to auto-gluing code

Thanks `rerere284` for making segments list for "_particles.png_"!

## [0.2] - 2025-06-15
- Some small optimization inserted
- Fixed a bug when a texturepack list can freak out
- Temporarily hardcoded position of action buttons, will eventually be fixed in consequent versions
- Experimental: the mod now can manually join several same file textures by comparing pixels from vanilla. Right now, it is supported for following textures:
  - /textures/gui/effects.png
  - /textures/gui/gui.png
  - /textures/gui/icons.png
  - /textures/gui/web_buttons.png
  - /textures/gui/hud/hud.png
  - /textures/gui/stats/slot.png

Thanks to `toothydeerryte` and `rerere284` for help during this version development!

## [0.1] - 2025-06-14
- Initial release