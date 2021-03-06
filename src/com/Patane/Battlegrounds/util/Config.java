package com.Patane.Battlegrounds.util;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.Patane.Battlegrounds.Messenger;


/**
 * 
 * Credit: https://bukkit.org/threads/simple-configuration-class-easily-create-edit-and-save-yml-files.293154/
 * 
 * @author Dragonphase
 *
 */

public class Config extends YamlConfiguration{
 
    private Plugin plugin;
    private String fileName;
 
    public Config(Plugin plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
 
        createFile();
    }
 
    private void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()){
                if (plugin.getResource(fileName) != null){
                	Messenger.info("Creating " + fileName + "...");
                    plugin.saveResource(fileName, false);
                }else{
                    save(file);
                }
            }else{
            	Messenger.info("Loading " + fileName + "...");
                load(file);
                save(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void save(){
        try {
            save(new File(plugin.getDataFolder(), fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}