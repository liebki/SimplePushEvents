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
		if (!config.check("configexists")) {
			String serverUuid = (UUID.randomUUID() + Bukkit.getServer().getIp()).trim();
			System.out.println(serverUuid);

			config.set("donottouch.configexists", true);
			config.set("donottouch.pushchannel", serverUuid);

			config.set("messages.general.title", "Minecraft Server:");

			config.set("messages.general.startup.status", true);
			config.set("messages.general.startup.content", "The server is online now!");

			config.set("messages.general.poweroff.status", true);
			config.set("messages.general.poweroff.content", "The server is shutting down!");

			config.set("messages.player.command.op.status", true);
			config.set("messages.player.command.op.content", "The player %PLAYER% executed /op for %TARGET% !");

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