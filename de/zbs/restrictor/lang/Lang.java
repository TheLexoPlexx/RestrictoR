
package de.zbs.restrictor.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.zbs.restrictor.api.AdvInventory;
import de.zbs.restrictor.api.ClickRunnable;
import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.api.PlayeR;
import de.zbs.restrictor.main.RestrictoR;
import de.zbs.restrictor.utils.ItemUtils;

public class Lang {
	
	private static List<Language> allLanguages = new ArrayList<Language>();
	
	public static String line = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------>---------------------------------------<------";
	public static String comingsoon = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Coming Soon™" + ChatColor.DARK_GRAY + "]";

	public static String get(String key, Language language) {
		return FileSystem.fileConfig(FileSystem.languageFile(language)).getString(key);
	}
	
	public static void openInventory(PlayeR inventoryOwner) {
		for (File f : FileSystem.languageFolder().listFiles()) {
			Language l = Language.getLanguageFromString(f.getName().replace("yml", ""));
			allLanguages.add(l);
		}
		if (allLanguages.size() > 45) {
			RestrictoR.plugin.getLogger().warning("More than 45 Languages found!");
		} else {
			int size;
			if (allLanguages.size() <= 45) {
				size = 54;
			} else if (allLanguages.size() <= 36) {
				size = 45;
			} else if (allLanguages.size() <= 27) {
				size = 36;
			} else if (allLanguages.size() <= 18) {
				size = 27;
			} else if (allLanguages.size() <= 9) {
				size = 18;
			} else {
				RestrictoR.plugin.getLogger().warning("More than 45 Languages found!");
				return;
			}
			
			AdvInventory adv = new AdvInventory("Languages...", size, true);
			adv.setItem(ItemUtils.getCustomPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FhYjI3Mjg0MGQ3OTBjMmVkMmJlNWM4NjAyODlmOTVkODhlMzE2YjY1YzQ1NmZmNmEzNTE4MGQyZTViZmY2In19fQ=="), "Need help?", 8, null, new String[] {
					"Click on a flag to switch to the desired Language.",
					"The changes will immediatly take effect,",
					"and you will recognize the change upon your next messages.",
					" ",
					"Your Language is not on the list?",
					"Report it to our forums and support us to translate everything",
					"into YOUR Language."
					});
			
			for (int i = 9; i < size; i++) {
				Language loop = allLanguages.get(i-9);
				adv.setItem(ItemUtils.getCustomPlayerHead(loop.
								getHeadValue() //Fehler: NullPointerException
								), loop.getLocale().getDisplayLanguage(), i, new ClickRunnable() {
					@Override
					public void run(InventoryClickEvent e) {
						PlayeR.getPlayeR(e.getWhoClicked()).setLanguage(Language.getLanguageFromString(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
					}
				});
			}
			
		}
	}
	
	public static void load() {
		Language german = new Language(Locale.GERMAN);
		LangCategory info = new LangCategory("info", RestrictoR.plugin);
		info.registerString(german, "headvalue", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==");
		LangCategory defaults = new LangCategory("defaults", RestrictoR.plugin);
		defaults.registerString(german, "group.error.notfound", "Gruppe nicht gefunden!");
		defaults.registerString(german, "group.error.name24Chars", "Der Name darf nicht länger als 24 Zeichen sein!");
		defaults.registerString(german, "group.error.justAtoZandHyphens", "Der Name darf nur A bis Z und Bindestriche enthalten!");
		defaults.registerString(german, "group.error.moreThan27found", "Mehr als 27 Gruppen gefunden!");
		defaults.registerString(german, "group.error.alredyexists", "Es wurde bereits eine Gruppe mit so einem Namen gefunden!");
		defaults.registerString(german, "group.success.addedpermission", "Neue Permission wurde erfolgreich hinzugefügt!");
		defaults.registerString(german, "group.success.removedpermission", "Permission wurde erfolgreich entfernt!");
		defaults.registerString(german, "group.success.creatednew", "Eine neue Gruppe wurde erfolgreich erstellt!");
		defaults.registerString(german, "group.item.addpermission", "Füge neue Permissions zu dieser Gruppe hinzu!");
		defaults.registerString(german, "group.item.removepermission", "Entferne Permissions von dieser Gruppe!");
		defaults.registerString(german, "group.item.listpermissions", "Aktuelle Permissions:");
		
		defaults.registerString(german, "world.error.notfound", "Welt nicht gefunden!");
		defaults.registerString(german, "world.error.morethan27found", "Mehr als 27 Welten gefunden!");
		
		defaults.registerString(german, "player.success.addedpermission", "Permission erfolgreich zum Spieler hinzugefügt!");
		defaults.registerString(german, "player.success.removedpermission", "Permission erfolgreich vom Spieler entfernt!");
		defaults.registerString(german, "player.error.notfound", "Spieler wurde nicht gefunden!");
		defaults.registerString(german, "player.item.addpermission", "Füge dem Spieler eine neue Permission hinzu!");
		defaults.registerString(german, "player.item.removepermission", "Entferne eine Permission vom Spielern!");
		defaults.registerString(german, "player.note.enterpermission", "Gib eine neue Permission ein...");
		defaults.registerString(german, "player.item.isingroup", "...ist in der Gruppe!");
		defaults.registerString(german, "player.item.isnotingroup", "...ist nicht in der Gruppe!");
		defaults.registerString(german, "player.item.clicktoaddtogroup", "Klicke um den Spieler zu dieser Gruppe hinzuzufügen!");
		defaults.registerString(german, "player.item.clicktoremovefromgroup", "Klicke um den Spieler aus dieser Gruppe zu entfernen!");
		defaults.registerString(german, "player.item.clicktoeditgroup", "Klicke um diese Gruppe zu bearbeiten!");
		defaults.registerString(german, "player.item.clicktoeditgroupworld", "Klicke um diese Welt zu bearbeiten!");
		
		defaults.registerString(german, "command.nopermission", "Keine Berechtigungen!");
		defaults.registerString(german, "command.toofewarguments", "Zu wenige Argumente!");
		defaults.registerString(german, "command.toomanyarguments", "Zu viele Argumente!");
		defaults.registerString(german, "command.invalidsubcommand", "Ungültiger Subcommand!");
		
		defaults.registerString(german, "chat.info.capsoff", "Bitte Caps ausschalten!");
		defaults.registerString(german, "chat.info.chatcleared", "Der Chat wurde erfolgreich geleert!");
		defaults.registerString(german, "chat.info.onlyfordev", "Dieser Befehl ist nur für Developer oder höher!");
		
		defaults.registerString(german, "misc.help.groups", "Bearbeite Gruppen und ihre Rechte");
		defaults.registerString(german, "misc.help.users", "Bearbeite Spieler und ihre Rechte");
		defaults.registerString(german, "misc.help.newgroup", "Erstelle eine neue Gruppe");
		defaults.registerString(german, "misc.help.clearchat", "Leert den Chat");
		defaults.registerString(german, "misc.help.hoverForInfo", "Fahre mit der Maus über einen Befehl um mehr Infos zu erfahren!");
		defaults.registerString(german, "misc.item.backToMenu", "Zurück zum Hauptmenü!");
	}
}