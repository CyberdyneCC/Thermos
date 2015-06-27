package kcauldron;

import kcauldron.updater.CommandSenderUpdateCallback;
import kcauldron.updater.KCauldronUpdater;
import kcauldron.updater.KVersionRetriever;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KCauldronCommand extends Command {
    public static final String NAME = "kc";
    public static final String CHECK = NAME + ".check";
    public static final String UPDATE = NAME + ".update";
    public static final String RESTART = NAME + ".restart";

    public KCauldronCommand() {
    	super(NAME);

    	StringBuilder builder = new StringBuilder();
    	builder.append(String.format("/%s check - Check to update\n", NAME));
    	builder.append(String
    			.format("/%s update [version] - Update to specified or latest version\n",
    					NAME));
    	builder.append(String.format("/%s restart - Restart server\n", NAME));
    	setUsage(builder.toString());

    	setPermission("kcauldron");
    }

    public boolean testPermission(CommandSender target, String permission) {
    	if (testPermissionSilent(target, permission)) {
    		return true;
    	}
    	target.sendMessage(ChatColor.RED
    			+ "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
    	return false;
    }

    public boolean testPermissionSilent(CommandSender target, String permission) {
    	if (!super.testPermissionSilent(target)) {
    		return false;
    	}
    	for (String p : permission.split(";"))
    		if (target.hasPermission(p))
    			return true;
    	return false;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel,
    		String[] args) {
    	if (!testPermission(sender))
    		return true;
    	if (args.length == 0) {
    		sender.sendMessage(ChatColor.YELLOW + "Please specify action");
    		sender.sendMessage(ChatColor.AQUA + usageMessage);
    		return true;
    	}
    	String action = args[0];
    	if ("check".equals(action)) {
    		if (!testPermission(sender, CHECK))
    			return true;
    		sender.sendMessage(ChatColor.GREEN + "Initiated version check...");
    		KVersionRetriever.startServer(new CommandSenderUpdateCallback(
    				sender), false);
    	} else if ("update".equals(action)) {
    		KCauldronUpdater.initUpdate(sender, args.length > 1 ? args[1]
    				: null);
    	} else if ("restart".equals(action)) {
    		if (!testPermission(sender, RESTART))
    			return true;
    		KCauldron.restart();
    	} else {
    		sender.sendMessage(ChatColor.RED + "Unknown action");
    	}
    	return true;
    }

}
