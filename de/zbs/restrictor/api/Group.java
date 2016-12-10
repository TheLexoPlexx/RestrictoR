package de.zbs.restrictor.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.zbs.restrictor.lang.Lang;
import de.zbs.restrictor.main.RestrictoR;
import de.zbs.restrictor.utils.ItemUtils;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;

public class Group { 
	
	private static HashMap<String, Group> grps = new HashMap<>();
	
	private String name;
	private String prefix;
	private String description;
	private HashMap<String, List<String>> worldpermissions; //String = World.getName()
	private List<Group> inheritances;
	private GroupType grouptype;
	
	@Deprecated
	private Group() {}
	
	private static Group load(String name) {
		if (FileSystem.groupsFile(name).exists()) {
			Group group = new Group();
			
			File f = FileSystem.groupsFile(name);
			FileConfiguration cfg = FileSystem.fileConfig(f);
			group.name = name;
			group.prefix = cfg.getString("info.prefix");
			group.description = cfg.getString("info.description");
			group.worldpermissions = new HashMap<String, List<String>>();
		    
		    for (World w : Bukkit.getWorlds()) {
		    	group.worldpermissions.put(w.getName(), cfg.getStringList("worlds." + w.getName()));
		    }
		    
		    if (cfg.getString("grouptype").equalsIgnoreCase(GroupType.SERVER_MANAGEMENT.toString())) {
		    	group.grouptype = GroupType.SERVER_MANAGEMENT;
		    } else if (cfg.getString("grouptype").equalsIgnoreCase(GroupType.SERVER_MODERATION.toString())) {
		    	group.grouptype = GroupType.SERVER_MODERATION;
		    } else if (cfg.getString("grouptype").equalsIgnoreCase(GroupType.DONATTION.toString())) {
		    	group.grouptype = GroupType.DONATTION;
		    } else {
		    	group.grouptype = GroupType.OTHER;
		    }
			grps.put(name.toLowerCase(), group);
			return group;
		} else {
			return null;
		}
	}
	
	public static Group createNewGroup(String name, String prefix, GroupType grouptype) {
		Group group = new Group();
		group.name = name;
		group.prefix = name.substring(0, 2);
		group.description = "Default description :(";
		group.worldpermissions = new HashMap<String, List<String>>();
	    group.inheritances = new ArrayList<Group>();
	    group.grouptype = GroupType.OTHER;
	    group.setPrefix(prefix);
	    group.setGroupType(grouptype);
	    group.save();
		grps.put(name.toLowerCase(), group);
		return group;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<Group> getInheritances() {
		return inheritances;
	}

	public void setInheritances(List<Group> inheritances) {
		this.inheritances = inheritances;
	}

	public void addInhertitance(Group inheritance) {
		this.inheritances.add(inheritance);
	}

	public void removeInheritance(Group inheritance) {
		this.inheritances.remove(inheritance);
	}	

	public HashMap<String, List<String>> getWorldpermissions() {
		return worldpermissions;
	}	

	public List<String> getWorldpermissions(World w) {
		return worldpermissions.get(w.getName());
	}	

	public boolean hasWorldPermission(World w, String perm) {
		return worldpermissions.get(w.getName()).contains(perm);
	}

	public void addPermission(World w, String perm) {
		if (w == null) {
			for (List<String> list : worldpermissions.values()) {
				list.add(perm);
			}
		} else {
			if (!worldpermissions.containsKey(w.getName())) {
				worldpermissions.put(w.getName(), new ArrayList<String>());
			}
			worldpermissions.get(w.getName()).add(perm);
		}
	}

	public void removePermission(World w, String perm) {
		if (w == null) {
			for (List<String> list : worldpermissions.values()) {
				list.remove(perm);
			}
		} else {
			worldpermissions.get(w.getName()).remove(perm);
		}
	}

	public void setWorldpermissions(HashMap<String, List<String>> worldpermissions) {
		this.worldpermissions = worldpermissions;
	}

	public void setWorldpermissions(World world, List<String> worldpermissions) {
		this.worldpermissions.put(world.getName(), worldpermissions);
	}

	public GroupType getGrouptype() {
		return grouptype;
	}

	public void setGroupType(GroupType grouptype) {
		this.grouptype = grouptype;
	}

	public static Group getGroup(String name) {
		if (!grps.containsKey(name.toLowerCase())) {
			return null;
		}
		return grps.get(name.toLowerCase());
	}
	
	protected File getGroupFile() {
		return FileSystem.file(FileSystem.groupsPath, getName() + ".yml");
	}
	
	public static Collection<Group> getAllGroups() {
		return grps.values();
	}

	public static void loadAllGroups() {
		File f = FileSystem.groupsFolder();
		if (FileSystem.groupsFolder().listFiles().length > 27) {
			RestrictoR.plugin.getLogger().warning("More than 27 groups found.");
		}
		if (Bukkit.getWorlds().size() > 27) {                                           
			RestrictoR.plugin.getLogger().warning("More than 27 Worlds found.");
		}
		if (f.exists() && f.listFiles().length > 0 ) {
			File[] lgf = f.listFiles();
			if (lgf.length < 28) {
				for (File fsm : lgf) {
					load(FileSystem.fileConfig(fsm).getString("info.name"));
				}
				for (Group g : getAllGroups()) {
					for (String s : YamlConfiguration.loadConfiguration(g.getGroupFile()).getStringList("inheritances")) {
						g.inheritances.add(getGroup(s));
					}
				}
			} else {
				Bukkit.getLogger().warning("More than 27 Groups found!");
			}
		} else {
			Bukkit.getLogger().info("No Groups found! Creating sample-groups...");
			Group exampleA = createNewGroup("Example Administrative group", "EAP", GroupType.SERVER_MANAGEMENT);
			exampleA.addPermission(null, "example.global.permission");
			exampleA.addPermission(Bukkit.getWorld("world"), "example.world.permission");
			exampleA.save();
			Group exampleB = createNewGroup("Example User group", "User", GroupType.OTHER);
			exampleB.addPermission(null, "example.global.permission");
			exampleB.save();
		}
	}

	public void save() {
		File f = FileSystem.groupsFile(name);
		FileConfiguration cfg = FileSystem.fileConfig(f);
		cfg.set("info.name", name);
		cfg.set("info.prefix", this.prefix);
		cfg.set("info.description", this.description);
		for (World w : Bukkit.getWorlds()) {
			cfg.set("worlds." + w.getName(), this.worldpermissions.get(w.getName()));
		}
    	List<String> temp = new ArrayList<String>();
	    for (Group g : this.inheritances) {
	    	temp.add(g.getName());
	    }
    	cfg.set("inheritances", temp);
    	cfg.set("grouptype", grouptype.toString());
    	try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openGroupInventory(PlayeR inventoryOwner) {
		AdvInventory adv = new AdvInventory(getName(), 54, true);
		adv.setItem(new ItemStack(Material.EMERALD), ChatColor.DARK_GREEN + getName(), 10, null);

		for (int i = 27; i < adv.getSize(); i++) {
			adv.setItem(ItemUtils.placeholder(DyeColor.RED), i);
		}
		
		adv.setItem(new ItemStack(Material.MAP), "World-Management", 14, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				openWorldList(inventoryOwner);
			}
		});
		adv.setItem(new ItemStack(Material.SHEARS), "Inheritance-Management", 15, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				openInheritanceManagement(inventoryOwner);
			}
		});
		adv.setItem(new ItemStack(Material.FIREBALL), "Delete Group", 16, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				for (File f : new File(FileSystem.playerdbPath).listFiles()) {
					PlayeR.getPlayeR(f).getGroups().remove(this);
				}
				String groupName = getName();
				getGroupFile().delete();
				inventoryOwner.sendSubTitle(5, 60, 5, ChatColor.DARK_RED + "Successfully deleted: " + ChatColor.RED + groupName);
				RestrictoR.plugin.getLogger().log(Level.INFO, inventoryOwner.getPlayer().getName() + " deleted " + groupName);
			}
		});
		
		int i = 27;
		Collection<Group> grps = Group.getAllGroups();
		if (grps.size() < 28) {
			for (Group grp : grps) {
				if (grp.getName().equals(getName())) {
					adv.setItem(new ItemStack(Material.REDSTONE), grp.getName(), i, null);
				} else {
					adv.setItem(new ItemStack(Material.SUGAR), grp.getName(), i, new ClickRunnable() {
						@Override
						public void run(InventoryClickEvent e) {
							Group g = getGroup(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
							g.openGroupInventory(PlayeR.getPlayeR(e.getWhoClicked()));
						}
					}, new String[] { Lang.get("group.item.clicktoeditgroup", inventoryOwner.getLanguage()) });
				}
				i++;
			}
		} else {
			inventoryOwner.getPlayer().sendMessage(Lang.get("group.error.morethan27found", inventoryOwner.getLanguage()));
			adv.setItem(ItemUtils.inventoryFailed("Worlds"), 40);
		}
		adv.openInventory(inventoryOwner);
	}

	public void openWorldList(PlayeR inventoryOwner) {
		AdvInventory adv = new AdvInventory("G > Worlds", 54, true);
		
		for (int i = 27; i < adv.getSize(); i++) {
			adv.setItem(ItemUtils.placeholder(DyeColor.RED), i);
		}
		
		int i = 27;
		List<World> wrlds = Bukkit.getWorlds();
		if (wrlds.size() < 28) {
			for (World w : wrlds) {
				adv.setItem(new ItemStack(Material.SUGAR), w.getName(), i, new ClickRunnable() {
					@Override
					public void run(InventoryClickEvent e) {
						openWorldManagement(Bukkit.getWorld(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), inventoryOwner);
					}
				}, new String[] { Lang.get("group.item.clicktoeditgroup", inventoryOwner.getLanguage()) });
				i++;
			}
		} else {
			inventoryOwner.getPlayer().sendMessage(Lang.get("world.error.morethan27found", inventoryOwner.getLanguage()));
			adv.setItem(ItemUtils.inventoryFailed("Worlds"), 40);
		}
		adv.openInventory(inventoryOwner);
	}
	
	public void openWorldManagement(World world, PlayeR inventoryOwner) {
		AdvInventory adv = new AdvInventory("G > W: " + world.getName(), 54, true);
		Group self = this;
		
		for (int i = 27; i < adv.getSize(); i++) {
			adv.setItem(ItemUtils.placeholder(DyeColor.RED), i);
		}
		
		adv.setItem(new ItemStack(Material.NETHER_STAR), "Add Permission", 15, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				UserActions.EditGroup.startEdit(inventoryOwner, self, world);
			}
		});
		adv.setItem(new ItemStack(Material.FIREBALL), "Delete Group", 16, new ClickRunnable() {
			@Override
			public void run(InventoryClickEvent e) {
				for (String s : getWorldpermissions(world)) {
					FancyMessage msg = new FancyMessage(
							ChatColor.DARK_GRAY + " - ").then(
									ChatColor.GRAY + s).command("/removepermissiongroup" + getName() + "::" + s + world.getName());
					msg.send(inventoryOwner.getPlayer());
				}
			}
		});
		
		adv.setItem(new ItemStack(Material.EMERALD), world.getName(), 54, null);
		
		int i = 27;
		List<World> wrlds = Bukkit.getWorlds();
		if (wrlds.size() < 28) {
			for (World w : wrlds) {
				if (w.getUID() == world.getUID()) {
					adv.setItem(new ItemStack(Material.SUGAR), w.getName(), i, new ClickRunnable() {
						@Override
						public void run(InventoryClickEvent e) {
							openWorldManagement(Bukkit.getWorld(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), inventoryOwner);
						}
					}, new String[] { Lang.get("group.item.clicktoeditgroupworld", inventoryOwner.getLanguage()) });
				} else {
					adv.setItem(new ItemStack(Material.REDSTONE), w.getName(), i, new ClickRunnable() {
						@Override
						public void run(InventoryClickEvent e) {
							openWorldManagement(Bukkit.getWorld(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), inventoryOwner);
						}
					}, new String[] { Lang.get("group.item.clicktoeditgroupworld", inventoryOwner.getLanguage()) });
				}
				i++;
			}
		} else {
			inventoryOwner.getPlayer().sendMessage(Lang.get("world.error.morethan27found", inventoryOwner.getLanguage()));
			adv.setItem(ItemUtils.inventoryFailed("Worlds"), 40);
		}
		adv.openInventory(inventoryOwner);
	}
	
	public void openInheritanceManagement(PlayeR inventoryOwner) {
	}
}