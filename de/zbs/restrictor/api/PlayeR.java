package de.zbs.restrictor.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ImageEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import de.zbs.restrictor.lang.Lang;
import de.zbs.restrictor.lang.Language;
import de.zbs.restrictor.main.RestrictoR;
import de.zbs.restrictor.utils.ItemUtils;
import mkremins.fanciful.FancyMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class PlayeR {

	private static Map<String, PlayeR> pls = new HashMap<>();

	private String uuid; //UUID.toString mit bindestrichen: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
	private List<Group> groups;
	private List<String> permissions;
	private List<String> activity; /*   20.12.2014/13:45-16:02&19:09-23:99   */
	private List<String> previousNames;
	private List<String> friends;
	private Language language;
	private long coins;
	private long experience;
	private GameSession session;
	private OfflinePlayer player;
	private List<String> statistics;
	private AdvInventory myUserInv;
	
	private PlayeR(OfflinePlayer p) {
		this.player = p;
		this.uuid = p.getUniqueId().toString();
		if (new File(FileSystem.playerdbPath, uuid + ".yml").exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(getPlayerFile());
			List<String> tempGroups  = cfg.getStringList("perm.groups");
			this.groups = new ArrayList<>();
			for (String s : tempGroups) {
				this.groups.add(Group.getGroup(s));
			}
			this.permissions = cfg.getStringList("perm.permissions");
			this.activity = cfg.getStringList("info.activity");
			this.previousNames = cfg.getStringList("info.prevNames");
			this.friends = cfg.getStringList("info.friends");
			this.language = Language.getLanguageFromString(cfg.getString("info.lang"));
			this.coins = cfg.getInt("eco.coins");
			this.experience = cfg.getInt("eco.experience");
			this.session = new GameSession(this);
			this.statistics = cfg.getStringList("statistics.list");
		} else {
			this.groups = new ArrayList<Group>();
			this.permissions = new ArrayList<String>();
			this.activity = new ArrayList<String>();
			this.previousNames = new ArrayList<String>();
			this.friends = new ArrayList<String>();
			String cfgstring = getPlayerFileConfiguration().getString("info.lang");
			if (new File(FileSystem.languagesPath, cfgstring + ".yml").exists()) {
				this.language = Language.getLanguageFromString(cfgstring);
			} else {
				this.language = new Language(Locale.GERMAN);
			}
			this.coins = 0;
			this.experience = 0;
			this.session = new GameSession(this);
			this.statistics = new ArrayList<String>();
			save();
		}
	}
	
	public void save() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getPlayerFile());
		List<String> tempGroups = new ArrayList<String>();
		if (!getGroups().isEmpty()) {
			for (Group g : getGroups()) {
				tempGroups.add(g.getName());
			}
		}
		cfg.set("perm.groups", tempGroups);
		cfg.set("perm.permissions", this.permissions);
		cfg.set("info.activity", this.activity);
		cfg.set("info.prevNames", this.previousNames);
		cfg.set("info.lang", language.getLocale().getLanguage());
		cfg.set("info.friends", this.friends);
		cfg.set("eco.coins", this.coins);
		cfg.set("eco.experience", this.experience);
		try {
			cfg.save(getPlayerFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /* ============================================================={ Getter }============================================================= */
	
	public static PlayeR getPlayeR(OfflinePlayer p) {
		if (!pls.containsKey(p.getUniqueId().toString())) {
			pls.put(p.getUniqueId().toString(), new PlayeR(p));
		}
		return pls.get(p.getUniqueId().toString());
	}

	public static PlayeR getPlayeR(Player player) {
		return getPlayeR((OfflinePlayer) player);
	}

	public static PlayeR getPlayeR(UUID uuid) {
		return getPlayeR(Bukkit.getOfflinePlayer(uuid));
	}

	public static PlayeR getPlayeR(File file) {
		return getPlayeR(Bukkit.getOfflinePlayer(UUID.fromString(file.getName().replace(".yml", ""))));
	}

	public static PlayeR getPlayeR(HumanEntity he) {
		return getPlayeR((OfflinePlayer) he);
	}
	
	 /* ============================================================={ UUID }============================================================= */
	
	public String getUUID() {
		return uuid;
	}
	 /* ============================================================={ Groups }============================================================= */

	public List<Group> getGroups() {
		return groups;
	}
	
	public boolean addGroup(Group group) {
		groups.add(group);
		if (groups.size() > 5) {
			groups.remove(group);
			save();
			return false;
		} else {
			return true;
		}
	}
	
	public void removeGroup(Group group) {
		groups.remove(group);
		save();
	}
	 /* ============================================================={ Permissions }============================================================= */

	public List<String> getPermissions() {
		return permissions;
	}
	
	public void addPermission(String permission) {
		permissions.add(permission);
		save();
	}
	
	public void removePermission(String permission) {
		permissions.remove(permission);
		save();
	}
	
	public void refreshPermissions() {
		for (String s : permissions) {
			if (s.startsWith("-")) {
				getPlayer().addAttachment(RestrictoR.plugin, s, true);
			} else {
				getPlayer().addAttachment(RestrictoR.plugin, s, false);
			}
		}
	}
	
	 /* ============================================================={ Activity }============================================================= */
	
	public List<String> getActivity() {
		return activity;
	}
	 /* ============================================================={ Previous Names }============================================================= */

	public List<String> getPreviousNames() {
		return previousNames;
	}
	
	public void addPreviousName(String name) {
		previousNames.add(name);
		save();
	}
	
	public void removePreviousName(String name) {
		previousNames.remove(name);
		save();
	}

	 /* ============================================================={ Friends }============================================================= */

	public List<String> getFriends() {
		return friends;
	}
	
	public void addFriend(UUID uuid) {
		friends.add(uuid.toString());
		save();
	}
	
	public void removeFriend(UUID uuid) {
		friends.remove(uuid.toString());
		save();
	}
	
	 /* ============================================================={ Language }============================================================= */
	
	public void setLanguage(Language lang) {
		this.language = lang;
		save();
	}
	
	public Language getLanguage() {
		return this.language;
	}
	
	
	 /* ============================================================={ Economy }============================================================= */
	
	public long getCoins() {
		return coins;
	}
	
	public void addCoins(int amount) {
		this.coins += amount;
		save();
	}

	public long getExperience() {
		return experience;
	}
	 
	public void addExperience(int amount) {
		Integer r = getRank();
		this.experience += amount;
		if (getRank() > r) {
			playSound(Sound.LEVEL_UP);
			sendTitle(5, 100, 30, ChatColor.RED + "" + ChatColor.MAGIC + "XXX" + ChatColor.RESET + ChatColor.GOLD + "   LEVEL  UP!   " + ChatColor.RED + "" + ChatColor.MAGIC + "XXX");
			sendSubTitle(5, 100, 30, ChatColor.YELLOW + "You are now Level " + ChatColor.GREEN + getRank());
			if (RestrictoR.effectlibenabled) {
				if (FileSystem.levelupimg().exists()) {
					try {
						EffectManager manager = new EffectManager(RestrictoR.plugin);
						final ImageEffect imageEffect = new ImageEffect(manager);
						imageEffect.setDynamicOrigin(new DynamicLocation(getPlayer().getLocation().add(0, 5, 0)));
						imageEffect.loadFile(FileSystem.levelupimg());
						imageEffect.particle = ParticleEffect.SLIME;
						imageEffect.enableRotation = true;
						imageEffect.start();
						Bukkit.getScheduler().runTaskLater(RestrictoR.plugin, new Runnable() {
							@Override
							public void run() {
								imageEffect.cancel();
							}
						}, 60);
					} catch (IOException e) {
						RestrictoR.plugin.getLogger().warning("LevelUp-Image not found!");
					}
				} else {
					RestrictoR.plugin.getLogger().warning("LevelUp-Image not found!");
				}
			}
		}
		save();
	}
	 
	public int getRank() {
		if (experience < 100) {
			return 1;
		} else if (experience < 1500) {
			return 2;
		} else if (experience < 1500) {
			return 3;
		} else if (experience < 3700) {
			return 4;
		} else if (experience < 7100) {
			return 5;
		} else if (experience < 12300) {
			return 6;
		} else if (experience < 20000) {
			return 7;
		} else if (experience < 29000) {
			return 8;
		} else if (experience < 41000) {
			return 9;
		} else if (experience < 57000) {
			return 10;
		} else if (experience < 76000) {
			return 11;
		} else if (experience < 98000) {
			return 12;
		} else if (experience < 125000) {
			return 13;
		} else if (experience < 156000) {
			return 14;
		} else if (experience < 192000) {
			return 15;
		} else if (experience < 233000) {
			return 16;
		} else if (experience < 280000) {
			return 17;
		} else if (experience < 332000) {
			return 18;
		} else if (experience < 390000) {
			return 19;
		} else if (experience < 455000) {
			return 20;
		} else if (experience < 527000) {
			return 21;
		} else if (experience < 606000) {
			return 22;
		} else if (experience < 692000) {
			return 23;
		} else if (experience < 787000) {
			return 24;
		} else if (experience < 889000) {
			return 25;
		} else if (experience < 1000000) {
			return 26;
		} else if (experience < 1122000) {
			return 27;
		} else if (experience < 1255000) {
			return 28;
		} else if (experience < 1400000) {
			return 29;
		} else {
			return 30;
		}
	}
	 
	public String getChromeRankName() {
		switch (getRank()) {
			case 1:
				return ChatColor.DARK_GRAY + "Recruit";
			case 2:
				return ChatColor.DARK_GRAY + "Bronze 1";
			case 3:
				return ChatColor.DARK_GRAY + "Bronze 2";
			case 4:
				return ChatColor.DARK_GRAY + "Bronze 3";
			case 5:
				return ChatColor.DARK_GRAY + "Bronze Elite";
			case 6:
				return ChatColor.DARK_GRAY + "Bronze Elite Master";
			case 7:
				return ChatColor.GRAY + "Silver 1";
			case 8:
				return ChatColor.GRAY + "Silver 2";
			case 9:
				return ChatColor.GRAY + "Silver 3";
			case 10:
				return ChatColor.GRAY + "Silver ELite";
			case 11:
				return ChatColor.GRAY + "Silver Elite Master";
			case 12:
				return ChatColor.GOLD + "Gold 1";
			case 13:
				return ChatColor.GOLD + "Gold 2";
			case 14:
				return ChatColor.GOLD + "Gold 3";
			case 15:
				return ChatColor.GOLD + "Gold Elite";
			case 16:
				return ChatColor.GOLD + "Gold Elite Master";
			case 17:
				return ChatColor.RED + "Nova 1";
			case 18:
				return ChatColor.RED + "Nova 2";
			case 19:
				return ChatColor.RED + "Nova 3";
			case 20:
				return ChatColor.RED + "Nova Elite";
			case 21:
				return ChatColor.RED + "Nova Elite Master";
			case 22:
				return ChatColor.DARK_PURPLE + "Master Guardian 1";
			case 23:
				return ChatColor.DARK_PURPLE + "Master Guardian 2";
			case 24:
				return ChatColor.DARK_PURPLE + "Master Guardian 3";
			case 25:
				return ChatColor.DARK_PURPLE + "Master Guardian Elite";
			case 26:
				return ChatColor.DARK_PURPLE + "Distinguised Master Guardian";
			case 27:
				return ChatColor.AQUA + "Legendary Eagle";
			case 28:
				return ChatColor.AQUA + "Legendary Eagle Master";
			case 29:
				return ChatColor.DARK_AQUA + "Supreme Master First Class";
			case 30:
				return ChatColor.DARK_AQUA + "Global Elite";
			default:
				return "Fehler";
		}
	}

	 /* ============================================================={ GameSession }============================================================= */
	
	public GameSession getSession() {
		return session;
	}
	
	public static class GameSession {

		private PlayeR player;
		private String join = null;
		private String left = null;
		private List<String> sessions = new ArrayList<String>();
		
		GameSession(PlayeR player) {
			this.player = player;
		}
		
		public void join() {
			join = new SimpleDateFormat("HH:mm:ss").format(new Date());
		}
		
		public void left() {
			left = new SimpleDateFormat("HH:mm:ss").format(new Date());
			sessions.add(join + "-" + left);
			join = null;
			left = null;
		}
		
		public void nextDay() {
			if (join == null) {
				finishSession();
			} else {
				left();

				join();
			}
		}
		
		private void finishSession() {
			String times = "";
			for (String s : sessions) {
				times += s + "&";
			}
			this.player.activity.add(new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "/" + times);
			sessions.clear();
		}
	}

	 /* ============================================================={ Statistics }============================================================= */
	
	public List<String> getStatistics() {
		return statistics;
	}
	
	public class Statistic {
		
		private String basePath;
		
		public Statistic(String basePath) {
			this.basePath = basePath;
		}
		
		public void setStatistic(String name, String value) {
			getPlayerFileConfiguration().set("statistics." + basePath + "." + name, value);
		}
		
		public String getStatistic(String name) {
			return getPlayerFileConfiguration().getString("statistics." + basePath + "." + name);
		}
	}

	 /* ============================================================={ Statistics }============================================================= */
	
	public File getPlayerFile() {
		return FileSystem.playerFile(this);
	}
	
	public FileConfiguration getPlayerFileConfiguration() {
		return FileSystem.fileConfig(getPlayerFile());
	}
		
	 /* ============================================================={ Base-Methods }============================================================= */
	
	public ItemStack getInfoItem() {
		ItemStack playerinfo = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	    SkullMeta meta = (SkullMeta) playerinfo.getItemMeta();
	    meta.setOwner(getPlayer().getName());
	    playerinfo.setItemMeta(meta);
		ItemMeta playerinfometa = playerinfo.getItemMeta();
		if (getPlayer().isOnline()) {
			playerinfometa.setDisplayName(ChatColor.BLUE + getPlayer().getDisplayName() + ChatColor.GRAY + " / " + ChatColor.BLUE + getPlayer().getName() + ChatColor.GRAY + " - " + ChatColor.GREEN + "Online");
		} else {
			playerinfometa.setDisplayName(ChatColor.BLUE + getPlayer().getDisplayName() + ChatColor.GRAY + " / " + ChatColor.BLUE + getPlayer().getName() + ChatColor.GRAY + " - " + ChatColor.GREEN + "Online");
		}
		List<String> playerlore = new ArrayList<String>();
		playerlore.add(ChatColor.GRAY + "Rank: " + ChatColor.YELLOW + getRank());
		playerlore.add(ChatColor.GRAY + "Experience: " + ChatColor.YELLOW + experience);
		playerlore.add(ChatColor.GRAY + "Coins: " + ChatColor.YELLOW + coins);
		playerlore.add(" ");
		playerlore.add(ChatColor.GRAY + "UUID: " + ChatColor.YELLOW + uuid);
		playerlore.add(" ");
		if (getPlayer().isOnline()) {
			playerlore.add(ChatColor.GRAY + "World: " + ChatColor.YELLOW + getPlayer().getWorld().getName());
			playerlore.add(ChatColor.GRAY + "X: " + ChatColor.YELLOW + getPlayer().getLocation().getX());
			playerlore.add(ChatColor.GRAY + "Y: " + ChatColor.YELLOW + getPlayer().getLocation().getY());
			playerlore.add(ChatColor.GRAY + "Z: " + ChatColor.YELLOW + getPlayer().getLocation().getZ());
			playerlore.add(" ");
		}
		playerlore.add(ChatColor.GRAY + "Gamemode: " + ChatColor.YELLOW + getPlayer().getGameMode().toString());
		playerlore.add(ChatColor.GRAY + "EntityID: " + ChatColor.YELLOW + getPlayer().getEntityId());
		playerlore.add(" ");
		playerlore.add(ChatColor.GREEN + "► " + Lang.get("group.item.listpermissions", language));
		playerinfometa.setLore(playerlore);
		playerinfo.setItemMeta(playerinfometa);
		return playerinfo;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}
	
	public Player getPlayer() {
		if (player.isOnline()) {
			return (Player) player;
		}
		return null;
	}
	
	 /* ============================================================={ Communication }============================================================= */

	 public void sendTitle(Integer fadeIn, Integer stay, Integer fadeOut, String title) {
		    PlayerConnection Connection = ((CraftPlayer)getPlayer()).getHandle().playerConnection;
		    IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
		    PacketPlayOutTitle titletimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, titleComponent, fadeIn, stay, fadeOut);
		    PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
			    
		    Connection.sendPacket(titletimes);
		    Connection.sendPacket(titlePacket);
	 }
	 
	 public void sendSubTitle(Integer fadeIn, Integer stay, Integer fadeOut, String title) {
		    PlayerConnection Connection = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)getPlayer()).getHandle().playerConnection;
		    IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
		    PacketPlayOutTitle titletimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, titleComponent, fadeIn, stay, fadeOut);
		    PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleComponent);
			    
		    Connection.sendPacket(titletimes);
		    Connection.sendPacket(titlePacket);
	 }
	
	 public void sendActionBar(String message){
		 CraftPlayer p = (CraftPlayer) getPlayer();
		 IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		 PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		 p.getHandle().playerConnection.sendPacket(ppoc);
	 }
	 
	 public void setTab(String header, String footer) {
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			try {
			    Field a = packet.getClass().getDeclaredField("a");
			    a.setAccessible(true);
			    a.set(packet, ChatSerializer.a("{text:\"" + header + "\"}"));
			    Field b = packet.getClass().getDeclaredField("b");
			    b.setAccessible(true);
			    b.set(packet, ChatSerializer.a("{text:\"" + footer + "\"}"));
			} catch (Exception e1) {
				RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e1);
			}
			((CraftPlayer) getPlayer()).getHandle().playerConnection.sendPacket(packet);
	 }
	 
	 /* ============================================================={ Additional Tweaks }============================================================= */
	
	public void playSound(Sound sound) {
		getPlayer().playSound(getPlayer().getLocation(), sound, 1, 1);
	}

	 /* ============================================================={ Chat }============================================================= */
	
	public String getPrefix() {
		List<String> groupPrefixList = new ArrayList<String>();
		for (Group group : getGroups()) {
			if (FileSystem.groupsFile(group.getName()).exists()) {
				groupPrefixList.add(ChatColor.GRAY + group.getPrefix());
			} else {
				getGroups().remove(group);
				save();
			}
		}
		if (groupPrefixList.size() == 0) {
			groupPrefixList.add(ChatColor.GRAY + "Member");
		}
		String end = ChatColor.DARK_GRAY + "[";
		boolean first = true;
		if (groupPrefixList.size() >= 6) {
			end += ChatColor.DARK_RED + "-----";
		} else {
			for (String s : groupPrefixList) {
				if (!first) {
					end += ChatColor.DARK_GRAY + "/" + s;
				} else {
					end += s;
					first = false;
				}
			}
		}
		end += ChatColor.DARK_GRAY + "]";
		return end;
	}
	
	public void checkForChatImage() {
		final Player p = getPlayer();
		Runnable r = new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				if (!(new File(FileSystem.headsPath, p.getUniqueId().toString() + ".png").exists())) {
					String imageUrl = "https://crafatar.com/avatars/" + p.getUniqueId().toString() + "?size=8";
					URL url = null;
					try {
						url = new URL(imageUrl);
					} catch (MalformedURLException e2) {
						failed(e2);
						return;
					}
					InputStream is = null;
					try {
						is = url.openStream();
					} catch (IOException e2) {
						failed(e2);
						return;
					}
					OutputStream os = null;
					try {
						os = new FileOutputStream(new File(FileSystem.headsPath, p.getUniqueId().toString() + ".png"));
					} catch (FileNotFoundException e2) {
						failed(e2);
						return;
					}

					byte[] b = new byte[2048];
					boolean got = false;
					int length;
					try {
						while ((length = is.read(b)) != -1) {
							got = true;
							os.write(b, 0, length);
						}
					} catch (IOException e2) {
						failed(e2);
						return;
					}
					
					try {
						is.close();
						os.close();
						if (got) {
							success();
						} else {
							failed(null);
						}
					} catch (IOException e2) {
						failed(e2);
						return;
					}
				}
			}
			
			private void success() {
				RestrictoR.plugin.getLogger().info("Successfully downloaded Head-Image for: " + p.getName());
			}
			
			private void failed(Exception ex) {
				RestrictoR.plugin.getLogger().warning("Download failed!");
				try {
					new File(FileSystem.headsPath, p.getUniqueId() + ".png").delete();
				} catch (Exception e10) {}
				if (ex != null) {
					RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", ex);
				}
			}
		};
		
		r.run();
	}
	
	 /* ============================================================={ GUI }============================================================= */
	
	public void openUserInventory(PlayeR inventoryOwner) {
		if (myUserInv == null) {
			myUserInv = new AdvInventory("User: " + getPlayer().getDisplayName(), 54, true) {
				@Override
				public void unRegister() {
					super.unRegister();
					myUserInv = null;
				}
			};
		}
		AdvInventory user = myUserInv;
		PlayeR me = this;
		user.setItem(getInfoItem(), 10);
		user.setItem(new ItemStack(Material.NETHER_STAR), ChatColor.BLUE + Lang.get("player.item.addpermission", getLanguage()), 13, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				e.getWhoClicked().closeInventory();
				UserActions.EditUser.startEdit(PlayeR.getPlayeR(e.getWhoClicked()), me);
				e.getWhoClicked().sendMessage(Lang.get("player.note.enterpermission", getLanguage()));
			}
		});
		user.setItem(new ItemStack(Material.BLAZE_POWDER), ChatColor.BLUE + Lang.get("player.item.removepermission", getLanguage()), 14, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage("Permissions for: " + getPlayer().getName());
				for (String s : getPermissions()) {
					new FancyMessage(ChatColor.DARK_GRAY + " - ")
					.then(ChatColor.GRAY + s).tooltip(ChatColor.RED + "Click to delete!").command("/removepermissionuser " + s)
					.send(e.getWhoClicked());
				}
			}
		});
		user.setItem(new ItemStack(Material.EYE_OF_ENDER), ChatColor.GREEN + "Achievements " + Lang.comingsoon, 15, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				e.getWhoClicked().closeInventory();
				PlayeR.getPlayeR(e.getWhoClicked()).sendTitle(5, 60, 5, Lang.comingsoon);
			}
		});
		user.setItem(new ItemStack(Material.BOOK), ChatColor.GREEN + "Warns " + Lang.comingsoon, 16, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				e.getWhoClicked().closeInventory();
				PlayeR.getPlayeR(e.getWhoClicked()).sendTitle(5, 60, 5, Lang.comingsoon);
			}
		});
		
		for (int i = 27; i < user.getSize(); i++) {
			user.setItem(ItemUtils.placeholder(DyeColor.RED), i);
		}
		
		int i = 27;
		Collection<Group> grps = Group.getAllGroups();
		if (grps.size() < 28) {
			for (Group grp : grps) {
				final int myi = i;
				ClickRunnable cr = new ClickRunnable() {
					@Override
					public void run(InventoryClickEvent e) {
						if (getGroups().contains(grp)) {
							getGroups().remove(grp);
							user.setItem(new ItemStack(Material.SNOW_BALL), ChatColor.DARK_GREEN + grp.getName(), myi, this, new String[] { Lang.get("player.item.isnotingroup", getLanguage()), Lang.get("player.item.clicktoaddtogroup", getLanguage()) });
						} else {
							getGroups().add(grp);
							user.setItem(new ItemStack(Material.SLIME_BALL), ChatColor.DARK_GREEN + grp.getName(), myi, this, new String[] { Lang.get("player.item.isingroup", getLanguage()), Lang.get("player.item.clicktoremovefromgroup", getLanguage()) });
						}
					}
				};
				if (getGroups().contains(grp)) {
					user.setItem(new ItemStack(Material.SLIME_BALL), ChatColor.DARK_GREEN + grp.getName(), i, cr, new String[] { Lang.get("player.item.isingroup", getLanguage()), Lang.get("player.item.clicktoremovefromgroup", getLanguage()) });
				} else {
					user.setItem(new ItemStack(Material.SNOW_BALL), ChatColor.DARK_GREEN + grp.getName(), i, cr, new String[] { Lang.get("player.item.isnotingroup", getLanguage()), Lang.get("player.item.clicktoaddtogroup", getLanguage()) });
				}
				i++;
			}
		} else {
			inventoryOwner.getPlayer().sendMessage(Lang.get("group.error.morethan27found", getLanguage()));
			user.setItem(inventoryFailed(), 40);
		}
		user.openInventory(inventoryOwner);
	}
	
	private ItemStack inventoryFailed() {
		ItemStack world = new ItemStack(Material.BARRIER);
		ItemMeta worldmeta = world.getItemMeta();
		worldmeta.setDisplayName(ChatColor.RED + "RestrictoR was unable to intitialize Groups");
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.RED + "ERROR: " + ChatColor.GRAY + "Too many groups were found");
		worldmeta.setLore(lore1);
		world.setItemMeta(worldmeta);
		return world;
	}
}