package de.zbs.restrictor.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.zbs.restrictor.api.AdvInventory;
import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.api.Group;
import de.zbs.restrictor.api.PlayeR;
import de.zbs.restrictor.api.uuid.UUIDAPI;
import de.zbs.restrictor.api.uuid.UUIDAPIListener;
import de.zbs.restrictor.lang.Lang;
import de.zbs.restrictor.lang.Language;
import de.zbs.restrictor.listeners.AsyncPlayerChatListener;
import de.zbs.restrictor.listeners.ConnectionHandler;
import de.zbs.restrictor.listeners.PlayerChangedWorldListener;

public class RestrictoR extends JavaPlugin {
	
	public static RestrictoR plugin;
	
	public static boolean effectlibenabled = true;

	public static String getDateTime(boolean first) {
        DateFormat dateFormat;
        if (first) {
			dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		} else {
			dateFormat = new SimpleDateFormat("HH:mm:ss");
		}
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	public static void log(Exception e) {
		File f = new File("plugins/RestrictoR", "templog.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		String dt = getDateTime(true) + "/" + getDateTime(false);
		cfg.set(dt + ".getLocalizedMessage", e.getLocalizedMessage());
		cfg.set(dt + ".getMessage", e.getMessage());
		cfg.set(dt + ".getCause", e.getCause());
		cfg.set(dt + ".getStackTrace", e.getStackTrace());
		cfg.set(dt + ".getSuppressed", e.getSuppressed());
		try {
			cfg.save(f);
		} catch (IOException e1) {}
	}
		
	@Override
	public void onEnable() {
		plugin = this;
		//Checking for Required Components
		if (this.getServer().getPluginManager().getPlugin("EffectLib") == null) {
			this.getLogger().warning("Effectlib is not installed! Some features may not work!");
			effectlibenabled = false;
		}
		FileSystem.init();
		UUIDAPI.load();
		Lang.load();
		Group.loadAllGroups();
		
		//Default Permissions
		File m = FileSystem.defaultpermissions();
		FileConfiguration mcfg = YamlConfiguration.loadConfiguration(m);
		ArrayList<String> perml = new ArrayList<String>();
		perml.add("default.permissions.for.everybody.go.here");
		mcfg.addDefault("permissions", perml);
		mcfg.options().copyDefaults(true);
		try {
			mcfg.save(m);
		} catch (IOException e1) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e1);
		}
		
		if (!FileSystem.levelupimg().exists()) {
			RestrictoR.plugin.getLogger().info("Downloading default LevelUp-Image...");
			try {
				FileOutputStream fos = new FileOutputStream(FileSystem.levelupimg());
				fos.getChannel().transferFrom(Channels.newChannel(new URL("http://i.imgur.com/0yGT3Ul.png").openStream()), 0, 2000);
				fos.close();
				RestrictoR.plugin.getLogger().info("Successfully downloaded LevelUp-Image!");
			} catch (IOException e) {}
		}
		
		if (!FileSystem.steve().exists()) {
			RestrictoR.plugin.getLogger().info("Downloading default Steve-Fallback-Image...");
			try {
				FileOutputStream fos = new FileOutputStream(FileSystem.steve());
				fos.getChannel().transferFrom(Channels.newChannel(new URL("https://crafatar.com/avatars/steve?helm&size=8").openStream()), 0, 2000);
				fos.close();
				RestrictoR.plugin.getLogger().info("Successfully downloaded Steve-Fallback-Image");
			} catch (IOException e) {
				RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
			}
		}
		
		getCommand("restrictor").setExecutor(new RestrictoRCommand());
		
		PluginManager pm = Bukkit.getPluginManager();
		List<Listener> listeners = new ArrayList<>();
		listeners.add(new UUIDAPIListener());
		
		listeners.add(new AsyncPlayerChatListener());
		listeners.add(new PlayerChangedWorldListener());
		listeners.add(new ConnectionHandler());
		listeners.add(new AdvInventory("", 0, false));

		@SuppressWarnings("unused")
		class metandinst {
			public final Object instance;
			public final Method method;
			public metandinst(Object obj, Method met) {
				instance = obj;
				method = met;
			}
		}

		List<metandinst> joinMethods = new ArrayList<>();
		for (Listener l : listeners) {
			pm.registerEvents(l, this);
			for (Method met : l.getClass().getDeclaredMethods()) {
				if (met.isAnnotationPresent(EventHandler.class)) {
					for (Class<?> cl : met.getParameterTypes()) {
						if ((cl.getPackage() + cl.getName()).equalsIgnoreCase(PlayerJoinEvent.class.getPackage() + PlayerJoinEvent.class.getName())) {
							joinMethods.add(new metandinst(l, met));
						}
					}
				}
			}
		}
//		for (Player z : Bukkit.getOnlinePlayers()) {
//			PlayerQuitEvent pqe = new PlayerQuitEvent(z, null);
//			for (metandinst mettt : quitMethods) {
//				try {
//					mettt.method.invoke(mettt.instance, pqe);
//				} catch (Exception e) {e.printStackTrace();}
//			}
//			
//			PlayerJoinEvent pje = new PlayerJoinEvent(z, null);
//			for (metandinst mettt : joinMethods) {
//				try {
//					mettt.method.invoke(mettt.instance, pje);
//				} catch (Exception e) {e.printStackTrace();}
//			}
//		}
		
		this.getLogger().info("Successfully enabled");
		this.getLogger().info(Lang.get("command.nopermission", Language.getLanguageFromString("de")));
	}
	
	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayeR.getPlayeR(p).save();
		}
		UUIDAPI.save();                             // UUIDAPI
		for (Handler h : this.getLogger().getHandlers()) {
			if (h instanceof FileHandler) {
				((FileHandler) h).flush();
				((FileHandler) h).close();
			}
		}
		this.getLogger().info("Successfully disabled.");
	}
}
