package de.liebki;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.liebki.events.EventManager;
import de.liebki.manager.PushManager;

public class Start extends JavaPlugin {

	public KonfigurationsManager config;
	private PushManager pushManager;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			boolean isActive = config.get("messages.general.chatcommand.status");

			String commandPermission = config.get("messages.general.chatcommand.permission");
			if (player.hasPermission(commandPermission) && isActive) {
				String msg = String.join(" ", args);
				String configMessage = config.get("messages.general.chatcommand.content");

				configMessage = configMessage.replace("%PLAYER%", player.getName()).replace("%MESSAGE%", msg);
				pushManager.SendMessage(configMessage);
			}

		}

		return true;
	}

	@Override
	public void onEnable() {
		config = new KonfigurationsManager("plugins/simplepushevents", "options.yml", this);
		boolean configExists = config.check("donottouch.configexists");

		if (!configExists) {
			CreateConfigDefaults();
		}

		pushManager = new PushManager(this);
		EventManager eventManager = new EventManager(this, pushManager);

		getServer().getPluginManager().registerEvents(eventManager, this);
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering on");

		boolean isActive = config.get("messages.general.startup.status");
		if (isActive) {
			String configMessage = config.get("messages.general.startup.content");
			pushManager.SendMessage(configMessage);
		}
	}

	/**
	 * To create the default config values at the first starts or after deletion
	 */
	private void CreateConfigDefaults() {
		String uuid = CreateShortUuid();

		config.set("donottouch.configexists", true);
		config.set("donottouch.pushchannel", uuid);

		config.set("messages.general.title", "Minecraft Server:");

		config.set("messages.general.startup.status", true);
		config.set("messages.general.startup.content", "The server is online now!");

		config.set("messages.general.poweroff.status", true);
		config.set("messages.general.poweroff.content", "The server is shutting down!");

		config.set("messages.general.chatcommand.status", true);
		config.set("messages.general.chatcommand.content", "[%PLAYER%] wrote: %MESSAGE%");
		config.set("messages.general.chatcommand.permission", "spe.usechatcommand");

		config.set("messages.player.advancement.status", true);
		config.set("messages.player.advancement.content", "The player %PLAYER% unlocked the advancement: %NAME%");

		config.set("messages.player.join.status", true);
		config.set("messages.player.join.content", "The player %PLAYER% joined!");

		config.set("messages.player.leave.status", true);
		config.set("messages.player.leave.content", "The player %PLAYER% left!");

		config.set("messages.player.count.status", true);
		config.set("messages.player.count.content", "Players online right now: %ONLINE%/%MAX%");

		config.set("messages.player.death.status", true);
		config.set("messages.player.death.content", "The Player %PLAYER% died: %MESSAGE%");

		config.set("messages.player.portalcreation.status", true);
		config.set("messages.player.portalcreation.content", "The Player %PLAYER% created a portal at %LOCATION%");

		config.set("messages.player.command.disableall", false);

		config.set("messages.player.command.op.status", true);
		config.set("messages.player.command.op.content", "The player %PLAYER% executed /op for %TARGET% !");

		config.set("messages.player.command.deop.status", true);
		config.set("messages.player.command.deop.content", "The player %PLAYER% executed /deop for %TARGET% !");

		config.set("messages.player.command.ban.status", true);
		config.set("messages.player.command.ban.content", "The player %PLAYER% executed /ban for %TARGET% !");

		config.set("messages.player.command.banip.status", true);
		config.set("messages.player.command.banip.content", "The player %PLAYER% executed /ban-ip for %TARGET% !");

		config.set("messages.player.command.pardon.status", true);
		config.set("messages.player.command.pardon.content", "The player %PLAYER% executed /pardon for %TARGET% !");

		config.set("messages.player.command.pardonip.status", true);
		config.set("messages.player.command.pardonip.content",
				"The player %PLAYER% executed /pardon-ip for %TARGET% !");

		config.set("messages.player.command.whitelist.status", true);
		config.set("messages.player.command.whitelist.content",
				"The player %PLAYER% used a whitelist command: %CONTENT%");

		config.saveConfig();
	}

	/**
	 * To create a (hopefully) short UUID-like string, unique for every server
	 * 
	 * @return UUID-like string
	 */
	private String CreateShortUuid() {
		String base = (UUID.randomUUID() + Bukkit.getServer().getIp()).trim();
		base = base.replace("-", "").substring(8, base.length() / 2);

		return base;
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering off");

		boolean isActive = config.get("messages.general.poweroff.status");
		if (isActive) {
			String configMessage = config.get("messages.general.poweroff.content");
			pushManager.SendMessage(configMessage);
		}
	}

}