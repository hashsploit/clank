package net.hashsploit.clank;

import java.net.URL;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public class DiscordWebhook {
	
	private WebhookClient client;
	
	DiscordWebhook(URL url) {
		
		// Using the builder
		WebhookClientBuilder builder = new WebhookClientBuilder(url.toString()); // or id, token
		builder.setThreadFactory((job) -> {
		    Thread thread = new Thread(job);
		    thread.setName("Hello");
		    thread.setDaemon(true);
		    return thread;
		});
		builder.setWait(true);
		client = builder.build();
	}
	
	/**
	 * Send a normal message.
	 * @param content
	 */
	public void send(String content) {
		client.send(content);
	}
	
	/**
	 * Send a fancy embedded message.
	 * @param builder
	 */
	public void send(WebhookMessageBuilder builder) {
		//builder.setUsername("Minn");
		//builder.setAvatarUrl(avatarUrl);
		//builder.setContent("Hello World");
		client.send(builder.build());
	}
	
	/**
	 * Shutdown this webhook executor.
	 */
	protected void shutdown() {
		client.close();
	}
	
}
