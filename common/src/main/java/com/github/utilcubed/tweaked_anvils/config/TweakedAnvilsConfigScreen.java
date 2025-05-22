package com.github.utilcubed.tweaked_anvils.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import net.objecthunter.exp4j.ExpressionBuilder;
import com.github.utilcubed.tweaked_anvils.util.MaxFunction;
import com.github.utilcubed.tweaked_anvils.util.MinFunction;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class TweakedAnvilsConfigScreen {
        private static final Set<String> XP_COST_VARIABLES = Set.of(
                        "n1", "n2", "nf", "p1", "p2", "pf", "r", "n", "w1", "w2", "w");
        private static final Set<String> POINT_VARIABLES = Set.of(
                        "el", "em", "ev");

        public static void register() {
                me.shedaniel.autoconfig.AutoConfig.register(TweakedAnvilsConfig.class,
                                me.shedaniel.autoconfig.serializer.GsonConfigSerializer::new);
        }

        public static TweakedAnvilsConfig getConfig() {
                return me.shedaniel.autoconfig.AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig();
        }

        private static boolean isValidFormula(@Nullable String formula, Set<String> allowedVars) {
                if (formula == null || formula.isBlank())
                        return false;
                try {
                        new ExpressionBuilder(formula)
                                        .variables(allowedVars)
                                        .functions(new MaxFunction(), new MinFunction())
                                        .build();
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        public static Screen buildScreen(Screen parent) {
                var holder = me.shedaniel.autoconfig.AutoConfig.getConfigHolder(TweakedAnvilsConfig.class);
                var config = holder.getConfig();
                var builder = ConfigBuilder.create()
                                .setParentScreen(parent)
                                .setTitle(Component.translatable("config.tweaked_anvils.title"))
                                .setSavingRunnable(holder::save);
                var entryBuilder = builder.entryBuilder();
                ConfigCategory xpCost = builder
                                .getOrCreateCategory(Component.translatable("config.tweaked_anvils.category.xp_cost"));
                ConfigCategory points = builder
                                .getOrCreateCategory(Component.translatable("config.tweaked_anvils.category.points"));
                xpCost.addEntry(entryBuilder
                                .startTextDescription(
                                                Component.translatable("config.tweaked_anvils.explanation.header"))
                                .build());
                xpCost.addEntry(entryBuilder
                                .startTextDescription(
                                                Component.translatable("config.tweaked_anvils.explanation.variables"))
                                .build());
                points.addEntry(entryBuilder
                                .startTextDescription(
                                                Component.translatable(
                                                                "config.tweaked_anvils.explanation.point_variables"))
                                .build());
                Function<String, Optional<Component>> xpErrorSupplier = s -> isValidFormula(s, XP_COST_VARIABLES)
                                ? Optional.empty()
                                : Optional.of(Component.translatable("config.tweaked_anvils.formula.invalid"));
                Function<String, Optional<Component>> pointErrorSupplier = s -> isValidFormula(s, POINT_VARIABLES)
                                ? Optional.empty()
                                : Optional.of(Component.translatable("config.tweaked_anvils.formula.invalid"));
                xpCost.addEntry(entryBuilder
                                .startStrField(Component.translatable("config.tweaked_anvils.repairFormula"),
                                                config.repairFormula)
                                .setDefaultValue("r/200 + sqrt(w)")
                                .setTooltip(Component.translatable("config.tweaked_anvils.repairFormula.tooltip"))
                                .setSaveConsumer(v -> config.repairFormula = v)
                                .setErrorSupplier(xpErrorSupplier)
                                .build());
                xpCost.addEntry(entryBuilder
                                .startStrField(Component.translatable("config.tweaked_anvils.renameFormula"),
                                                config.renameFormula)
                                .setDefaultValue("n")
                                .setTooltip(Component.translatable("config.tweaked_anvils.renameFormula.tooltip"))
                                .setSaveConsumer(v -> config.renameFormula = v)
                                .setErrorSupplier(xpErrorSupplier)
                                .build());
                xpCost.addEntry(entryBuilder
                                .startStrField(Component.translatable("config.tweaked_anvils.enchantMergeSameFormula"),
                                                config.enchantMergeSameFormula)
                                .setDefaultValue("pf - max(p1, p2)")
                                .setTooltip(Component
                                                .translatable("config.tweaked_anvils.enchantMergeSameFormula.tooltip"))
                                .setSaveConsumer(v -> config.enchantMergeSameFormula = v)
                                .setErrorSupplier(xpErrorSupplier)
                                .build());
                xpCost.addEntry(entryBuilder
                                .startStrField(Component.translatable("config.tweaked_anvils.enchantApplyBookFormula"),
                                                config.enchantApplyBookFormula)
                                .setDefaultValue("pf - p1 + n1")
                                .setTooltip(Component
                                                .translatable("config.tweaked_anvils.enchantApplyBookFormula.tooltip"))
                                .setSaveConsumer(v -> config.enchantApplyBookFormula = v)
                                .setErrorSupplier(xpErrorSupplier)
                                .build());
                xpCost.addEntry(entryBuilder
                                .startTextDescription(
                                                Component.translatable("config.tweaked_anvils.xp_cost_explanation"))
                                .build());
                xpCost.addEntry(entryBuilder
                                .startBooleanToggle(Component.translatable("config.tweaked_anvils.calculateUsingXp"),
                                                config.calculateUsingXp)
                                .setDefaultValue(true)
                                .setTooltip(Component.translatable("config.tweaked_anvils.calculateUsingXp.tooltip"))
                                .setSaveConsumer(v -> config.calculateUsingXp = v)
                                .build());
                xpCost.addEntry(entryBuilder
                                .startIntField(Component.translatable("config.tweaked_anvils.xpPerUnit"),
                                                config.xpPerUnit)
                                .setDefaultValue(20)
                                .setTooltip(Component.translatable("config.tweaked_anvils.xpPerUnit.tooltip"))
                                .setSaveConsumer(v -> config.xpPerUnit = v)
                                .setErrorSupplier(v -> config.calculateUsingXp
                                                ? Optional.empty()
                                                : Optional.of(Component.translatable(
                                                                "config.tweaked_anvils.xpPerUnit.disabled")))
                                .build());
                points.addEntry(entryBuilder
                                .startStrField(Component.translatable("config.tweaked_anvils.enchantPointFormula"),
                                                config.enchantPointFormula)
                                .setDefaultValue("ev * 2^(el - 1)")
                                .setTooltip(Component.translatable("config.tweaked_anvils.enchantPointFormula.tooltip"))
                                .setSaveConsumer(v -> config.enchantPointFormula = v)
                                .setErrorSupplier(pointErrorSupplier)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evCommon"),
                                                config.evCommon)
                                .setDefaultValue(1.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evCommon.tooltip"))
                                .setSaveConsumer(v -> config.evCommon = v)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evUncommon"),
                                                config.evUncommon)
                                .setDefaultValue(2.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evUncommon.tooltip"))
                                .setSaveConsumer(v -> config.evUncommon = v)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evRare"), config.evRare)
                                .setDefaultValue(4.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evRare.tooltip"))
                                .setSaveConsumer(v -> config.evRare = v)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evVeryRare"),
                                                config.evVeryRare)
                                .setDefaultValue(8.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evVeryRare.tooltip"))
                                .setSaveConsumer(v -> config.evVeryRare = v)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evMending"),
                                                config.evMending)
                                .setDefaultValue(16.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evMending.tooltip"))
                                .setSaveConsumer(v -> config.evMending = v)
                                .build());
                points.addEntry(entryBuilder
                                .startDoubleField(Component.translatable("config.tweaked_anvils.evCurse"),
                                                config.evCurse)
                                .setDefaultValue(0.0)
                                .setTooltip(Component.translatable("config.tweaked_anvils.evCurse.tooltip"))
                                .setSaveConsumer(v -> config.evCurse = v)
                                .build());
                return builder.build();
        }
}
