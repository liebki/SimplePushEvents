package de.liebki.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
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

	private static final Map<String, String> commandMap = new HashMap<>();

	static {
		commandMap.put("/op", "messages.player.command.op");
		commandMap.put("/deop", "messages.player.command.deop");
		commandMap.put("/ban", "messages.player.command.ban");
		commandMap.put("/ban-ip", "messages.player.command.banip");
		commandMap.put("/pardon", "messages.player.command.pardon");
		commandMap.put("/pardon-ip", "messages.player.command.pardonip");
		commandMap.put("/whitelist", "messages.player.command.whitelist");
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		String commandText = e.getMessage();
		Player player = e.getPlayer();

		if (player.isOp()) {
			String playerName = player.getName();
			String messageToPushSend = "";

			for (Map.Entry<String, String> entry : commandMap.entrySet()) {
				if (commandText.startsWith(entry.getKey())) {
					boolean isActive = (boolean) plugin.config.get(entry.getValue() + ".status");

					if (isActive) {
						if (entry.getKey().equals("/whitelist")) {
							messageToPushSend = (String) plugin.config.get(entry.getValue() + ".content");
							messageToPushSend = messageToPushSend.replace("%CONTENT%", commandText).replace("%PLAYER%",
									playerName);
						} else {
							String[] parts = commandText.split(" ");
							if (parts.length > 1) {
								String targetPlayer = parts[1];
								messageToPushSend = (String) plugin.config.get(entry.getValue() + ".content");

								messageToPushSend = messageToPushSend.replace("%PLAYER%", playerName)
										.replace("%TARGET%", targetPlayer);
							}
						}
					}
					break;
				}
			}

			if (!messageToPushSend.isEmpty()) {
				pushManager.SendMessage(messageToPushSend);
			}
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
