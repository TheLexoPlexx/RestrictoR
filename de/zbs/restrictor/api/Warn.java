package de.zbs.restrictor.api;

public class Warn {

	private String name;
	private String category;
	
	public Warn(String name, String category) {
		this.name = name;
		this.category = category;
	}
	
	public Warn(String serializedString) {
		this.name = serializedString.split("/")[1];
		this.category = serializedString.split("/")[0];
	}
	
	public String serialize() {
		return category + "/" + name;
	}
	
	public Warn dezerialze(String string) {
		return new Warn(string.split("/")[1], string.split("/")[0]);
	}
}