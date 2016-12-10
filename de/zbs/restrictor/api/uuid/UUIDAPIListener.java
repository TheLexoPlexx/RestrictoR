package de.zbs.restrictor.api.uuid;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UUIDAPIListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUIDAPI.initializePlayer(p);
	}
}