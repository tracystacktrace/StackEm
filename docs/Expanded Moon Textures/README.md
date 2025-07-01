## `stackem.moon.json`

Since version `0.3.4` the mod allows you to configure the moon textures in a such way it can even support cycles!

This is created as an experiment of Stack 'Em capabilities and allows me to think of possible ways to expand this mod

## Attributes

### `path`
- Explanation: A path to the moon cycles `.png` file
- Type: `string`
- Default value: `/textures/environment/moon_phases.png`

Custom value example:
```json
{
  "path": "/textures/mytexturepack/moon_cool.png"
}
```

### `cycle`
- Explanation: the type of moon cycle it must follow
- Type: `string`
- Default value: `default`
- Allowed values: `default`, `reverse`, `random`

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