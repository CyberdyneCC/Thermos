package thermos.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import net.minecraft.entity.Entity;
import net.minecraftforge.cauldron.entity.CraftCustomEntity;

public class CustomProjectileEntity extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter;
    private boolean doesBounce;

    public CustomProjectileEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public LivingEntity _INVALID_getShooter() {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void _INVALID_setShooter(LivingEntity shooter) {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.shooter = shooter;
    }

    @Override
    public boolean doesBounce() {
        return doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }
}

