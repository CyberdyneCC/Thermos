package org.bukkit.craftbukkit.util;

public class LongHash {
    public static long toLong(int msw, int lsw) {
    	return (long)msw & 4294967295L | ((long)lsw & 4294967295L) << 32;
    }

    public static int msw(long l) {
        return (int) (l >> 32);
    }

    public static int lsw(long l) {
        return (int) (l) + Integer.MIN_VALUE; // Spigot - remove redundant &
    }
}
