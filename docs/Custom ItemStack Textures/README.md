# Custom ItemStack (Item) Textures

So, uhh... I'm bad at writing comprehensive guides, so I'll go with different approach!

Starting with version `0.3.5`, you can change textures of items based on what their name is or what their meta is. In other words, you can set different textures for different names and/or metadata values for the item.

Starting with version `0.3.7`, you can provide custom armor textures for armor items using this code, just add `"armorTexture"` next to `"texture"` and provide full texture path.

You can check example texturepacks:
- [**Example 1 - By (this) Guide**](https://github.com/tracystacktrace/StackEm/tree/main/docs/Custom%20ItemStack%20Textures/Example%201%20-%20By%20Guide)
- [**Example 2 - Asgard Giant Swords**](https://github.com/tracystacktrace/StackEm/tree/main/docs/Custom%20ItemStack%20Textures/Example%202%20-%20Asgard%20Giant%20Swords)
- [**Example 3 - Black Diamond Armor**](https://github.com/tracystacktrace/StackEm/tree/main/docs/Custom%20ItemStack%20Textures/Example%203%20-%20Black%20Diamond%20Armor)

The file where we will put all custom textures is named `stackem.items.json` and is located in the root section of a texturepack (next to `pack.txt`).

Let's go and check an example of `stackem.items.json`:
```json
{
  "data": [
    {
      "item": "item.diamond_sword",
      "onName": [
        {
          "equals": "Diamond Giant Sword",
          "texture": "myitems/diamond_giant_sword"
        }
      ]
    }
    
  ]
}
```

## Global List

As you can see, you can provide multiple texture swaps within a single file, just keep it inside `"data"` section:
```json5
{
 "data": [
   { /* item 1 */ },
   { /* item 2 */ },
   { /* item 3 */ },
 ] 
}
```

## Per Item Section

Now let's move to the part, where we create a pattern for a **single** item!

### I. Providing `"item"`

Let's say, we need to change the texture of `Iron Sword`. For this, we need to know either the internal name or its id. You can open ReIndev, hover at the iron sword and hold `LEFT CTRL` to unveal some more data.

The tooltip shows us:
```
Iron Sword
Durability: 256/256
#item.iron_sword:267
```

There are two different identifiers: `item.iron_sword` and `267`. You can use either of them:
```json5
{
  "item": "item.iron_sword",
  /* other code */
}
```
```json5
{
  "item": 267,
  /* other code */
}
```

### II. Swapping by meta

Now let's assume we need to change the texture with following meta:

```
The item will have texture0.png when its meta value is only 51
The item will have texture1.png when its meta value is below 13
The item will have texture2.png when its meta value is between 23 and 31
```

**ATTENTION!** Due to specific way of fetching texture for items (ReIndev), you need to locate item textures in this path: `textures/items`

We will put textures as `mycustompack/texture[X].png`, so we need to create a folder named `mycustompack` in `texture/items` and locate all textures here...

I will provide an example code, but you can check [**ARGS.md**](https://github.com/tracystacktrace/StackEm/blob/main/docs/Custom%20ItemStack%20Textures/ARGS.md) for deep technical information:
```json5
{
  "item": 267,
  
  "onMeta": [
    {
      "static": 51, //only 51 meta
      "texture": "mycustompack/texture0" //full path is texturePack.zip!/textures/items/mycustompack/texture0.png
    },
    {
      "below": 13, //below 13 (inclusive)
      "texture": "mycustompack/texture1" //full path is texturePack.zip!/textures/items/mycustompack/texture1.png
    },
    {
      "between": [23, 31],
      "texture": "mycustompack/texture2" //full path is texturePack.zip!/textures/items/mycustompack/texture2.png
    }
  ]
}
```

## III. Swapping by name

We did add changes by metadata value, but now let's add some changes by name!

```
The item will have cool_sword0.png when its name is strictly "Iron Sword Of Magicians"
The item will have cool_sword1.png when its name contains "Sigma Power"
The item will have cool_sword2.png when its name ends with "Hello Sword"
```

**ATTENTION!** Due to specific way of fetching texture for items (ReIndev), you need to locate item textures in this path: `textures/items`

We will put textures as `mycustompack/texture[X].png`, so we need to create a folder named `mycustompack` in `texture/items` and locate all textures here...
I will provide an example code, but you can check [**ARGS.md**](https://github.com/tracystacktrace/StackEm/blob/main/docs/Custom%20ItemStack%20Textures/ARGS.md) for deep technical information:
```json5
{
  "item": 267,
  
  "onMeta": [
    {
      "equals": "Iron Sword of Magicians", //trigger check
      "texture": "mycustompack/cool_sword0" //full path is texturePack.zip!/textures/items/mycustompack/cool_sword0.png
    },
    {
      "contains": "Sigma Power", //trigger check
      "texture": "mycustompack/cool_sword1" //full path is texturePack.zip!/textures/items/mycustompack/cool_sword1.png
    },
    {
      "endsWith": "Hello Sword", //trigget check
      "texture": "mycustompack/cool_sword2" //full path is texturePack.zip!/textures/items/mycustompack/cool_sword2.png
    }
  ]
}
```

## IV. Extra: Adding armor texture

For some items, especially those that can be equipped as an armor, you can provide a custom armor texture:
```json5
{
  "item": "item.iron_chestplate", // armor item
  
  "onName": {
    "equals": "Bronze Chestplate",
    "texture": "bronze_stuff/bronze_chestplate", //full path is texturePack.zip!/textures/items/bronze_stuff/bronze_chestplate.png
    "armorTextures": "/textures/models/armor/bronze_armor_1.png"
  }
}
```

[Check the "Example 3 - Black Diamond Armor" for full armor swap example.](https://github.com/tracystacktrace/StackEm/tree/main/docs/Custom%20ItemStack%20Textures/Example%203%20-%20Black%20Diamond%20Armor)