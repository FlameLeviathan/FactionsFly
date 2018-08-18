package com.chivalrycraft;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove  implements Listener{
	Core c;
	public PlayerMove(Core c){
		this.c = c;
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(e.getPlayer().getAllowFlight()){
			Location location = e.getPlayer().getLocation();
			if(!c.canFlyHere(location, e.getPlayer())){
			      p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» "+ChatColor.GRAY + "Your Flight has been " +ChatColor.RED+"DISABLED");
			      c.grounded(p);
			      p.setAllowFlight(false);
			      p.setFlying(false);
				e.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Chivalry"+ChatColor.DARK_GRAY+"» " + ChatColor.RED + "You do not have permission to Fly here!");
			}
			else{
				//Location newLoc = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY()-1,e.getPlayer().getLocation().getZ());
				if(c.noParticles.contains(e.getPlayer())){} else{
				Location newLoc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() -1, p.getLocation().getZ());
			    
				Block b = newLoc.getBlock();
				if(b.getType() == Material.AIR){
				ParticleEffect.CLOUD.display(1, -.5f, 1, 0, 5, e.getPlayer().getLocation()/*.add(.5, -1, .5)*/, 1);
				}
				}
			}
		}
	}
}
