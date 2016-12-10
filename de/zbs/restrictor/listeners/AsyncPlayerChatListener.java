package de.zbs.restrictor.listeners;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;

import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.api.Group;
import de.zbs.restrictor.api.PlayeR;
import de.zbs.restrictor.api.UserActions;
import de.zbs.restrictor.lang.Lang;
import de.zbs.restrictor.main.RestrictoR;
import mkremins.fanciful.FancyMessage;

public class AsyncPlayerChatListener implements Listener {
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(final AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		PlayeR p = PlayeR.getPlayeR(e.getPlayer());
		if (UserActions.EditGroup.isEditing(p)) {
			if (UserActions.EditGroup.getWorld(p) == null) {
				
			} else {
				
			}
		} else if (UserActions.EditUser.isEditing(p)) {
			UserActions.EditUser.get(p).addPermission(e.getMessage());
			UserActions.EditUser.stopEditing(p);
		} else {
			String message;
			if (e.getPlayer().hasPermission("restrictor.usecolourcodes")) {
				message = ChatColor.translateAlternateColorCodes('&', e.getMessage()).replace(ChatColor.RESET + "", ChatColor.GRAY + "");
			} else {
				message = e.getMessage();
			}
			String[] msgar = message.split(" ");
			for (String z : msgar) {
				if (z.length() > 4) {
					if (!e.getPlayer().hasPermission("restrictor.bypasscaps")) {
						if (StringUtils.isAllUpperCase(z)) {
							e.getPlayer().sendMessage(Lang.get("chat.info.capsoff", p.getLanguage()));
							return;
						}
					}
				}
			}
			FancyMessage msg = null;
			PlayeR pr = PlayeR.getPlayeR(e.getPlayer());
			String[] appender = new String[8];
			String uuid = ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + e.getPlayer().getUniqueId() + ChatColor.DARK_GRAY + "]";
			if (e.getPlayer().getDisplayName().equalsIgnoreCase(e.getPlayer().getName())) {
				appender[0] = ChatColor.GRAY + "Name: " + ChatColor.YELLOW + e.getPlayer().getDisplayName() + uuid;
			} else {
				appender[0] = ChatColor.GRAY + "Name: " + ChatColor.YELLOW + e.getPlayer().getDisplayName() + ChatColor .DARK_GRAY + " / " + ChatColor.YELLOW + e.getPlayer().getName() + uuid;
		    }
			appender[1] = ChatColor.GRAY + "Rang: " + pr.getChromeRankName() + ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + pr.getRank() + ChatColor.DARK_GRAY + "]";
			appender[2] = " ";
			appender[3] = " ";
			appender[4] = " ";
			appender[5] = " ";
			appender[6] = " ";
			if (p.getGroups().size() == 0) {
				appender[3] = ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + "Member" /* + ChatColor.DARK_GRAY + " - "*/;
			} else if (p.getGroups().size() < 4) {
				appender[2] = " ";
				int i = 3;
				for (Group g : p.getGroups()) {
					if (i < 6) {
						appender[i] = ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + g.getName();
						i++;
					}
				}
				appender[6] = " ";
			} else if (p.getGroups().size() < 6) {
				int i = 2;
				for (Group g : p.getGroups()) {
					if (i < 7) {
						appender[i] = ChatColor.DARK_GRAY + " > " + ChatColor.GRAY + g.getName();
						i++;
					}
				}
			} else {
				appender[3] = ChatColor.RED + "Zu viele Gruppen gefunden!";
			}
			appender[7] = ChatColor.GREEN + "► " + "Click to open User-Inventory";
			try {
		        File file;
		        if (new File(FileSystem.headsFolder(), e.getPlayer().getUniqueId().toString() + ".png").exists()) {
		        	file = new File(FileSystem.headsFolder(), e.getPlayer().getUniqueId() + ".png");
		        } else {
		        	file = new File(FileSystem.headsFolder(), "steve.png");
		        	pr.checkForChatImage();
		        }
		        BufferedImage imageToSend = null;
				try {
					imageToSend = ImageIO.read(file);
				} catch (IOException e1) {
					RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e1);
				}
		        String[] info = new ImageMessage(imageToSend, 8, ImageChar.BLOCK.getChar()).appendText(appender).getLines();
		        msg = new FancyMessage(
		        		p.getPrefix()
		        		+ " " + ChatColor.GRAY + e.getPlayer().getDisplayName())
		        	.tooltip(info)
		        	.command("/user " + e.getPlayer().getName())
		        	.then(ChatColor.DARK_GRAY + " » ")
		        	.then(message)
		        	.color(ChatColor.GRAY);
			} catch (Exception ex) {
				RestrictoR.plugin.getLogger().info("Failed retrieving playerhead for: " + e.getPlayer().getName() + ", activating fallback.");
		       	msg = new FancyMessage(
		       			p.getPrefix()
		       			+ " " + ChatColor.GRAY + e.getPlayer().getDisplayName())
		       		.tooltip(appender)
		       		.command("/user " + e.getPlayer().getName())
		       		.then(ChatColor.DARK_GRAY + " » ")
		       		.then(message)
		       		.color(ChatColor.GRAY);
			}
			for (Player player : Bukkit.getOnlinePlayers()) {
				msg.send(player);
			}
		}
	}
}