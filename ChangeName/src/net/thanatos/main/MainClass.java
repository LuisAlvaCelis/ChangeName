package net.thanatos.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.thanatos.cmd.ChangeNameCommand;
import net.thanatos.listener.Listeners;

public class MainClass extends JavaPlugin{

	private static MainClass instance;
	
	public static MainClass getInstance() {
		if(instance==null) {
			instance=new MainClass();
		}
		return instance;
	}
	
	
	@Override
	public void onEnable() {
		instance=this;
		this.registerYML();
		this.registerCommands();
		this.registerEvents();
		this.getServer().getConsoleSender().sendMessage("§aChange name has been enabled.");
	}
	
	@Override
	public void onDisable() {
		instance=null;
		this.getServer().getConsoleSender().sendMessage("§cChange name has been disabled.");
	}
	
	
	private void registerCommands() {
		this.getCommand("changename").setExecutor(new ChangeNameCommand());
	}
	
	private void registerYML() {
		try {
			File file=new File(getDataFolder(),"config.yml");
			if(!file.exists()) {
				this.getConfig().options().copyDefaults(true);
				this.saveConfig();
				file=new File("plugins/ChangeNames/players.yml");
				if(!file.exists()) {
					file.createNewFile();
					this.saveResource("players.yml",true);
				}
			}else {
				file=new File("plugins/ChangeNames/players.yml");
				if(!file.exists()) {
					file.createNewFile();
					this.saveResource("players.yml",true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerEvents() {
		PluginManager pm=Bukkit.getPluginManager();
		pm.registerEvents(new Listeners(), this);
	}
	
}
