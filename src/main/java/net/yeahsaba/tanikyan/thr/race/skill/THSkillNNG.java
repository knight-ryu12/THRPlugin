package net.yeahsaba.tanikyan.thr.race.skill;

import java.util.List;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class THSkillNNG extends JavaPlugin {
	//移動スキル系

	//攻撃スキル系
	//魔法
	//土魔法
	public static void magic_dirt(Player pl,Plugin plugin, PlayerInteractEvent event){
		pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "土の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.PISTON_EXTEND, 1.0F, -1.0F);
		List<Entity> enemys = pl.getNearbyEntities(12.0D, 12.0D, 12.0D);
		for (Entity enemy : enemys) {
			if (((enemy instanceof LivingEntity)) && (enemy.isOnGround())){
				((LivingEntity)enemy).damage(25.0D);
				enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.HORSE_HIT, 1.0F, 0.0F);
			}
		}
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Player pl = event.getPlayer();
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 60L);
	}
	//風魔法
	public static void magic_wind(Player pl, Plugin plugin, PlayerInteractEvent event){
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "風の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
		pl.setVelocity(pl.getVelocity().add(new Vector(0.5D, 3.0D, 0.5D)));
		pl.setFallDistance(-40.0F);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Player pl = event.getPlayer();
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 60L);
	}
}
