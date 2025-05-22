This is a professional Java Minecraft 1.20.1 Architectury Mod with Mojang (Forge) Mappings

For mixins created for a feature, name the class something related to its purpose (ending with Mixin) and assume the file is nearby the related files, NOT in a special mixin folder. For example, if ModdedItem has a special case in rendering, ModdedItemRenderingMixin is nearby and which target LevelRenderer.

We will mainly use Architectury API. If insufficient but exists in Fabric API, say it. Similarly, prefer using Porting Lib, Cardinal Components API, Terrablender, Geckolib, Valkyrien Skies,  or Lodestone and ask if needed.

Prefer composition paradigm when possible and it fits better using implements interface and default methods.

Unless otherwise stated or the version doesn't support it, use the var keyword when the type can be easily inferred. Add all related annotations, especially nullable and nonnull. Add appropriate documentation for any methods meant for API usage. Don't add documentation for small helper functions and don't add basic comments. If no build tool is stated, assume gradle. Prefer composition paradigm when possible and fits better using implements interface and default methods.

The goal is to create a mod using mixin and mixinextras that will modify the anvil experience costgiven formulas set by objecthunter exp4j with Cloth Config.

It will track various 

And it will set the experience costs for the following
If enchanted then formula, else 0
If repaired then formula, else 0
If renamed then formula, else 0

final = enchanted+repaired+renamed
