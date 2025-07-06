# Custom ItemStack (Item) Textures

So, uhh... I'm bad at writing comprehensive guides, so I'll go with different approach!

Starting with version `0.3.5`, you can change textures of items based on what their name is or what their meta is. In other words, you can set different textures for different names and/or metadata values for the item.

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

### I. Providing `"item"`/`"id"`

Let's say, we need to change the texture of an `iron sword`. For this, we need to know either the internal name or its id. You can open ReIndev, hover at the iron sword and hold `CTRL` to unveal some more data.

The tooltip shows us:
```
Iron Sword
Durability: 256/256
#item.iron_sword:267
```

There are two different identifiers: `item.iron_sword` and `267`. You can use either of them, but if you implement them correctly. In order to put a string identifier, you need to provide it in `"item"` section, like this:
```json5
{
  "item": "item.iron_sword",
  /* other code */
}
```

For a numerical id, you provide it inside `"id"` section:
```json5
{
  "id": 267,
  /* other code */
}
```

### II. Swapping by meta

