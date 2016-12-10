package de.zbs.restrictor.utils;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public class ItemUtils {
	
	@SuppressWarnings("deprecation")
	public static ItemStack placeholder(DyeColor c) {
		ItemStack placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, c.getData());
		ItemMeta placeholdermeta = placeholder.getItemMeta();
		placeholdermeta.setDisplayName(ChatColor.YELLOW + "");
		placeholder.setItemMeta(placeholdermeta);
		return placeholder;
	}
	
	public static ItemStack inventoryFailed(String name) {
		ItemStack world = new ItemStack(Material.BARRIER);
		ItemMeta worldmeta = world.getItemMeta();
		worldmeta.setDisplayName(ChatColor.RED + "RestrictoR was unable to intitialize " + name);
		world.setItemMeta(worldmeta);
		return world;
	}
	
	public static ItemStack getCustomPlayerHead(String b64stringtexture) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        propertyMap.put("textures", new Property("textures", b64stringtexture));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        try {
			getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        head.setItemMeta(headMeta);
        return head;
	}
	
    private static <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return field;
            }
        }

        // Search in parent classes
        if (target.getSuperclass() != null) {
			return getField(target.getSuperclass(), name, fieldType, index);
		}
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }
}