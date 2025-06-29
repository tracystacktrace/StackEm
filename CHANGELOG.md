
## [0.3.3] - 2025-06-29

- Added ability to toggle stacked textures by pressing **F10**. This is useful when you need to quickly toggle textures to see changes

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