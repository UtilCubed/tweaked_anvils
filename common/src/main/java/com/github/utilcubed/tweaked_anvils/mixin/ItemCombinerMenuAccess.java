package com.github.utilcubed.tweaked_anvils.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes private slots of ItemCombinerMenu to mixins.
 */
@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccess {
    @Accessor("inputSlots")
    Container getInputSlots();

    @Accessor("resultSlots")
    ResultContainer getResultSlots();
}
