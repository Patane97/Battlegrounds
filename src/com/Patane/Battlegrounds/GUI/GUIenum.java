package com.Patane.Battlegrounds.GUI;

import org.bukkit.ChatColor;

public enum GUIenum {
	BACK("&a&lBack"), 
	BAR(" "), 
	SAVE_EXIT("&a&lSave & Exit");
	
	private String value;
	
	private GUIenum(String value){
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
