## StackEm JSON File Guide

Starting from version `0.3.3`, the mod supports a QoL feature (I think) for texturepacks - **additional description file** `stackem.json`.

This file allows to specify the author's name (username), their website (or texturepack website) and category details.

This file is named `stackem.json` and it must be located in the root entry of a `.zip` texturepack (next to `pack.txt`).

So, there is a complete example of a `stackem.json`:

```json
{
  "category": {
    "id": ["qol", "queer"],
    "custom": ["Trans Rights!", "My favourite!"]
  },

  "author": "Author name or username",
  "website": "https://example.com"
}
```

If you don't want to include either category info or author info, you can

## Description of each parameter

### `"category"`

A root object for category. This is where you should put categories of your texturepack.

**There are two ways to include category:**
#### [1] Using in-built category list:

For this, you need to provide `"id"` parameter with a list of in-built category id(s).

In this example, we include `qol` (QoL features included) and `sound` (changes/adds sound):
```json
{
  /* ... */
  "category":{
    "id": ["qol", "sound"]
  }
  /* ... */
}
```

#### [2] Writing custom (own) categories