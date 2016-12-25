package com.Patane.Battlegrounds.util;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.Patane.Battlegrounds.Messenger;


/**
 * 
 * Credit: https://bukkit.org/threads/simple-configuration-class-easily-create-edit-and-save-yml-files.293154/
 * 
 * @author Dragonphase
 *
 */

public class Config extends YamlConfiguration{
 
    private JavaPlugin plugin;
    private String fileName;
 
    public Config(JavaPlugin plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
 
        createFile();
    }
 
    private void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()){
                if (plugin.getResource(fileName) != null){
                	Messenger.info("Creating " + fileName +"...");
                    plugin.saveResource(fileName, false);
                }else{
                    save(file);
                }
            }else{
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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