package de.liebki.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;

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

	private boolean ConfigStatus(String configPath) {
		return plugin.config.get(configPath);
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if (!ConfigStatus("messages.player.command.disableall")) {
			String commandText = e.getMessage();
			Player player = e.getPlayer();

			if (player.isOp()) {
				String playerName = player.getName();
				String messageToPushSend = "";

				for (Map.Entry<String, String> entry : commandMap.entrySet()) {
					if (commandText.startsWith(entry.getKey())) {
						boolean isActive = plugin.config.get(entry.getValue() + ".status");

						if (isActive) {
							if (entry.getKey().equals("/whitelist")) {
								messageToPushSend = plugin.config.get(entry.getValue() + ".content");
								messageToPushSend = messageToPushSend.replace("%CONTENT%", commandText)
										.replace("%PLAYER%", playerName);

							} else {
								String[] parts = commandText.split(" ");
								if (parts.length > 1) {
									String targetPlayer = parts[1];
									messageToPushSend = plugin.config.get(entry.getValue() + ".content");

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

	}

	@EventHandler
	public void OnPortalCreate(PortalCreateEvent event) {
		if (event.getEntity() instanceof Player && ConfigStatus("messages.player.portalcreation.status")) {
			try {
				String playerName = event.getEntity().getName();
				Location portalLocation = event.getBlocks().get(0).getLocation();

				World world = portalLocation.getWorld();
				String locationText = "[World: " + world + ", X: " + portalLocation.getX() + ", Y: "
						+ portalLocation.getY() + ",Z: " + portalLocation.getZ() + "]";

				String configMessage = plugin.config.get("messages.player.portalcreation.content");
				configMessage = configMessage.replace("%PLAYER%", playerName).replace("%LOCATION%", locationText);

				pushManager.SendMessage(configMessage);
			} catch (Exception e) {
				Bukkit.getConsoleSender().sendMessage("Error in OnPortalCreate(): \n" + e.getMessage());
			}
		}

	}

	@EventHandler
	public void OnPlayerAdvancement(PlayerAdvancementDoneEvent event) {
		if (ConfigStatus("messages.player.advancement.status")) {
			try {
				String playerName = event.getPlayer().getName();
				Advancement AdvancementObj = event.getAdvancement();

				String AdvancementName = AdvancementObj.getDisplay().getTitle();
				String configMessage = plugin.config.get("messages.player.advancement.content");

				configMessage = configMessage.replace("%PLAYER%", playerName).replace("%NAME%", AdvancementName);
				pushManager.SendMessage(configMessage);
			} catch (Exception e) {
				if (!e.getMessage().contains("Cannot invoke")) {
					Bukkit.getConsoleSender().sendMessage("Error in OnPlayerAdvancement(): \n" + e.getMessage());
				}
			}
		}
	}

	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event) {
		if (ConfigStatus("messages.player.death.status")) {
			String playerName = event.getEntity().getName();
			String deathCause = event.getDeathMessage();

			String configMessage = plugin.config.get("messages.player.death.content");
			configMessage = configMessage.replace("%PLAYER%", playerName).replace("%MESSAGE%", deathCause);

			pushManager.SendMessage(configMessage);
		}
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		if (ConfigStatus("messages.player.join.status")) {
			String playerName = event.getPlayer().getName();
			String configMessage = plugin.config.get("messages.player.join.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			pushManager.SendMessage(configMessage);
		}

		MessagePlayerCount();
	}

	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent event) {
		if (ConfigStatus("messages.player.leave.status")) {
			String playerName = event.getPlayer().getName();
			String configMessage = plugin.config.get("messages.player.leave.content");

			configMessage = configMessage.replace("%PLAYER%", playerName);
			pushManager.SendMessage(configMessage);
		}

		MessagePlayerCount();
	}

	/**
	 * If enabled, sends a push of the current and max players online
	 */
	private void MessagePlayerCount() {
		if (ConfigStatus("messages.player.count.status")) {
			String configMessage = plugin.config.get("messages.player.count.content");

			configMessage = configMessage.replace("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size() - 1))
					.replace("%MAX%", String.valueOf(Bukkit.getMaxPlayers()));
			pushManager.SendMessage(configMessage);
		}
	}

}
