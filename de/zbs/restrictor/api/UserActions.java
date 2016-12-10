package de.zbs.restrictor.api;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.World;

public class UserActions {
	
	public static class EditUser {

		private static HashMap<String, String> editingUser = new HashMap<String, String>(); //String uuid EDITS String uuid
		
		public static void startEdit(PlayeR editor, PlayeR player) {
			editingUser.put(editor.getUUID().toString(), player.getUUID().toString());
		}
		
		public static void stopEditing(PlayeR editor) {
			editingUser.remove(editor.getUUID().toString());
		}
		
		public static boolean isEditing(PlayeR editor) {
			return editingUser.containsKey(editor.getUUID().toString());
		}
		
		public static PlayeR get(PlayeR editor) {
			return PlayeR.getPlayeR(UUID.fromString(editingUser.get(editor.getUUID().toString())));
		}
	}

	
	public static class EditGroup {

		@SuppressWarnings("unused")
		private static class currentEdit {
			public currentEdit(Group g, World w, PlayeR p) {
				group = g;
				world = w;
				player = p;
			}
			
			public final Group group;
			public final World world;
			public final PlayeR player;
		}
		
		private static HashMap<String, currentEdit> editingGroup = new HashMap<String, currentEdit>(); //String uuid EDITS String groupName::World.getName
		
		public static void startEdit(PlayeR editor, Group group, World w) {
			editingGroup.put(editor.getUUID().toString(), new currentEdit(group, w, editor));
		}
		
		public static void stopEditing(PlayeR editor) {
			editingGroup.remove(editor.getUUID().toString());
		}
		
		public static boolean isEditing(PlayeR editor) {
			return editingGroup.containsKey(editor.getUUID().toString());
		}
		
		public static Group getGroup(PlayeR editor) {
			return editingGroup.get(editor.getUUID().toString()).group;
		}
		
		public static World getWorld(PlayeR editor) {
			return editingGroup.get(editor.getUUID().toString()).world;
		}
	}
}