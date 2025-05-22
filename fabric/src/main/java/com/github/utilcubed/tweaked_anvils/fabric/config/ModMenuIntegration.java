package com.github.utilcubed.tweaked_anvils.fabric.config;

import com.github.utilcubed.tweaked_anvils.config.TweakedAnvilsConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TweakedAnvilsConfigScreen::buildScreen;
    }
}
