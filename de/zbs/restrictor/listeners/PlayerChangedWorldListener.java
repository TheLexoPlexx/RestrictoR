package de.zbs.restrictor.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.zbs.restrictor.api.PlayeR;

public class PlayerChangedWorldListener implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.getFrom().getWorld() != e.getTo().getWorld()) {
			PlayeR.getPlayeR(e.getPlayer()).refreshPermissions();
		}
	}
}