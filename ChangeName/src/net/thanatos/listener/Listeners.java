package net.thanatos.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.thanatos.main.MainClass;
import net.thanatos.utils.SourceCodes;

public class Listeners implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player=e.getPlayer();
		if(SourceCodes.getInstance().getStatus()==true) {
			e.setJoinMessage(null);
			if(SourceCodes.getInstance().getFile().getStringList("Player.using").contains(String.valueOf(player.getUniqueId()))) {
				SourceCodes.getInstance().setNamePlayer(SourceCodes.getInstance().getFile().getString("Player.names."+String.valueOf(player.getUniqueId())+".newName"), player);
			}
		}else {
			player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.enable-pl")));
		}
	}

}
