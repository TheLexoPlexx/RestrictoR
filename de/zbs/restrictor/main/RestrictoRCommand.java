package de.zbs.restrictor.main;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.zbs.restrictor.api.AdvInventory;
import de.zbs.restrictor.api.ClickRunnable;
import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.api.Group;
import de.zbs.restrictor.api.GroupType;
import de.zbs.restrictor.api.PlayeR;
import de.zbs.restrictor.lang.Lang;
import de.zbs.restrictor.utils.ItemUtils;
import mkremins.fanciful.FancyMessage;

public class RestrictoRCommand implements CommandExecutor {
	

	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs instanceof Player) {
			final Player p = (Player) cs;
			PlayeR pr = PlayeR.getPlayeR(p);
			/*
			 * Infobefehl
			 */
			if (label.equalsIgnoreCase("restrictor")) {
				if (args.length == 0) {
					if (p.hasPermission("restrictor.info")) {
						p.sendMessage(Lang.line);
						p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + RestrictoR.plugin.getDescription().getName() + ChatColor.RESET + ChatColor.GRAY + "v" + RestrictoR.plugin.getDescription().getVersion() + " by " + ChatColor.BLUE + RestrictoR.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""));
						boolean group = true, user = false, newgroup = false, clearchat = true;
						if (p.hasPermission("restrictor.group")) {
							new FancyMessage(ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + "group <group>").tooltip(Lang.get("misc.help.groups", pr.getLanguage())).suggest("/group").send(p);
							group = false;
						}
						if (p.hasPermission("restrictor.user")) {
							new FancyMessage(ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + "user <player>").tooltip(Lang.get("misc.help.users", pr.getLanguage())).suggest("/user <player>").send(p);
							user = false;
						}
						if (p.hasPermission("restrictor.newgroup")) {
							new FancyMessage(ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + "newgroup <groupname> <prefix>").tooltip(Lang.get("misc.help.newgroup", pr.getLanguage())).suggest("/newgroup <groupname> <prefix>").send(p);
							newgroup = false;
						}
						if (p.hasPermission("restrictor.clearchat")) {
							new FancyMessage(ChatColor.DARK_GRAY + " - " + ChatColor.BLUE + "clearchat/chatclear").tooltip(Lang.get("misc.help.clearchat", pr.getLanguage())).suggest("/clearchat").send(p);
							clearchat = false;
						}
						if (group == true && user == true && newgroup == true && clearchat == true) {
							p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
						}
					    p.sendMessage(Lang.line);
					} else {
						p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
					}
				} else {
					p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
				}
								
			/*
			 * Gruppenbefehl
			 */
			} else if (label.equalsIgnoreCase("group") || label.equalsIgnoreCase("groups")) {
				if (p.hasPermission("restrictor.group")) {
					if (args.length == 0) {
						AdvInventory groups = new AdvInventory("Groups", 54, true);
						
						for (int i = 27; i < groups.getSize(); i++) {
							groups.setItem(ItemUtils.placeholder(DyeColor.RED), i);
						}
						
						int i = 27;
						Collection<Group> grps = Group.getAllGroups();
						if (grps.size() < 28) {
							for (Group grp : grps) {
								groups.setItem(new ItemStack(Material.SNOW_BALL), ChatColor.DARK_GREEN + grp.getName(), i, new ClickRunnable() {
									@Override
									public void run(InventoryClickEvent e) {
										Group.getGroup(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())).openGroupInventory(pr);
									}
								}, new String[] { Lang.get("group.item.clicktoedit", pr.getLanguage()) });
								i++;
							}
						} else {
							p.sendMessage(Lang.get("group.error.morethan27found", pr.getLanguage()));
							groups.setItem(ItemUtils.inventoryFailed("Groups"), 40);
						}
						groups.openInventory(pr);
					} else {
						p.sendMessage(Lang.get("command.toomanyarguments", pr.getLanguage()));
					}
				} else {
					p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
				}
				
				
			/*
			 * Debug-Befehl
			 */
			} else if (label.equalsIgnoreCase("debug") || label.equalsIgnoreCase("dev")) {
				if (p.getName().equals("G_3_N_I_S_Y_S") || p.getName().equals("decontamin4t0R")) {
					pr.sendTitle(0, 30, 0, "Nix eingestellt");
				} else {
					p.sendMessage(Lang.get("chat.info.onlyfordev", pr.getLanguage()));
				}
				
				
			/*
			 * Remove Permission from Group Befehl
			 */
			} else if (label.equalsIgnoreCase("removepermissiongroup")) {
				if (args.length == 1) {
					if (p.hasPermission("restrictor.group.edit")) {
						Group g = Group.getGroup(args[0].split("::")[0]);
						String permission = args[0].split("::")[1];
						World w = Bukkit.getWorld(args[0].split("::")[2]);
						
						g.removePermission(w, permission);
						
						p.sendMessage(Lang.get("group.success.removedpermission", pr.getLanguage()));
					} else {
						p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
					}
				}
				
				
			/*
			 * Remove Permission from User Befehl
			 */
			} else if (label.equalsIgnoreCase("removepermissionuser")) {
				if (args.length == 1) {
					if (p.hasPermission("restrictor.user.edit")) {
						PlayeR user = PlayeR.getPlayeR(UUID.fromString(args[0].split("::")[0]));
						String permission = args[0].split("::")[1];
						user.addPermission(permission);
						user.openUserInventory(PlayeR.getPlayeR(p));
					} else {
						p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
					}
				}
				
				
			/*
			 * Benutzerbefehl
			 */
			} else if (label.equalsIgnoreCase("user") || label.equalsIgnoreCase("player")) {
				if (p.hasPermission("restrictor.user")) {
					if (args.length == 0) {
						PlayeR.getPlayeR(p).openUserInventory(PlayeR.getPlayeR(p));
					} else if (args.length == 1) {
						if (!FileSystem.playerFile(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString()).exists()) {
							p.sendMessage(Lang.get("player.error.notfound", pr.getLanguage()));
						} else {
							PlayeR.getPlayeR(Bukkit.getOfflinePlayer(args[0])).openUserInventory(PlayeR.getPlayeR(p));
						}
					}
				} else {
					p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
				}
			/*
			 * Chatbefehl
			 */
			} else if (label.equalsIgnoreCase("clearchat")) {
				if (p.hasPermission("restrictor.clearchat")) {
					for (int i = 0; i < 300; i++) {
						for (Player op : Bukkit.getOnlinePlayers()) {
							if (!op.hasPermission("restrictor.clearchat.ignore")) {
								Bukkit.broadcastMessage(" ");
							}
						}
						Bukkit.broadcastMessage(Lang.get("chat.info.chatcleared", pr.getLanguage()));
					}
				} else {
					p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
				}
				
				
			/*
			 * Neue Gruppe
			 */
			} else if (label.equalsIgnoreCase("newgroup")) {
				if (p.hasPermission("restrictor.newgroup")) {
					if (args.length < 2) {
						p.sendMessage(Lang.get("command.toofewarguments", pr.getLanguage()));
						p.sendMessage("/newgroup <name> <prefix>");
					} else if (args.length >= 3) {
						p.sendMessage(Lang.get("command.toomanyarguments", pr.getLanguage()));
						p.sendMessage("/newgroup <name> <prefix>");
					} else if (args.length == 2) {
						String groupName = args[0].replace("-", "ZZZKXKZZZ");
						if (FileSystem.groupsFile(args[0]).exists()) {
							p.sendMessage(Lang.get("group.error.alreadyexists", pr.getLanguage()));
						} else if (args[0].length() > 24) {
							p.sendMessage(Lang.get("group.error.name24Chars", pr.getLanguage()));
						} else if (!StringUtils.isAlphanumeric(groupName)) {
							p.sendMessage(Lang.get("group.error.justAtoZandHyphens", pr.getLanguage()));
						} else {
							Group g = Group.createNewGroup(groupName.replace("ZZZKXKZZZ", "-"), args[1], GroupType.OTHER);
							p.sendMessage(Lang.get("group.error.creatednew", pr.getLanguage()));
							g.openGroupInventory(PlayeR.getPlayeR(p));
						}
					}
				} else {
					p.sendMessage(Lang.get("command.nopermission", pr.getLanguage()));
				}
//				
//				
//			/*
//			 * Friend-Befehl
//			 */
//			} else if (label.equalsIgnoreCase("friends") || label.equalsIgnoreCase("friend")) {
//				if (args.length == 0) {
//					p.sendMessage(Lang.get(Keys.Error.toofewarguments, language));
//					p.sendMessage("/friends <add/remove/list> [player]");
//				} else if (args.length == 1) {
//					if (args[0].equalsIgnoreCase("add")) {
//						p.sendMessage(Lang.get(Keys.Error.toofewarguments, language));
//						p.sendMessage("/friends add <player>");
//					} else if (args[0].equalsIgnoreCase("remove")) {
//						p.sendMessage(Lang.get(Keys.Error.toofewarguments, language));
//						p.sendMessage("/friends remove <player>");
//					} else if (args[0].equalsIgnoreCase("list")) {
//						if (p.hasPermission("restrictor.friends")) {
//							/*
//							 * List friends
//							 */
//						} else {
//							p.sendMessage(Lang.get(Keys.Error.nopermission, language));
//						}
//					} else {
//						p.sendMessage(Lang.get(Keys.Error.invalidSubCommand, language));
//					}
//				} else if (args.length == 2) {
//					if (args[0].equalsIgnoreCase("add")) {
//						if (p.hasPermission("restrictor.friends")) {
//							/*
//							 * Add friend
//							 */
//						} else {
//							p.sendMessage(Lang.get(Keys.Error.nopermission, language));
//						}
//					} else if (args[0].equalsIgnoreCase("remove")) {
//						if (FileSystem.fileConfig(FileSystem.playerFile(p)).getStringList("friends").contains(args[0])) {
//							if (p.hasPermission("restrictor.friends")) {
//								/*
//								 * Remove Friend
//								 */
//							} else {
//								p.sendMessage(Lang.get(Keys.Error.nopermission, language));
//							}
//						} else {
//							p.sendMessage(Lang.get(Keys.Error.playerNotFound, language));
//						}
//					} else if (args[0].equalsIgnoreCase("list")) {
//						p.sendMessage(Lang.get(Keys.Error.toomanyarguments, language));
//						p.sendMessage("/friends list");
//					} else {
//						p.sendMessage(Lang.get(Keys.Error.invalidSubCommand, language));
//					}
//				} else {
//					p.sendMessage(Lang.get(Keys.Error.toomanyarguments, language));
//				}
//				
//			/*
//			 * Broadcast
//			 */
//			} else if (label.equalsIgnoreCase("broadcast")) {
//				if (p.hasPermission("restrictor.broadcast")) {
//					if (args.length > 0) {
//						for (Player p1 : Bukkit.getOnlinePlayers()) {
//							PlayeR pr = new PlayeR(p1);
//							String s = args[1];
//							for (int i = 2; i < args.length; i++) {
//								s += " " + args[i];
//							}
//							if (args[0].equalsIgnoreCase("subtitle")) {
//								pr.sendSubTitle(5, 60, 5, s);
//							} else if (args[0].equalsIgnoreCase("actionbar")) {
//								pr.sendActionBar(s);
//							} else if (args[0].equalsIgnoreCase("title")) {
//								pr.sendTitle(5, 60, 5, s);
//							} else if (args[0].equalsIgnoreCase("chat")) {
//								p1.sendMessage(" ");
//								p1.sendMessage(Lang.line);
//								p1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.translateAlternateColorCodes('&', s));
//								p1.sendMessage(Lang.line);
//								p1.sendMessage(" ");
//							} else {
//								p.sendMessage(Lang.get(Keys.Error.invalidSubCommand, language));
//								p.sendMessage(ChatColor.GRAY + "Try: title/subitle/chat/actionbar");
//							}
//							p1.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
//						}
//					} else {
//						p.sendMessage(Lang.get(Keys.Error.toofewarguments, language));
//					}
//				} else {
//					p.sendMessage(Lang.get(Keys.Error.nopermission, language));
//				}
			}
		} else {
			RestrictoR.plugin.getLogger().warning("You need to be a Player to execute Commands");
		}
		return true;
	}
}