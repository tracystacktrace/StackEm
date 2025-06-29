## StackEm JSON File Guide

Starting from version `0.3.3`, the mod supports a QoL feature (I think) for texturepacks - **additional description file** `stackem.json`.  This file allows to specify an author's name (username), their website (or texturepack website) and the texturepack's categories.

This file is named `stackem.json` and it must be located in the root entry of a `.zip` texturepack (next to `pack.txt`).

So, there is a complete example of a `stackem.json`:

```json
{
  "author": "Author name or username",
  "website": "https://example.com",
  
  "category": {
    "id": ["qol", "queer"],
    "custom": ["Trans Rights!", "My favourite!"]
  }
}
```

All options are optional (lol)! You can include all of them, or any of the following elements, it's up to you!

If you don't want to include anything, simply don't create this file.

## Description of each parameter

### `author`

A string that represents an author's username or name.

Example:
```json
{
  "author": "Ellie the Greatest"
}
```

### `website`

A string that represent a texturepack's or an author's website. Better to start with either `http://` or `https://` as it's used in GUI as click to open link button.

Example:
```json
{
  "website": "https://example.com"
}
```

### `"category"`

A root object for category. This is where you should put categories of your texturepack.

There are two ways to include category, you can use either or them, both or even not include them at all.

#### [1] Using in-built category list:

For this, you need to provide `"id"` parameter with a list of in-built category id(s).

In this example, we include `qol` (QoL features included) and `sound` (changes/adds sound):
```json
{
  "category": {
    "id": ["qol", "sound"]
  }
}
```

#### [2] Writing custom (own) categories:

If you want to include custom categories, you need to provide `"custom"` paramater with a list of strings that represent category name(s).

In this example, we include `"Trans Rights!"` and `"Hello, world!"` as two custom categories:
```json
{
  "category": {
    "custom": ["Trans Rights!", "Hello, world!"]
  }
}
```
