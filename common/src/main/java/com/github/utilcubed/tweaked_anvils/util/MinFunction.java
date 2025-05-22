package com.github.utilcubed.tweaked_anvils.util;

import net.objecthunter.exp4j.function.Function;

/**
 * exp4j function “min” taking exactly 2 arguments.
 */
public final class MinFunction extends Function {
    public MinFunction() {
        super("min", 2);
    }

    @Override
    public double apply(double... args) {
        return Math.min(args[0], args[1]);
    }
}