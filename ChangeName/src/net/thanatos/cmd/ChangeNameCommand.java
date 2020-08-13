package net.thanatos.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.thanatos.main.MainClass;
import net.thanatos.utils.SourceCodes;

public class ChangeNameCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("changename")) {
			return false;
		}else {
			if(sender instanceof Player) {
				Player player=(Player)sender;
				if(player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.op")) || player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.allowed"))) {
					if(args.length<1) {
						if(player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.op"))) {
							player.sendMessage(SourceCodes.translateColor("&cError: Usage /cn <name> or /cn <online player> <name> or /cn <toggle,reset,view,reload>"));							
						}else if(player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.allowrd"))) {
							player.sendMessage(SourceCodes.translateColor("&cError: Usage /cn <name,reset>"));
						}
					}else {
						if(player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.op"))) {
							if(args[0].equalsIgnoreCase("view")) {
								if(SourceCodes.getInstance().getStatus()==true) {
									SourceCodes.getInstance().view(player);									
								}else {
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.enable-pl")));
								}
							}else if(args[0].equalsIgnoreCase("reload")) {
								MainClass.getInstance().reloadConfig();
								player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Config.yml has been reloaded."));
							}else if(args[0].equalsIgnoreCase("toggle")) {
								if(SourceCodes.getInstance().getStatus()==true) {
									SourceCodes.getInstance().toggleStatus(false);
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.disable-pl")));
								}else {
									SourceCodes.getInstance().toggleStatus(true);
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.enable-pl")));
								}
							}else if(args[0].equalsIgnoreCase("reset")) {
								if(SourceCodes.getInstance().getStatus()==true) {
									if(SourceCodes.getInstance().getFile().getStringList("Player.using").contains(String.valueOf(player.getUniqueId()))) {
										SourceCodes.getInstance().removeCurrentName(player);
										player.sendMessage(SourceCodes.translateColor("&a[ChangeName] You name has been reset."));
									}else {
										player.sendMessage(SourceCodes.translateColor("&cError: You are not using a name change"));
									}
								}else {
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.no-active")));
								}
							}else {
								if(SourceCodes.getInstance().getStatus()==true) {
									if(args.length==2) {
										Player target=MainClass.getInstance().getServer().getPlayer(args[0]);
										if(target!=null) {
											if(args[1].equalsIgnoreCase("reset")) {
												if(SourceCodes.getInstance().getFile().getStringList("Player.using").contains(String.valueOf(target.getUniqueId()))) {
													SourceCodes.getInstance().removeCurrentName(target);
													player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Player "+args[0]+"'s name has been reset."));
													target.sendMessage(SourceCodes.translateColor("&e[ChangeName] Your name has been reset by "+player.getName()));
												}else {
													player.sendMessage(SourceCodes.translateColor("&cError: "+args[0]+" are not using a name change"));
												}
											}else {
												String newname=args[1];
												if(newname.length()<16) {
													Player sp=Bukkit.getPlayer(newname);
													if(sp!=null) {
														if(!sp.isOnline()) {
															if(!SourceCodes.getInstance().verifyName(newname)) {
																SourceCodes.getInstance().setNamePlayer(newname,target);
																player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Player "+args[0]+"'s name has been changed to "+newname));
																target.sendMessage(SourceCodes.translateColor("&e[ChangeName] Your name has been changed to "+newname+" by "+player.getName()));
															}else {
																player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
															}
														}else {
															player.sendMessage(SourceCodes.translateColor("&cError: That name is already in use."));
														}
													}else {
														if(!SourceCodes.getInstance().verifyName(newname)) {
															SourceCodes.getInstance().setNamePlayer(newname,target);
															player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Player "+args[0]+"'s name has been changed to "+newname));
															target.sendMessage(SourceCodes.translateColor("&e[ChangeName] Your name has been changed to "+newname+" by "+player.getName()));
														}else {
															player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
														}
													}
												}else {
													player.sendMessage(SourceCodes.translateColor("&cError: New name must be less than 16 characters."));
												}
											}
										}
									}else if(args.length==1){
										String newname=args[0];
										if(newname.length()<16) {
											Player sp=Bukkit.getPlayer(newname);
											if(sp!=null) {
												if(!sp.isOnline()) {
													if(!SourceCodes.getInstance().verifyName(newname)) {
														SourceCodes.getInstance().setNamePlayer(newname, player);
														player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Your name has been changed to "+newname));
													}else {
														player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
													}
												}else {
													player.sendMessage(SourceCodes.translateColor("&cError: That name is already in use."));
												}
											}else {
												if(!SourceCodes.getInstance().verifyName(newname)) {
													SourceCodes.getInstance().setNamePlayer(newname, player);
													player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Your name has been changed to "+newname));
												}else {
													player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
												}
											}
										}else {
											player.sendMessage(SourceCodes.translateColor("&cError: New name must be less than 16 characters."));
										}
									}else {
										player.sendMessage(SourceCodes.translateColor("&cError: Operation not supported."));
									}
								}else {
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.no-active")));
								}
							}
						}else if(player.hasPermission(MainClass.getInstance().getConfig().getString("Config.permissions.allowed"))) {
							if(args[0].equalsIgnoreCase("reset")) {
								if(SourceCodes.getInstance().getStatus()==true) {
									if(SourceCodes.getInstance().getFile().getStringList("Player.using").contains(String.valueOf(player.getUniqueId()))) {
										SourceCodes.getInstance().removeCurrentName(player);
										player.sendMessage(SourceCodes.translateColor("&a[ChangeName] Your name has been reset."));
									}else {
										player.sendMessage(SourceCodes.translateColor("&cError: You are not using a name change"));
									}
								}else {
									player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.no-active")));
								}
							}else {
								String newname=args[0];
								if(newname.length()<16) {
									Player sp=Bukkit.getPlayer(newname);
									if(sp!=null) {
										if(!sp.isOnline()) {
											if(!SourceCodes.getInstance().verifyName(newname)) {
												SourceCodes.getInstance().setNamePlayer(newname, player);
											}else {
												player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
											}
										}else {
											player.sendMessage(SourceCodes.translateColor("&cError: That name is already in use."));
										}
									}else {
										if(!SourceCodes.getInstance().verifyName(newname)) {
											SourceCodes.getInstance().setNamePlayer(newname, player);
										}else {
											player.sendMessage(SourceCodes.translateColor("&cError: Another player is using that user."));
										}
									}
								}else {
									player.sendMessage(SourceCodes.translateColor("&cError: New name must be less than 16 characters."));									
								}
							}
						}
					}
				}else {
					player.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.no-permission")));
				}
			}else {
				sender.sendMessage(SourceCodes.translateColor(MainClass.getInstance().getConfig().getString("Config.message.no-permission")));
			}
		}
		return false;
	}

}
