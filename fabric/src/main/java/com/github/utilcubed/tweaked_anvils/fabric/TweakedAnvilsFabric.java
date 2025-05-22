package com.github.utilcubed.tweaked_anvils.fabric;

import net.fabricmc.api.ModInitializer;

import com.github.utilcubed.tweaked_anvils.TweakedAnvils;

public final class TweakedAnvilsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        TweakedAnvils.init();
    }
}
