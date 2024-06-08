package de.liebki;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Start extends JavaPlugin implements Listener {

	private Config config;

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		boolean IsActive = (boolean) config.get("messages.player.join.status");
		if (IsActive) {
			String playerName = event.getPlayer().getName();
			String configMessage = (String) config.get("messages.player.join.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			SendMessage(configMessage);
		}
	}

	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent event) {
		boolean IsActive = (boolean) config.get("messages.player.leave.status");
		if (IsActive) {
			String playerName = event.getPlayer().getName();
			String configMessage = (String) config.get("messages.player.leave.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			SendMessage(configMessage);
		}
	}

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

			config.set("messages.player.join.status", true);
			config.set("messages.player.join.content", "The player %PLAYER% joined!");

			config.set("messages.player.leave.status", true);
			config.set("messages.player.leave.content", "The player %PLAYER% left!");

			config.saveConfig();
		}

		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering on");

		boolean IsActive = (boolean) config.get("messages.general.startup.status");
		if (IsActive) {
			String configMessage = (String) config.get("messages.general.startup.content");
			SendMessage(configMessage);
		}

	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§4SimplePushEvents powering off");

		boolean IsActive = (boolean) config.get("messages.general.poweroff.status");
		if (IsActive) {
			String configMessage = (String) config.get("messages.general.poweroff.content");
			SendMessage(configMessage);
		}

	}

	private void SendMessage(String content) {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://ntfy.sh/" + (String) config.get("donottouch.pushchannel")))
				.POST(BodyPublishers.ofString(content))
				.setHeader("Title", (String) config.get("messages.general.title")).setHeader("Priority", "urgent")
				.setHeader("Content-Type", "application/x-www-form-urlencoded").build();

		try {
			client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			Bukkit.getConsoleSender().sendMessage("The message could not be sent using the push service!");
		}
	}

}