package thermos.chaud;

import net.minecraft.entity.player.EntityPlayer;

public class DesUtils {

    public static boolean isModded()
    {
    	String pkg = sun.reflect.Reflection.getCallerClass().getPackage().getName();
    	return !(pkg.startsWith("net.minecraft") || pkg.startsWith("org.bukkit") || pkg.startsWith("cpw.mods.fml") || pkg.startsWith("org.spigotmc") || pkg.startsWith("thermos"));
    }
}
