## `stackem.items.json`

A primitive description of all arguments and possible values

## Attributes "Globally"

### `"data"`
- Explanation: The array that contains per-item changes
- Type: `json array`

Example code:
```json5
{
  "data": [
    { /* item 1... */ },
    { /* item 2... */ },
    { /* item 3... */ }
  ]
}
```

## Attributes "Per Item"

### `"item"`
- Explanation: The identifying name of the item. Use either this or `"id"`.
- Type: `string`

Example code:
```json5
{
  "item": "item.diamond_sword"
}
```

### `"id"`
- Explanation: The id (int) of the item. Use either this or `"item"`.
- Type: `int`

Example code:
```json5
{
  "id": 257, //id of IRON PICKAXE
}
```

### `"onMeta"`
- Explanation: The json array that contains rules for changing textures based by metadata
- Type: `json array`

Example code:
```json5
{
  "id": 257,
  
  "onMeta": [
    { /* rule 1... */ },
    { /* rule 2... */ },
    { /* rule 3... */ }
  ]
}
```

### `"onName"`
- Explanation: The json array that contains rules for changing textures based on display name
- Type: `json array`

Example code:
```json5
{
  "id": 257,
  
  "onName": [
    { /* rule 1... */ },
    { /* rule 2... */ },
    { /* rule 3... */ }
  ]
}
```

## Attributes for `"onMeta"` rules!

### `"texture"`
- Explanation: the texture id of the texture that will replace
- Type: `string`

Example code:
```json5
{
  "id": 257,
  
  "onMeta": [
    {
      "below": 100,
      "texture": "break_progress/iron_pickaxe_break_0" //full path will be texturePack.zip!/textures/items/break_progress/iron_pickaxe_break_0.png
    }
  ]
}
```

### `"priority"`
- Explanation: the priority of the rule, the bigger value - the more prioritised it is. `0` is default priority.
- Type: `int`
- Default value: `0`

Example code:
```json5
{
  "id": 257,
  
  "onMeta": [
    {
      "priority": 5,
      "below": 100,
      "texture": "break_progress/iron_pickaxe_break_0" //full path will be texturePack.zip!/textures/items/break_progress/iron_pickaxe_break_0.png
    }
  ]
}
```

### possible trigger checks
Trigger checks have the same pattern of:
```json5
{
  "<trigger name>": 1, //integer
  //BUT, BETWEEN and FOLLOWING USES OWN SYNTAX:
  "between": [0, 12],
  "following": [0, 1, 2, 3, 4]
}
```

List of trigger checks:
- `"static"`: Checks if the metadata is strictly equals the target int
- `"between"`: Checks if the metadata is between the two int (inclusive)
- `"below"`: Checks if the metadata is below the target int (exclusive)
- `"after"`: Checks if the metadata is after the target int (exclusive)
- `"follwoing"`: Checks if the metadata is present in the list of ints

Example code:
```json5
{
  "id": 257,

  "onMeta": [
    {
      "between": [0, 95], // basically checks if it lies between 0 and 95, including 0 and 95
      "texture": "break_progress/iron_pickaxe_break_0" //full path will be texturePack.zip!/textures/items/break_progress/iron_pickaxe_break_0.png
    }
  ]
}
```

## Attributes for `"onName"` rules!

### `"texture"`
- Explanation: the texture id of the texture that will replace
- Type: `string`

Example code:
```json5
{
  "id": 257,
  
  "onName": [
    {
      "equals": "Copper Pickaxe",
      "texture": "copper_tools/copper_pickaxe" //full path will be texturePack.zip!/textures/items/copper_tools/copper_pickaxe.png
    }
  ]
}
```

### `"priority"`
- Explanation: the priority of the rule, the bigger value - the more prioritised it is. `0` is default priority.
- Type: `int`
- Default value: `0`

Example code:
```json5
{
  "id": 257,
  
  "onMeta": [
    {
      "priority": 5,
      "below": 100,
      "texture": "break_progress/iron_pickaxe_break_0" //full path will be texturePack.zip!/textures/items/break_progress/iron_pickaxe_break_0.png
    }
  ]
}
```

### possible trigger checks
Trigger checks have the same pattern of:
```json5
{
  "<trigger name>": "<target string>"
}
```

List of trigger checks:
- `"equals"`: checks if the display name strictly equals the target string
- `"equalsIgnoreCase"`: checks if the display name strictly equals (but ignores case) the target string
- `"contains"`: checks if the display name contains the target string
- `"startsWith"`: checks if the display name starts with the target string
- `"endsWith"`: checks if the display name ends with the target string

Example code:
```json5
{
  "id": 257,

  "onName": [
    {
      "endsWith": "Copper Pickaxe", //will work even if it is "&5&lCopper Pickaxe"
      "texture": "copper_tools/copper_pickaxe" //full path will be texturePack.zip!/textures/items/copper_tools/copper_pickaxe.png
    }
  ]
}
```