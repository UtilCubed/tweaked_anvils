package com.github.utilcubed.tweaked_anvils.util;

import net.objecthunter.exp4j.function.Function;

/**
 * exp4j function “max” taking exactly 2 arguments.
 */
public final class MaxFunction extends Function {
    public MaxFunction() {
        super("max", 2);
    }

    @Override
    public double apply(double... args) {
        return Math.max(args[0], args[1]);
    }
}