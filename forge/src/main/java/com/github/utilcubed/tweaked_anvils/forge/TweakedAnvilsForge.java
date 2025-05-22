package com.github.utilcubed.tweaked_anvils.forge;

import net.minecraftforge.fml.common.Mod;
import com.github.utilcubed.tweaked_anvils.TweakedAnvils;

@Mod(TweakedAnvils.MOD_ID)
public final class TweakedAnvilsForge {
    public TweakedAnvilsForge() {
        // Run our common setup.
        TweakedAnvils.init();
    }
}
