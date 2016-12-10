package de.zbs.restrictor.utils;

public class Tab {
	
//	static PlayerInfoData bananasquadGlobal(PacketPlayOutPlayerInfo ppopi) {
//		//Global-Spacer
//        GameProfile bananasquadempty = new GameProfile(UUID.randomUUID(), "c");
//        bananasquadempty.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{SKIN:{url:\"http://withercraft.de/skins/bananasquad.png\"}}")));
//        return new PlayerInfoData(bananasquadempty, 0, EnumGamemode.SURVIVAL, IChatBaseComponent.a("{\"text\":\"\",\"extra\":[{\"text\":\" \",\"color\":\"dark_green\",\"bold\":\"true\"}]}")));
//	}
//	
//	static PlayerInfoData bananasquadTeam(PacketPlayOutPlayerInfo ppopi) {
//		//Team-Spacer
//        GameProfile bananasquadempty = new GameProfile(UUID.randomUUID(), "f");
//        bananasquadempty.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{SKIN:{url:\"http://withercraft.de/skins/bananasquad.png\"}}")));
//        return new PlayerInfoData(ppopi, bananasquadempty, 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\" \",\"color\":\"dark_green\",\"bold\":\"true\"}]}"));
//	}
//	
//	static PlayerInfoData player(Player p, PacketPlayOutPlayerInfo ppopi) {
//		GameProfile player = new GameProfile(p.getUniqueId(), ChatColor.GRAY + "b");
//        player.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"http://withercraft.de/skins/bananasquad.png\"}}}")));
//        return new PlayerInfoData(ppopi, player, 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"" + p.getName() + "\",\"color\":\"gray\",\"bold\":\"true\"}]}"));
//	}
	
	public static void update() {
//		/*
//		 * Globalliste
//		 */
//    	PacketPlayOutPlayerInfo ppopiglobaltitle = new PacketPlayOutPlayerInfo();
//        Field aglobaltitle;
//		try {
//			aglobaltitle = ppopiglobaltitle.getClass().getDeclaredField("a");
//			aglobaltitle.setAccessible(true);
//			aglobaltitle.set(ppopiglobaltitle, EnumPlayerInfoAction.ADD_PLAYER);
//	        Field bglobaltitle = ppopiglobaltitle.getClass().getDeclaredField("b");
//	        bglobaltitle.setAccessible(true);
//	        ArrayList<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
//	        GameProfile kevosglobal = new GameProfile(UUID.fromString("7443ef63-59fc-4a7c-b82a-fb009753eef0"), "a");
//	        kevosglobal.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"http://withercraft.de/skins/kevos.png\"}}}")));
//	        dataList.add(new PlayerInfoData(ppopiglobaltitle, kevosglobal, 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"~ Global ~\",\"color\":\"dark_green\",\"bold\":\"true\"}]}")));
//	        bglobaltitle.set(ppopiglobaltitle, dataList);
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
//		}
//    	PacketPlayOutPlayerInfo ppopiglobalplayers = new PacketPlayOutPlayerInfo();
//        Field aglobalplayers;
//		try {
//			aglobalplayers = ppopiglobalplayers.getClass().getDeclaredField("a");
//			aglobalplayers.setAccessible(true);
//			aglobalplayers.set(ppopiglobalplayers, EnumPlayerInfoAction.ADD_PLAYER);
//	        Field bglobalplayers = ppopiglobalplayers.getClass().getDeclaredField("b");
//	        bglobalplayers.setAccessible(true);
//	        ArrayList<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
//	        if (Bukkit.getOnlinePlayers().length < 60) {
//	        	for (Player p : Bukkit.getOnlinePlayers()) {
//    				dataList.add(new PlayerInfoData(ppopiglobalplayers, new GameProfile(p.getUniqueId(), "b"), 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"" + p.getName() + "\",\"color\":\"gray\"}]}")));
//	        	}
//	        	for (int i = 0; i < 60-Bukkit.getOnlinePlayers().length; i++) {
//					dataList.add(bananasquadGlobal(ppopiglobalplayers));
//	        	}
//	        } else {
//	    		for (int i = 0; i < 60; i++) {
//					if (i >= Bukkit.getOnlinePlayers().length) {
//						dataList.add(bananasquadGlobal(ppopiglobalplayers));
//					} else {
//	    				dataList.add(new PlayerInfoData(ppopiglobalplayers, new GameProfile(Bukkit.getOnlinePlayers()[i].getUniqueId(), "b"), 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"" + Bukkit.getOnlinePlayers()[i].getName() + "\",\"color\":\"gray\"}]}")));
//					}
//	    		}
//	        }
//	        bglobalplayers.set(ppopiglobalplayers, dataList);
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
//		}
//		
//		/*
//		 * Teamliste
//		 */
//    	PacketPlayOutPlayerInfo ppopiteamtitle = new PacketPlayOutPlayerInfo();
//        Field ateamtitle;
//		try {
//			ateamtitle = ppopiteamtitle.getClass().getDeclaredField("a");
//			ateamtitle.setAccessible(true);
//			ateamtitle.set(ppopiteamtitle, EnumPlayerInfoAction.ADD_PLAYER);
//	        Field b = ppopiteamtitle.getClass().getDeclaredField("b");
//	        b.setAccessible(true);
//	        ArrayList<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
//	        GameProfile alistorteam = new GameProfile(UUID.fromString("e452ddc7-b1ba-4c61-9823-35c1d6d47afa"), "d");
//	        alistorteam.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"http://withercraft.de/skins/alistor.png\"}}}")));
//	        dataList.add(new PlayerInfoData(ppopiteamtitle, alistorteam, 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"~  Team  ~\",\"color\":\"dark_green\",\"bold\":\"true\"}]}")));
//	        b.set(ppopiteamtitle, dataList);
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
//		}
//    	PacketPlayOutPlayerInfo ppopiteamplayers = new PacketPlayOutPlayerInfo();
//        Field ateamplayers;
//		try {
//			ateamplayers = ppopiteamplayers.getClass().getDeclaredField("a");
//			ateamplayers.setAccessible(true);
//			ateamplayers.set(ppopiteamplayers, EnumPlayerInfoAction.ADD_PLAYER);
//	        Field b = ppopiteamplayers.getClass().getDeclaredField("b");
//	        b.setAccessible(true);
//	        ArrayList<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
//            FileConfiguration cfg = FileSystem.config_config();
//    		List<String> listteam = cfg.getStringList("teamlist");
//    		for (String s : listteam) {
//   				dataList.add(new PlayerInfoData(ppopiglobalplayers, new GameProfile(UUID.fromString(s), "e"), 0, EnumGamemode.SURVIVAL, ChatSerializer.a("{\"text\":\"\",\"extra\":[{\"text\":\"" + Bukkit.getOfflinePlayer(UUID.fromString(s)).getName() + "\",\"color\":\"gray\"}]}")));
//    		}
//    		for (int i = 0; i < 20-listteam.size(); i++) {
//				dataList.add(bananasquadTeam(ppopiglobalplayers));
//    		}
//    		b.set(ppopiteamplayers, dataList);
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			RestrictoR.plugin.getLogger().log(Level.SEVERE, "Exception thrown: ", e);
//		}
//		
//		for (Player player : Bukkit.getOnlinePlayers()) {
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppopiglobaltitle);
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppopiglobalplayers);
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppopiteamtitle);
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppopiteamplayers);
//		}
	}
}
