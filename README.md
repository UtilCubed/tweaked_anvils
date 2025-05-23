# Tweaked Anvils

Tweaked Anvils is a Minecraft mod for 1.20.1+ that allows full customization of anvil XP cost formulas. It supports both Fabric and Forge platforms.

## Features
- Configure XP cost formulas for repairing, renaming, and enchanting items via anvil.
- Use mathematical expressions with variables for fine-grained control.
- Supports custom variables for enchantment points and rarity values.
- In-game configuration screen (via ModMenu on Fabric).
- Raw XP or vanilla level cost calculation modes.
- Built-in formula validation and helpful tooltips.

## Configuration
Access the configuration screen from ModMenu (Fabric) or via the config file. You can edit formulas for:
- Repair cost
- Rename cost
- Enchant merge (same items)
- Enchant apply (book)
- Enchantment point calculation

Variables and their meanings are explained in the config UI. See the [Wiki](https://github.com/UtilCubed/tweaked_anvils/wiki) for details.

## Building
This is a multiplatform project using Architectury. To build:

```sh
gradlew build
```

Artifacts for Fabric and Forge will be in their respective `build/libs` folders.

## Requirements
- Java 17+
- Minecraft 1.20.1
- Fabric Loader or Forge (see `gradle.properties` for versions)

## Credits
- UtilCubed, Team Aba
- Uses [exp4j](https://www.objecthunter.net/exp4j/) for formula parsing
- [Cloth Config](https://github.com/shedaniel/cloth-config) for config UI

## License
AGPL-3.0
