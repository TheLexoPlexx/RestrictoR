package de.zbs.restrictor.lang;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class LangCategory {

	private String name;
	private Plugin plugin;
	
	public LangCategory(String name, Plugin plugin) {
		this.name = name;
		this.plugin = plugin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void registerString(Language language, String key, String translation) {
		File f = language.getFile();
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.set(this.name + "." + key, translation);
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}