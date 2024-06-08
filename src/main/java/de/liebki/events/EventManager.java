package de.liebki.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.liebki.Start;
import de.liebki.manager.PushManager;

public class EventManager implements Listener {

	private Start plugin;
	private PushManager pushManager;

	public EventManager(Start plugin, PushManager pushManager) {
		this.plugin = plugin;
		this.pushManager = pushManager;
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		boolean IsActive = (boolean) plugin.config.get("messages.player.command.op.status");

		if (IsActive && e.getPlayer().isOp() && e.getMessage().startsWith("/op")) {
			String configMessage = (String) plugin.config.get("messages.player.command.op.content");
			configMessage = configMessage.replace("%PLAYER%", e.getPlayer().getName()).replace("%TARGET%",
					e.getMessage().replace("/op ", ""));

			pushManager.SendMessage(configMessage);

		}

	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		boolean IsActive = (boolean) plugin.config.get("messages.player.join.status");

		if (IsActive) {
			String playerName = event.getPlayer().getName();
			String configMessage = (String) plugin.config.get("messages.player.join.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			pushManager.SendMessage(configMessage);
		}
	}

	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent event) {
		boolean IsActive = (boolean) plugin.config.get("messages.player.leave.status");

		if (IsActive) {
			String playerName = event.getPlayer().getName();
			String configMessage = (String) plugin.config.get("messages.player.leave.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			pushManager.SendMessage(configMessage);
		}
	}
}
