package de.liebki;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.liebki.events.EventManager;
import de.liebki.manager.PushManager;

public class Start extends JavaPlugin {

	public Config config;
	private PushManager pushManager;

	@Override
	public void onEnable() {
		config = new Config("plugins/simplepushevents", "options.yml", this);
		if (!config.check("donottouch.configexists")) {

			String uuid = CreateShortUuid();

			config.set("donottouch.configexists", true);
			config.set("donottouch.pushchannel", uuid);

			config.set("messages.general.title", "Minecraft Server:");

			config.set("messages.general.startup.status", true);
			config.set("messages.general.startup.content", "The server is online now!");

			config.set("messages.general.poweroff.status", true);
			config.set("messages.general.poweroff.content", "The server is shutting down!");

			config.set("messages.player.advancement.status", true);
			config.set("messages.player.advancement.content", "The player %PLAYER% unlocked the advancement: %NAME%");

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

			config.set("messages.player.join.status", true);
			config.set("messages.player.join.content", "The player %PLAYER% joined!");

			config.set("messages.player.leave.status", true);
			config.set("messages.player.leave.content", "The player %PLAYER% left!");

			config.saveConfig();
		}

		pushManager = new PushManager(this);
		EventManager eventManager = new EventManager(this, pushManager);

		getServer().getPluginManager().registerEvents(eventManager, this);
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering on");

		boolean IsActive = (boolean) config.get("messages.general.startup.status");
		if (IsActive) {
			String configMessage = (String) config.get("messages.general.startup.content");
			pushManager.SendMessage(configMessage);
		}

	}

	String CreateShortUuid() {
		String base = (UUID.randomUUID() + Bukkit.getServer().getIp()).trim();
		base = base.replace("-", "").substring(6, base.length() / 2);

		return base;
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering off");

		boolean IsActive = (boolean) config.get("messages.general.poweroff.status");
		if (IsActive) {
			String configMessage = (String) config.get("messages.general.poweroff.content");
			pushManager.SendMessage(configMessage);
		}

	}

}