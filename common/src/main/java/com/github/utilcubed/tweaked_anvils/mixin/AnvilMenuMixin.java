package com.github.utilcubed.tweaked_anvils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.Items;
import net.objecthunter.exp4j.ExpressionBuilder;
import com.github.utilcubed.tweaked_anvils.config.TweakedAnvilsConfig;
import com.github.utilcubed.tweaked_anvils.util.MaxFunction;
import com.github.utilcubed.tweaked_anvils.util.MinFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.Map;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Inject(method = "createResult()V", at = @At("HEAD"))
    private void initShares(CallbackInfo ci,
            @Share("item1") LocalRef<ItemStack> item1Ref,
            @Share("item2") LocalRef<ItemStack> item2Ref,
            @Share("resultItem") LocalRef<ItemStack> resultItemRef,
            @Share("isRepairOp") LocalBooleanRef isRepairOpRef,
            @Share("repairAmount") LocalIntRef repairAmountRef,
            @Share("isEnchantOp") LocalBooleanRef isEnchantOpRef,
            @Share("isRenameOp") LocalBooleanRef isRenameOpRef) {
        repairAmountRef.set(0);
        isRepairOpRef.set(false);
        isEnchantOpRef.set(false);
        isRenameOpRef.set(false);
    }

    @Inject(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureStacks(CallbackInfo ci,
            @Local(ordinal = 0) ItemStack firstInputStack,
            @Local(ordinal = 1) ItemStack currentResultStack,
            @Local(ordinal = 2) ItemStack secondInputStack,
            @Share("item1") LocalRef<ItemStack> item1Ref,
            @Share("item2") LocalRef<ItemStack> item2Ref,
            @Share("resultItem") LocalRef<ItemStack> resultItemRef) {
        item1Ref.set(firstInputStack.copy());
        item2Ref.set(secondInputStack.isEmpty() ? ItemStack.EMPTY : secondInputStack.copy());
        resultItemRef.set(currentResultStack);
    }

    @ModifyArg(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", ordinal = 0), index = 0)
    private int captureMaterialRepair(int newDamage,
            @Local(ordinal = 1) ItemStack itemStackBeingRepaired,
            @Share("repairAmount") LocalIntRef repairAmountRef,
            @Share("isRepairOp") LocalBooleanRef isRepairOpRef) {
        int old = itemStackBeingRepaired.getDamageValue();
        int repaired = old - newDamage;
        if (repaired > 0) {
            repairAmountRef.set(repairAmountRef.get() + repaired);
            isRepairOpRef.set(true);
        }
        return newDamage;
    }

    @ModifyArg(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", ordinal = 1), index = 0)
    private int captureSacrificeRepair(int newDamage,
            @Local(ordinal = 1) ItemStack itemStackBeingRepaired,
            @Share("repairAmount") LocalIntRef repairAmountRef,
            @Share("isRepairOp") LocalBooleanRef isRepairOpRef) {
        int old = itemStackBeingRepaired.getDamageValue();
        int repaired = old - newDamage;
        if (repaired > 0) {
            repairAmountRef.set(repairAmountRef.get() + repaired);
            isRepairOpRef.set(true);
        }
        return newDamage;
    }

    @Inject(method = "createResult()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private void markEnchant(CallbackInfo ci,
            @Share("isEnchantOp") LocalBooleanRef isEnchantOpRef) {
        isEnchantOpRef.set(true);
    }

    @Inject(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setHoverName(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/world/item/ItemStack;"))
    private void markRenameSet(CallbackInfo ci,
            @Share("isRenameOp") LocalBooleanRef isRenameOpRef) {
        isRenameOpRef.set(true);
    }

    @Inject(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;resetHoverName()V"))
    private void markRenameReset(CallbackInfo ci,
            @Share("isRenameOp") LocalBooleanRef isRenameOpRef) {
        isRenameOpRef.set(true);
    }

    @ModifyArg(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V", ordinal = 5), index = 0)
    private int modifyFinalCost(int originalCost,
            @Local(name = "map") Map<Enchantment, Integer> finalEnchantmentMap,
            @Share("item1") LocalRef<ItemStack> item1Ref,
            @Share("item2") LocalRef<ItemStack> item2Ref,
            @Share("resultItem") LocalRef<ItemStack> resultItemRef,
            @Share("repairAmount") LocalIntRef repairAmountRef,
            @Share("isRepairOp") LocalBooleanRef isRepairOpRef,
            @Share("isEnchantOp") LocalBooleanRef isEnchantOpRef,
            @Share("isRenameOp") LocalBooleanRef isRenameOpRef) {
        var item1 = item1Ref.get();
        var item2 = item2Ref.get();

        if ((item1.isEmpty() || (item2.isEmpty() && !isRenameOpRef.get() ||
                (!item2.isEmpty() && !(item1.is(item2.getItem()) ||
                        item2.is(Items.ENCHANTED_BOOK) ||
                        (item1.isDamageableItem() && item1.getItem().isValidRepairItem(item1, item2))))))) {
            return originalCost;
        }

        int w1 = item1.getBaseRepairCost();
        int w2 = item2.isEmpty() ? 0 : item2.getBaseRepairCost();
        int w = Math.max(w1, w2);

        int n1 = tweaked_anvils$getEnchantmentCount(item1);
        int p1 = tweaked_anvils$calculateEnchantmentPoints(item1);

        int n2 = item2.isEmpty() ? 0 : tweaked_anvils$getEnchantmentCount(item2);
        int p2 = item2.isEmpty() ? 0 : tweaked_anvils$calculateEnchantmentPoints(item2);

        int nf = tweaked_anvils$getEnchantmentCount(finalEnchantmentMap);
        int pf = tweaked_anvils$calculateEnchantmentPoints(finalEnchantmentMap);

        int r = repairAmountRef.get();
        int n = isRenameOpRef.get() ? 1 : 0;
        double cost = 0;

        boolean isBookBeingApplied = !item1.is(Items.ENCHANTED_BOOK) && item2.is(Items.ENCHANTED_BOOK);

        System.out.println("DEBUG: Anvil cost calculation");

        if (isEnchantOpRef.get() && !isBookBeingApplied) {
            cost += new ExpressionBuilder(
                    AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig().getEnchantMergeSameFormula())
                    .variables("n1", "n2", "nf", "p1", "p2", "pf", "w1", "w2", "w", "r", "n")
                    .functions(new MaxFunction(), new MinFunction())
                    .build()
                    .setVariable("n1", n1)
                    .setVariable("n2", n2)
                    .setVariable("nf", nf)
                    .setVariable("p1", p1)
                    .setVariable("p2", p2)
                    .setVariable("pf", pf)
                    .setVariable("w1", w1)
                    .setVariable("w2", w2)
                    .setVariable("w", w)
                    .setVariable("r", r)
                    .setVariable("n", n)
                    .evaluate();
        } else if (isEnchantOpRef.get() && isBookBeingApplied) {
            cost += new ExpressionBuilder(
                    AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig().getEnchantApplyBookFormula())
                    .variables("n1", "n2", "nf", "p1", "p2", "pf", "w1", "w2", "w", "r", "n")
                    .functions(new MaxFunction(), new MinFunction())
                    .build()
                    .setVariable("n1", n1)
                    .setVariable("n2", n2)
                    .setVariable("nf", nf)
                    .setVariable("p1", p1)
                    .setVariable("p2", p2)
                    .setVariable("pf", pf)
                    .setVariable("w1", w1)
                    .setVariable("w2", w2)
                    .setVariable("w", w)
                    .setVariable("r", r)
                    .setVariable("n", n)
                    .evaluate();
        }
        if (isRepairOpRef.get()) {
            cost += new ExpressionBuilder(
                    AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig().getRepairFormula())
                    .variables("r", "w", "n1", "n2", "nf", "p1", "p2", "pf", "w1", "w2", "n")
                    .functions(new MaxFunction(), new MinFunction())
                    .build()
                    .setVariable("r", r)
                    .setVariable("w", w)
                    .setVariable("n1", n1)
                    .setVariable("n2", n2)
                    .setVariable("nf", nf)
                    .setVariable("p1", p1)
                    .setVariable("p2", p2)
                    .setVariable("pf", pf)
                    .setVariable("w1", w1)
                    .setVariable("w2", w2)
                    .setVariable("n", n)
                    .evaluate();
        }
        if (isRenameOpRef.get()) {
            cost += new ExpressionBuilder(
                    AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig().getRenameFormula())
                    .variables("n", "w", "n1", "n2", "nf", "p1", "p2", "pf", "w1", "w2", "r")
                    .functions(new MaxFunction(), new MinFunction())
                    .build()
                    .setVariable("n", n)
                    .setVariable("w", w)
                    .setVariable("n1", n1)
                    .setVariable("n2", n2)
                    .setVariable("nf", nf)
                    .setVariable("p1", p1)
                    .setVariable("p2", p2)
                    .setVariable("pf", pf)
                    .setVariable("w1", w1)
                    .setVariable("w2", w2)
                    .setVariable("r", r)
                    .evaluate();
        }
        var config = AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig();
        int finalCost;
        if (config.getCalculateUsingXp()) {
            finalCost = tweaked_anvils$levelFromXp((int)Math.round(cost * config.getXpPerUnit()));
        } else {
            finalCost = (int)Math.max(0, Math.round(cost));
        }
        System.out.println("cost: " + cost + ", finalCost: " + finalCost);
        return finalCost;
    }

    @Unique
    private static int tweaked_anvils$getEnchantmentCount(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return 0;
        return EnchantmentHelper.getEnchantments(stack).size();
    }

    @Unique
    private static int tweaked_anvils$getEnchantmentCount(Map<Enchantment, Integer> enchantments) {
        if (enchantments == null)
            return 0;
        return enchantments.size();
    }

    @Unique
    private static int tweaked_anvils$calculateEnchantmentPoints(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return 0;
        return tweaked_anvils$calculateEnchantmentPoints(EnchantmentHelper.getEnchantments(stack));
    }

    @Unique
    private static int tweaked_anvils$calculateEnchantmentPoints(Map<Enchantment, Integer> enchantments) {
        if (enchantments == null)
            return 0;
        var config = AutoConfig.getConfigHolder(TweakedAnvilsConfig.class).getConfig();
        double total = 0;
        for (var entry : enchantments.entrySet()) {
            var enchant = entry.getKey();
            int el = entry.getValue();
            int em = enchant.getMaxLevel();
            double ev = tweaked_anvils$getEv(enchant, config);
            double points = new ExpressionBuilder(config.getEnchantPointFormula())
                    .variables("el", "em", "ev")
                    .functions(new MaxFunction(), new MinFunction())
                    .build()
                    .setVariable("el", el)
                    .setVariable("em", em)
                    .setVariable("ev", ev)
                    .evaluate();
            total += points;
            System.out.println(enchant.getFullname(enchant.getMaxLevel()) + ": " + points);
        }
        return (int) Math.round(total);
    }

    @Unique
    private static double tweaked_anvils$getEv(Enchantment enchant, TweakedAnvilsConfig config) {
        double ev;
        if (enchant == Enchantments.MENDING) {
            ev = config.getEvMending();
        } else if (enchant.isCurse()) {
            ev = config.getEvCurse();
        } else {
            ev = switch (enchant.getRarity()) {
                case COMMON -> config.getEvCommon();
                case UNCOMMON -> config.getEvUncommon();
                case RARE -> config.getEvRare();
                case VERY_RARE -> config.getEvVeryRare();
            };
        }
        return ev;
    }

    @Unique
    private static int tweaked_anvils$levelFromXp(int xp) {
        if (xp <= 0)
            return 0;
        if (xp < 352) {
            // Level 0-16: xp = level^2 + 6*level
            // Solve quadratic: level^2 + 6*level - xp = 0
            return (int) Math.floor((-6 + Math.sqrt(36 + 4 * xp)) / 2);
        } else if (xp < 1507) {
            // Level 17-31: xp = 2.5*level^2 - 40.5*level + 360
            // 2.5l^2 - 40.5l + (360-xp) = 0
            return (int) Math.floor((40.5 + Math.sqrt(40.5 * 40.5 - 10 * (360 - xp))) / 5);
        } else {
            // Level 32+: xp = 4.5*level^2 - 162.5*level + 2220
            // 4.5l^2 - 162.5l + (2220-xp) = 0
            return (int) Math.floor((162.5 + Math.sqrt(162.5 * 162.5 - 18 * (2220 - xp))) / 9);
        }
    }
}
