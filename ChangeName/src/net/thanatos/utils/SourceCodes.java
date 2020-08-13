package net.thanatos.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.thanatos.main.MainClass;

public class SourceCodes {
	
	private boolean status;
	private static SourceCodes instance;
	private File file;
	private YamlConfiguration yml;
	
	public static SourceCodes getInstance() {
		if(instance==null) {
			instance=new SourceCodes();
		}
		return instance;
	}
	
	public SourceCodes() {
		this.status=MainClass.getInstance().getConfig().getBoolean("Config.status");
		this.file=new File(MainClass.getInstance().getDataFolder()+File.separator+"players.yml");
		this.yml=YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration getFile() {
		return this.yml;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void view(Player player) {
		player.sendMessage(translateColor("&7&m-------- &eChangeNames &7&m--------"));
		player.sendMessage(translateColor(""));
		player.sendMessage(translateColor("&eList of players with name change"));
		player.sendMessage(translateColor(""));
		List<String> list=yml.getStringList("Player.using");
		if(list.size()>=1) {
			for(int i=0;i<list.size();i++) {
				player.sendMessage(translateColor("&7 - &e"+yml.getString("Player.names."+list.get(i)+".realName")+" &7new name &a"+yml.getString("Player.names."+list.get(i)+".newName")));
			}			
		}else {
			player.sendMessage(translateColor("&7No one's using the name change"));
		}
		player.sendMessage(translateColor(""));
		player.sendMessage(translateColor("&7&m-------- &eChangeNames &7&m--------"));
	}
	
	public void toggleStatus(boolean status) {
		this.status=status;
		MainClass.getInstance().getConfig().set("Config.status", status);
		MainClass.getInstance().saveConfig();
		if(status==false) {
			List<String> listp=yml.getStringList("Player.using");
			for(int i=0;i<listp.size();i++) {
				SourceCodes.getInstance().removeCurrentName(Bukkit.getPlayer(UUID.fromString(listp.get(i))));
				Bukkit.getPlayer(UUID.fromString(listp.get(i))).sendMessage(SourceCodes.translateColor("&a[ChangeName] You name has been reset."));
			}
		}
	}
	
	public boolean verifyName(String name) {
		boolean verify=false;
		List<String> list=yml.getStringList("Player.list-names");
		if(list.contains(name)) {
			verify=true;
		}
		return verify;
	}
	
	public void removeCurrentName(Player player) {
		String uuid=String.valueOf(player.getUniqueId());
		this.setPlayerNameTag(player, yml.getString("Player.names."+uuid+".realName"));
		List<String> list=yml.getStringList("Player.list-names");
		list.remove(yml.getString("Player.names."+uuid+".newName"));
		this.yml.set("Player.list-names", list);
		this.yml.set("Player.names."+uuid,null);
		this.saveFile();
		this.removePlayerToListUse(uuid);
		player.setDisplayName(null);
		player.setPlayerListName(null);
		player.setCustomName(null);
		player.setCustomNameVisible(false);
	}
	
	public void setNamePlayer(String name, Player player) {
		String uuid=String.valueOf(player.getUniqueId());
		List<String> l=yml.getStringList("Player.using");
		if(l.contains(uuid)) {
			this.setPlayerNameTag(player, yml.getString("Player.names."+uuid+".realName"));
			this.removeCurrentName(player);
			this.removePlayerToListUse(uuid);
			List<String> list=yml.getStringList("Player.list-names");
			list.add(name);
			this.yml.set("Player.list-names", list);
			this.yml.set("Player.names."+uuid+".newName",name);
			this.yml.set("Player.names."+uuid+".realName",player.getName());
			this.saveFile();
			player.setDisplayName(name);
			player.setPlayerListName(name);
			player.setCustomName(name);
			player.setCustomNameVisible(true);
			this.addPlayerToListUse(uuid);
			this.setPlayerNameTag(player, name);
		}else {
			List<String> list=yml.getStringList("Player.list-names");
			list.add(name);
			this.yml.set("Player.list-names", list);
			this.yml.set("Player.names."+uuid+".newName",name);
			this.yml.set("Player.names."+uuid+".realName",player.getName());
			this.saveFile();
			player.setDisplayName(name);
			player.setPlayerListName(name);
			player.setCustomName(name);
			player.setCustomNameVisible(true);
			this.addPlayerToListUse(uuid);
			this.setPlayerNameTag(player, name);
		}
	}
	public void removePlayerToListUse(String uuid) {
		List<String> list=yml.getStringList("Player.using");
		list.remove(uuid);
		this.yml.set("Player.using", list);
		this.saveFile();
	}
	
	public void addPlayerToListUse(String uuid) {
		List<String> list=yml.getStringList("Player.using");
		list.add(uuid);
		this.yml.set("Player.using", list);
		this.saveFile();
	}
	
	private void saveFile() {
		try {
			this.yml.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String translateColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	@SuppressWarnings("deprecation")
	public void setPlayerNameTag(Player player, String name) {
	    try {
	        Method getHandle = player.getClass().getMethod("getHandle");
	        Object entityPlayer = getHandle.invoke(player);
	        boolean gameProfileExists = false;
	        try {
	            Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
	            gameProfileExists = true;
	        } catch (ClassNotFoundException ignored) {

	        }
	        try {
	            Class.forName("com.mojang.authlib.GameProfile");
	            gameProfileExists = true;
	        } catch (ClassNotFoundException ignored) {

	        }
	        if (!gameProfileExists) {
	            Field nameField = entityPlayer.getClass().getSuperclass().getDeclaredField("name");
	            nameField.setAccessible(true);
	            nameField.set(entityPlayer, name);
	        } else {
	            Object profile = entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer);
	            Field ff = profile.getClass().getDeclaredField("name");
	            ff.setAccessible(true);
	            ff.set(profile, name);
	        }
	        for(Player p:MainClass.getInstance().getServer().getOnlinePlayers()) {
	        	p.hidePlayer(player);
                p.showPlayer(player);
	        	
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void sendMessageToAll(String msg) {
		Bukkit.getServer().broadcastMessage(translateColor(msg));
	}
}
