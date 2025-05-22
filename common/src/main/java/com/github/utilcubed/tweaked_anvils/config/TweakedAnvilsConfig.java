package com.github.utilcubed.tweaked_anvils.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.jetbrains.annotations.NotNull;

@Config(name = "tweaked_anvils")
public class TweakedAnvilsConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    @Comment("Formula for experience cost when repairing items. See wiki for variables.")
    public String repairFormula = "r/200 + sqrt(w)";

    @ConfigEntry.Gui.Tooltip
    @Comment("Formula for experience cost when renaming items. See wiki for variables.")
    public String renameFormula = "n";

    @ConfigEntry.Gui.Tooltip
    @Comment("Formula for merging enchantments on the same items. See wiki for variables.")
    public String enchantMergeSameFormula = "pf - max(p1, p2)";

    @ConfigEntry.Gui.Tooltip
    @Comment("Formula for applying enchantment book. See wiki for variables.")
    public String enchantApplyBookFormula = "pf - p1 + n1";

    @ConfigEntry.Gui.Tooltip
    @Comment("Formula for calculating enchantment points. Variables: el, em, ev.")
    public String enchantPointFormula = "ev * 2^(el - 1)";

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for common enchantments.")
    public double evCommon = 1.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for uncommon enchantments.")
    public double evUncommon = 2.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for rare enchantments.")
    public double evRare = 4.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for very rare enchantments.")
    public double evVeryRare = 8.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for mending enchantment.")
    public double evMending = 16.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Rarity value for curses.")
    public double evCurse = 0.0;

    @ConfigEntry.Gui.Tooltip
    @Comment("If enabled, formulas calculate raw XP and convert to levels. If disabled, formulas are fixed level cost. Recommended ON.")
    public boolean calculateUsingXp = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Raw XP per unit of formula result, used if calculateUsingXp is enabled.")
    public int xpPerUnit = 20;

    @ConfigEntry.Gui.Excluded
    public static final String ARTICLE_LINK = "https://github.com/UtilCubed/tweaked_anvils/wiki";

    public @NotNull String getRepairFormula() {
        return repairFormula;
    }

    public @NotNull String getRenameFormula() {
        return renameFormula;
    }

    public @NotNull String getEnchantMergeSameFormula() {
        return enchantMergeSameFormula;
    }

    public @NotNull String getEnchantApplyBookFormula() {
        return enchantApplyBookFormula;
    }

    public @NotNull String getEnchantPointFormula() {
        return enchantPointFormula;
    }

    public double getEvCommon() {
        return evCommon;
    }

    public double getEvUncommon() {
        return evUncommon;
    }

    public double getEvRare() {
        return evRare;
    }

    public double getEvVeryRare() {
        return evVeryRare;
    }

    public double getEvMending() {
        return evMending;
    }

    public double getEvCurse() {
        return evCurse;
    }

    public boolean getCalculateUsingXp() {
        return calculateUsingXp;
    }

    public int getXpPerUnit() {
        return xpPerUnit;
    }
}
