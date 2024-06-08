package de.liebki.manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import org.bukkit.Bukkit;

import de.liebki.Start;

public class PushManager {

	private Start plugin;

	public PushManager(Start plugin) {
		this.plugin = plugin;
	}

	public void SendMessage(String content) {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://ntfy.sh/" + (String) plugin.config.get("donottouch.pushchannel")))
				.POST(BodyPublishers.ofString(content))
				.setHeader("Title", (String) plugin.config.get("messages.general.title"))
				.setHeader("Priority", "urgent").setHeader("Content-Type", "application/x-www-form-urlencoded").build();

		try {
			client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			Bukkit.getConsoleSender().sendMessage("The message could not be sent using the push service!");
		}
	}
}
