package de.zbs.restrictor.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.zbs.restrictor.lang.Language;
import de.zbs.restrictor.main.RestrictoR;

public class FileSystem {
	
	public static String seperator = "/";
	public static String basePath = "plugins";
	public static String restrictorPath = basePath + seperator + RestrictoR.plugin.getName();
	public static String headsPath = restrictorPath + seperator + "Heads";
	public static String languagesPath = restrictorPath + seperator + "Language";
	public static String playerdbPath = restrictorPath + seperator + "PlayerDB";
	public static String groupsPath = restrictorPath + seperator + "Groups";
	
	private static Map<String, File> fcache = new HashMap<>();
	private static Map<String, YamlConfiguration> cfgcache = new HashMap<>();
	
	/*
	 * Base Methods
	 */
	public static void clearCache() {
		fcache.clear();
		cfgcache.clear();
	}
	
	public static File file(String path, String file) {
		if (fcache.containsKey(file + path)) {
			return fcache.get(file + path);
		}
		File f = new File(path, file);
		fcache.put(path, f);
		return f;
	}
	
	public static File folder(String path) {
		if (fcache.containsKey(path)) {
			return fcache.get(path);
		}
		File f = new File(path);
		fcache.put(path, f);
		return f;
	}

	public static FileConfiguration fileConfig(File file) {
		if (cfgcache.containsKey(file.getPath())) {
			return cfgcache.get(file.getPath());
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		cfgcache.put(file.getPath(), config);
		return config;
	}
	
	public static void init() {
		mainFolder().mkdirs();
		headsFolder().mkdirs();
		groupsFolder().mkdirs();
		playerdbFolder().mkdirs();
		languageFolder().mkdirs();
	}
	
	/*
	 * Folders
	 * ----
	 * Files
	 */
	
	public static File mainFolder() {
		return folder(restrictorPath);
	}
	
	public static File headsFolder() {
		return folder(headsPath);
	}
	
	public static File playerdbFolder() {
		return folder(playerdbPath);
	}
	
	public static File playerFile(String uuid) {
		return file(playerdbPath, uuid + ".yml");
	}
	
	public static File playerFile(Player player) {
		return file(playerdbPath, player.getUniqueId().toString() + ".yml");
	}
	
	public static File playerFile(PlayeR player) {
		return file(playerdbPath, player.getUUID() + ".yml");
	}
	
	public static File languageFolder() {
		return folder(languagesPath);
	}
	
	public static File groupsFolder() {
		return folder(groupsPath);
	}
	
	public static File groupsFile(String group) {
		return file(groupsPath, group + ".yml");
	}
	
	public static File config() {
		return file(restrictorPath, "config.yml");
	}
	
	public static File defaultpermissions() {
		return file(restrictorPath, "default-permissions.yml");
	}

	public static File levelupimg() {
		return file(restrictorPath, "levelup.png");
	}
	
	public static File steve() {
		return file(restrictorPath, "steve.png");
	}
	
	public static File uuidapi1() {
		return file(restrictorPath, "UUID-API1.db");
	}
	
	public static File uuidapi2() {
		return file(restrictorPath, "UUID-API2.db");
	}

	public static File languageFile(Language language) {
		return file(languagesPath, language.getLocale().getLanguage() + ".yml");
	}
}
