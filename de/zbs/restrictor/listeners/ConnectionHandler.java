package de.zbs.restrictor.listeners;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.api.PlayeR;
import de.zbs.restrictor.api.uuid.UUIDAPI;
import de.zbs.restrictor.lang.Lang;

public class ConnectionHandler implements Listener {
	
	/*
	 * PlayerJoinEvent, PlayerQuitEvent, PlayerKickEvent, ServerListPingEvent
	 */
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		PlayeR pr = PlayeR.getPlayeR(e.getPlayer());
		if (e.getResult() == Result.KICK_BANNED) {
			e.setKickMessage("KICKMESSAGE");
		} else if (e.getResult() == Result.KICK_FULL) {
			if (e.getPlayer().hasPermission("restrictor.joinfullserver")) {
				e.allow();
			} else {
				e.setKickMessage("KICKMESSAGE");
			}
		} else if (e.getResult() == Result.KICK_WHITELIST) {
			if (pr.getGroups().contains("Developer") || pr.getGroups().contains("Owner")) {
				e.allow();
			} else {
				e.setKickMessage("KICKMESSAGE");
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!(new File(FileSystem.playerdbPath, e.getPlayer().getUniqueId() + ".yml").exists())) {
			Lang.openInventory(PlayeR.getPlayeR(e.getPlayer()));
		}
		PlayeR pr = PlayeR.getPlayeR(e.getPlayer());
		pr.getSession().join();
//		pr.setTab("HEADER", "FOOTER");
		
		if (pr.getPreviousNames().size() > 0) {
			if (!(pr.getPreviousNames().get(pr.getPreviousNames().size() - 1).startsWith(e.getPlayer().getName()))) {
				pr.addPreviousName(e.getPlayer().getName() + "::" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "/" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
			}
		} else {
			pr.addPreviousName(e.getPlayer().getName() + "::" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "/" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
		}
		
		pr.checkForChatImage();
		pr.refreshPermissions();
 		e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		left(e.getPlayer());
 		e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		left(e.getPlayer());
 		e.setLeaveMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getDisplayName());
	}
	
	private void left(Player p) {
		PlayeR pr = PlayeR.getPlayeR(p);
		pr.getSession().left();
		
  		UUIDAPI.initializePlayer(p);
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if (Bukkit.hasWhitelist()) {
			e.setMotd("WHITELIST MOTD");
		} else {
//			File f = FileSystem.config();
//			FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
//			ArrayList<String> list = (ArrayList<String>) cfg.getStringList("randommotd");
//			Random r = new Random();
//			String random = list.get(r.nextInt(list.size() -1));
			e.setMotd("NORMAL MOTD");
		}
	}
}