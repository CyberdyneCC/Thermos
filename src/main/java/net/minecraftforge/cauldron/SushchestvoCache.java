package net.minecraftforge.cauldron;

import net.minecraft.entity.Entity;

public class SushchestvoCache {

    public Class<? extends Entity> entityClass;
    public boolean tickNoPlayers = false;
    public int tickInterval = 1;
    public String configPath;
    public String worldName;

    public SushchestvoCache(Class<? extends Entity> entityClass, String worldName, String configPath, boolean tickNoPlayers, int tickInterval)
    {
        this.entityClass = entityClass;
        this.worldName = worldName;
        this.tickNoPlayers = tickNoPlayers;
        this.tickInterval = tickInterval;
        this.configPath = configPath;
    }
}
