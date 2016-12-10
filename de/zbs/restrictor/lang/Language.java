package de.zbs.restrictor.lang;

import java.io.File;
import java.util.Locale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.zbs.restrictor.api.FileSystem;

public class Language {
	
/*
Netherlands		eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzY2YyMTBlZGVhMzk2ZjJmNWRmYmNlZDY5ODQ4NDM0ZjkzNDA0ZWVmZWFiZjU0YjIzYzA3M2IwOTBhZGYifX19
Norway			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTA1OTZlMTY1ZWMzZjM4OWI1OWNmZGRhOTNkZDZlMzYzZTk3ZDljNjQ1NmU3YzJlMTIzOTczZmE2YzVmZGEifX19
Sweden			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkxMDkwNGJmZjljODZmNmVkNDc2ODhlOTQyOWMyNmU4ZDljNWQ1NzQzYmQzZWJiOGU2ZjUwNDBiZTE5Mjk5OCJ9fX0=
Egypt			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI2ZTc0MmIzMmYwZjhkYjU5YzA3YjFiY2RkZTZmOGE5M2Y4NWM5MjllNTk4YzdlOTI3M2I5MjExZjJjZTc4In19fQ==
USA				eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNWQ1NjYyMDJhYzBlNzZjZDg5Nzc1OWRmNWQwMWMxMWY5OTFiZDQ2YzVjOWEwNDM1N2VhODllZTc1In19fQ==
Belgium			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM3OGFhZTQyZWY5ZWU5ZmFhNjdiNjRiYjk3NGNlYTI3NWNlNzAyNjU1ZDM1Zjg0MWI2MDE3NDE2ZWUxYzM5MyJ9fX0=
Italy			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU2YzVjYzE3MzE5YTZjOWVjODQ3MjUyZTRkMjc0NTUyZDk3ZGE5NWUxMDg1MDcyZGJhNDlkMTE3Y2YzIn19fQ==
Germany			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==
France			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmEyNTI4N2QxMTQwZmIxNzQxZDRiNmY3ZTY1NjcyZjllNjRmZmZlODBlYTczNzFjN2YzZWM1YTZmMDQwMzkifX19
Russia			eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIxNmJjMzk3ZTNkZTgyZGE2NjMyYzYzZDMxNDQ3NzdjNDBkNWM4YjdmNzhlY2U1YjE0Y2MxYjZkOTYzIn19fQ==
*/
	
	Locale locale;
	String headValue;
	File file;
	
	public Language(Locale locale) {
		this.locale = locale;
		if (getFile().exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(getFile());
			if (cfg.getString("info.headvalue") != null) {
				this.headValue = cfg.getString("info.headvalue");
			} else {
				this.headValue = null;
			}
		}
		this.file = new File(FileSystem.languageFolder(), locale.getLanguage() + ".yml");
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public String getHeadValue() {
		return headValue;
	}
	
	public void setHeadValue(String headValue) {
		this.headValue = headValue;
	}

	public File getFile() {
		return new File(FileSystem.languageFolder(), locale.getLanguage() + ".yml");
	}
	
	public static Language getLanguageFromString(String string) { //string = language.getLocale().getLanguage();
		for (Locale l : Locale.getAvailableLocales()) {
			if (l.getLanguage().equalsIgnoreCase(string)) {
				return new Language(l);
			}
		}
		return null;
	}
}