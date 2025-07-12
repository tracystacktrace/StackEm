## `stackem.moon.json`

Since version `0.3.4`, the mod allows you to configure the moon texture to support cycles!

All configuration is located within `stackem.moon.json` file, which is located in the root directory of a texturepack (next to `pack.txt`)

[You can check one of my moon cycle texturepacks as an example!](https://github.com/tracystacktrace/rind-broken-moon)

This is a complete example content of `stackem.moon.json`:
```json
{
	"path": "/textures/environment/moon_phases.png",
	"cycle": "default",
	"scale": 1.0,
	
	"number_x": 4,
	"number_y": 2
}
```

## Attributes (5)

### `path`
- Explanation: A path to the moon cycles `.png` file
- Type: `string`
- Default value: `"/textures/environment/moon_phases.png"`

Custom value example:
```json
{
  "path": "/textures/mytexturepack/moon_cool.png"
}
```

### `cycle`
- Explanation: the type of moon cycle it must follow
- Type: `string`
- Default value: `"default"`
- Allowed values: `"default"`, `"reverse"`, `"random"`

Custom value example:
```json
{
  "cycle": "random"
}
```

### `scale`
- Explanation: the scale of the moon during render
- Type: `float`
- Default value: `1.0`
- Allowed value: between `0.0` (exclusive) and `128.0` (inclusive)

Custom value example:
```json
{
  "scale": 3.0
}
```

### `number_x`
- Explanation: the number of moon cycles located horizontally on the texture file (modern mc has 4)
- Type: `int`
- Default value: `4`
- Allowed value: from `0` (exclusive)

Custom value example:
```json
{
  "number_x": 7
}
```

### `number_y`
- Explanation: the number of moon cycles located vertically on the texture file (modern mc has 2)
- Type: `int`
- Default value: `2`
- Allowed value: from `0` (exclusive)

Custom value example:
```json
{
  "number_y": 3
}
```