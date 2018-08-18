package com.chivalrycraft;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Rel;
/**
 * 
 * @author FlameKnight15
 *
 */
public class Core extends JavaPlugin implements Listener{
	Player player;
	ArrayList<Player> opFly = new ArrayList<Player>();
	ArrayList<Player> noParticles = new ArrayList<Player>();
	//Fired when plugin is first enabled
	@Override
	public void onEnable(){
		getLogger().info("onEnable has been invoked!");
		saveConfig();
		registerEvents(this,new PlayerMove(this));
	}
	//Fired when plugin is disabled
	@Override
	public void onDisable(){
		getLogger().info("onDisable has been invoked!");
		saveConfig();
	}
	
	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
		Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
		}

	
	public void grounded(Player p){
		Location loc = p.getLocation();
		int y = loc.getBlockY() + 1;
		for(int checkY = y; checkY > 0; checkY--){
			Location newLoc = new Location(p.getWorld(), loc.getX(), checkY, loc.getZ());
		    
			Block b = newLoc.getBlock();
			if(b.getType() != Material.AIR){
				Location newestLoc = new Location(p.getWorld(), newLoc.getX(), checkY + 1, newLoc.getZ());
				newestLoc.setYaw(p.getLocation().getYaw());
			    newestLoc.setPitch(p.getLocation().getPitch());
			    double health = p.getHealth();
				p.teleport(newestLoc);
				p.setHealth(health);
				p.sendMessage(ChatColor.YELLOW + "(!) You have been grounded!");
				break;
			}
		}
	}
	
	public boolean canFlyHere(Location loc, Player p){
		Faction pFaction = FPlayers.i.get(p).getFaction();
		Rel rel = pFaction.getRelationTo(Board.getFactionAt(loc));
		if(opFly.contains(p)){return true;}else
		
			if(pFaction.getTag().equals(Board.getFactionAt(loc).getTag())){
				if(p.hasPermission("ffly.yours")){
					return true;} return false;
			}else
			if(rel == Rel.ALLY){
				if(p.hasPermission("ffly.ally")){
					return true;} return false;
			}else
			if(rel == Rel.NEUTRAL){
				if(p.hasPermission("ffly.neutral")){
					return true;} return false;
			}else
			if(rel == Rel.ENEMY){
				if(p.hasPermission("ffly.enemy")){
					return true;} return false;
			}else
			if(rel == Rel.TRUCE){
				if(p.hasPermission("ffly.truce")){
					return true;} return false;
			}else
			if(Board.getFactionAt(loc).getTag().equals(ChatColor.DARK_RED + "WarZone")){
				if(p.hasPermission("ffly.warzone")){
					return true;} return false;
			}else
			if(Board.getFactionAt(loc).getTag().equals(ChatColor.YELLOW + "SafeZone")){
				if(p.hasPermission("ffly.safezone")){
					return true;} return false;
			}else
			if(Board.getFactionAt(loc).getTag().equals(ChatColor.DARK_GREEN + "Wilderness")){
				if(p.hasPermission("ffly.wilderness")){
					return true;} return false;
			}

		return false;
	}
	
	  void handleFly(Player p)
	  {
	    if (!p.getAllowFlight()) {
	      p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight has been " +ChatColor.GREEN+"ENABLED");
	      p.setAllowFlight(true);
	      p.setFlying(true);
	    } else {
	      p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight has been " +ChatColor.RED+"DISABLED");
	      grounded(p);
	      p.setAllowFlight(false);
	      p.setFlying(false);
	    }
	  }
	  
	  void handleFlyOp(Player p, Player admin)
	  {
	    if (!p.getAllowFlight()) {
	      p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight has been " +ChatColor.GREEN+"ENABLED");
	      p.setAllowFlight(true);
	      p.setFlying(true);
	      admin.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Flight have been " +ChatColor.GREEN+"ENABLED " + ChatColor.GRAY + "for player " + ChatColor.AQUA + p.getName());
	      opFly.add(p);
	    } else {
	      p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight has been " +ChatColor.RED+"DISABLED");
	      grounded(p);
	      p.setAllowFlight(false);
	      p.setFlying(false);
	      admin.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Flight have been " +ChatColor.RED+"DISABLED"  + ChatColor.GRAY + "for player " + ChatColor.AQUA + p.getName());
	      opFly.remove(p);
	    }
	  }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		Player p = (Player)sender;
		if(command.equals("fly") || alias.equals("fly") && args.length == 0){
			if(sender.hasPermission("ffly.fly")){
				//sender.sendMessage(FPlayers.i.get((Player)sender).getFaction().getTag().equals(Board.getFactionAt(((Player)sender).getLocation()).getTag()) + ", " + canFlyHere(((Player)sender).getLocation(), (Player)sender));
				if(canFlyHere(((Player)sender).getLocation(), (Player)sender)){
						handleFly((Player)sender);}
			else
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.RED + "You do not have permission to Fly here!");
					
				}else
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.RED + "You do not have permission to issue this command!");
			}
		if(command.equals("fly") || alias.equals("fly") && args.length == 1){
				if(sender.hasPermission("ffly.others")){
					if(Bukkit.getServer().getPlayer(args[0]).isOnline()){
				Player otherP = Bukkit.getServer().getPlayer(args[0]);
				handleFlyOp(otherP, (Player)sender);
				}
			else sender.sendMessage(ChatColor.RED + "That Player does not seem to exist and/or be online!");
				}else
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.RED + "You do not have permission to issue this command!");
		}
		if(command.equals("ground") || alias.equals("ground")){
			if(args.length == 1){
			if(Bukkit.getServer().getPlayer(args[0]).isOnline()){
				Player groundee = Bukkit.getServer().getPlayer(args[0]);
				grounded(groundee);
			}
			else sender.sendMessage(ChatColor.RED + "That Player does not seem to exist and/or be online!");
			}
			else
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Usage: /ground <playername> to ground that player!");
		}
		
		if(command.equals("flypart") || alias.equals("flypart")){
			if(args.length == 1){
				if(sender.hasPermission("ffly.others")){
				if(Bukkit.getServer().getPlayer(args[0]).isOnline()){
					if(!noParticles.contains(Bukkit.getServer().getPlayer(args[0]))){
						noParticles.add(Bukkit.getServer().getPlayer(args[0]));
						Bukkit.getServer().getPlayer(args[0]).sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight Particles have been " +ChatColor.RED+"DISABLED");
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.AQUA + Bukkit.getServer().getPlayer(args[0]).getName() + "'s"+ChatColor.GRAY + " Flight Particles have been " +ChatColor.RED+"DISABLED");
					}
					else{
						noParticles.remove(Bukkit.getServer().getPlayer(args[0]));
						Bukkit.getServer().getPlayer(args[0]).sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight Particles have been " +ChatColor.GREEN+"ENABLED");
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.AQUA + Bukkit.getServer().getPlayer(args[0]).getName() + "'s"+ChatColor.GRAY + " Flight Particles have been " +ChatColor.GREEN+"ENABLED");
					}
				}
				else sender.sendMessage(ChatColor.RED + "That Player does not seem to exist and/or be online!");
				}else sender.sendMessage(ChatColor.RED + "You do not have permission to toggle someone elses flight particles!");
			}else
				if(sender.hasPermission("ffly.particles")){
				if(!noParticles.contains(p)){
					noParticles.add(p);
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight Particles have been " +ChatColor.RED+"DISABLED");
				}else
					if(noParticles.contains(p)){
					noParticles.remove(p);
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight Particles have been " +ChatColor.GREEN+"ENABLED");
					}
				}else sender.sendMessage(ChatColor.RED + "You do not have permission to toggle flight particles!");
		}
		return true;
	}
	

	
}
