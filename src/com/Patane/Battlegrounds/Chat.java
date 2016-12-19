package com.Patane.Battlegrounds;

import org.bukkit.ChatColor;

public enum Chat {
	PLUGIN_PREFIX("&2[&aBattlegrounds&2]&r "),
	STRIPPED_PLUGIN_PREFIX("[Battlegrounds] ");
	
	private String value;
	
	private Chat (String value){
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
}
