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

## Part 1 - Understanding overall structure

### **I. Global list**

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

### **II. Per item section**

Now let's move