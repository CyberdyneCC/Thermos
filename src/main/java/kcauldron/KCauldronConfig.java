package kcauldron;

import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.configuration.BoolSetting;
import net.minecraftforge.cauldron.configuration.ConfigBase;
import net.minecraftforge.cauldron.configuration.Setting;
import net.minecraftforge.cauldron.configuration.StringSetting;

public class KCauldronConfig extends ConfigBase {
    public BoolSetting commandEnable = new BoolSetting(this, "command.enable",
            true, "Enable KCauldron command");
    public BoolSetting updatecheckerEnable = new BoolSetting(this,
            "updatechecker.enable", true, "Enable KCauldron update checker");
    public StringSetting updatecheckerSymlinks = new StringSetting(this,
            "updatechecker.symlinks", "KCauldron.jar", "(Re)create symlinks after update");
    public BoolSetting updatecheckerAutoinstall = new BoolSetting(this,
            "updatechecker.autoinstall", false, "Install updates without confirming");
    public BoolSetting updatecheckerAutorestart = new BoolSetting(this,
            "updatechecker.autorestart", false, "Restart server after updating without confirming (set restart script in spigot.yml)");
    public BoolSetting updatecheckerQuite = new BoolSetting(this,
            "updatechecker.quite", false, "Print less info during update");

    public BoolSetting loggingMaterialInjection = new BoolSetting(this,
            "logging.materialInjection", false, "Log material injection event");
    
    public BoolSetting commonAllowNetherPortal = new BoolSetting(this,
            "common.allowNetherPortalBesidesOverworld", false, "Allow nether portals in dimensions besides overworld");
    
    public KCauldronConfig() {
        super("kcauldron.yml", "kc");
        register(commandEnable);
        register(updatecheckerEnable);
        register(updatecheckerSymlinks);
        register(updatecheckerAutoinstall);
        register(updatecheckerAutorestart);
        register(updatecheckerQuite);
        register(loggingMaterialInjection);
        register(commonAllowNetherPortal);
        load();
    }

    private void register(Setting<?> setting) {
        settings.put(setting.path, setting);
    }

    @Override
    public void registerCommands() {
        if (commandEnable.getValue()) {
            super.registerCommands();
        }
    }

    @Override
    protected void addCommands() {
        commands.put(commandName, new KCauldronCommand());
    }

    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = "";
            for (Setting<?> toggle : settings.values()) {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: "
                            + toggle.def + " # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(
                        config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);
            save();
        } catch (Exception ex) {
            MinecraftServer.getServer().logSevere(
                    "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
