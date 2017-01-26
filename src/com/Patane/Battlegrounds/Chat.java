package com.Patane.Battlegrounds;

import org.bukkit.ChatColor;

public enum Chat {
	PLUGIN_PREFIX("&2[&aBattleGrounds&2]&r "),
	PLUGIN_PREFIX_SMALL("&2[&aBG&2]&r "),
	STRIPPED_PLUGIN_PREFIX("[Battlegrounds] "),
	STRIPPED_PLUGIN_PREFIX_SMALL("[BG] ");
	
	private String value;
	
	private Chat(String value){
        set(value);
    }

    void set(String value) {
        this.value = value;
    }
    
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', value);
    }
    public String format(String s) {
        return (s == null) ? "" : toString().replace("%", s);
    }
    public static String translate(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static boolean hasAlpha(String s){
    	return s.matches(".*[a-zA-Z]+.*");
    }
}
