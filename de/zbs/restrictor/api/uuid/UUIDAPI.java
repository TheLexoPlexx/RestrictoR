package de.zbs.restrictor.api.uuid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.zbs.restrictor.api.FileSystem;
import de.zbs.restrictor.main.RestrictoR;

public class UUIDAPI {

	private UUIDAPI() {}

	private static HashMap<String, String> NU = new HashMap<>();
	private static HashMap<String, String> UN = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			File f1 = FileSystem.uuidapi1(); //UN
			if (f1.exists()) {
			FileInputStream fis1 = new FileInputStream(f1);
			ObjectInputStream ois1 = new ObjectInputStream(fis1);
			UN = (HashMap<String, String>) ois1.readObject();
			ois1.close();
			fis1.close();
			}
		} catch (IOException ioe) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", ioe);
		} catch (ClassNotFoundException e) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
		}
		try {
			File f2 = FileSystem.uuidapi2(); //NU
			if (f2.exists()) {
			FileInputStream fis2 = new FileInputStream(f2);
			ObjectInputStream ois2 = new ObjectInputStream(fis2);
			NU = (HashMap<String, String>) ois2.readObject();
			ois2.close();
			fis2.close();
			}
		} catch (IOException ioe) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", ioe);
		} catch (ClassNotFoundException e) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
		}
	}
	
	public static void initialize() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			INTERNAL_initalizePlayer(p);
		}
		save();
	}
	
	public static void initializePlayer(Player p) {
		INTERNAL_initalizePlayer(p);
		save();
	}
	
	private static void INTERNAL_initalizePlayer(Player p) {
		UN.put(p.getUniqueId().toString().toLowerCase(), p.getName());
		NU.put(p.getName(), p.getUniqueId().toString().toLowerCase());
		NU.put(p.getCustomName(), p.getUniqueId().toString().toLowerCase());
		NU.put(p.getDisplayName(), p.getUniqueId().toString().toLowerCase());
		NU.put(p.getPlayerListName(), p.getUniqueId().toString().toLowerCase());
	}
	
	public static void save() {
		try {
			File f1 = FileSystem.uuidapi1(); // UN
			FileOutputStream fos1 = new FileOutputStream(f1);
			ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
			oos1.writeObject(UN);
			oos1.close();
			fos1.close();
		} catch (IOException ioe) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", ioe);
		}
		try {
			File f2 = FileSystem.uuidapi2(); // NU
			FileOutputStream fos2 = new FileOutputStream(f2);
			ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
			oos2.writeObject(NU);
			oos2.close();
			fos2.close();
		} catch (IOException ioe) {
			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", ioe);
		}
	}
	
	public static UUID getUUID(String playername) throws Exception {
		if (playername == null || playername.isEmpty()) {
			throw new NullPointerException("Playername is null or empty");
		}
		String uu = NU.get(playername);
		if (uu == null || uu.isEmpty()) {
			throw new Exception("Player not found.");
		}
		return UUID.fromString(uu);
	}
	
	public static String getName(UUID id) throws Exception {
		if (id == null) {
			throw new NullPointerException("UUID is null or empty");
		}
		String s = UN.get(id.toString().toLowerCase());
		if (s == null || s.isEmpty()) {
			throw new Exception("Player not found.");
		}
		return s;
	}
	
	public static Player getPlayer(UUID id) {
		return Bukkit.getPlayer(id);
	}
	
	public static Player getPlayer(String s) throws Exception {
		return getPlayer(getUUID(s));
	}
}
