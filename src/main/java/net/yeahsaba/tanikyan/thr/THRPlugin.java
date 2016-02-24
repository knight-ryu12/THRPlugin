package net.yeahsaba.tanikyan.thr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import net.minecraft.server.v1_8_R3.MathHelper;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.yeahsaba.tanikyan.thr.race.skill.THSkillNNG;
import net.yeahsaba.tanikyan.thr.race.skill.THSkillYUZ;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

public class THRPlugin extends JavaPlugin implements Listener {
	  public final Logger logger = Logger.getLogger("Minecraft");
	  public static THRPlugin plugin;
	  public final String touhouraces = ChatColor.WHITE + "[" + ChatColor.RED + "THR" + ChatColor.WHITE + "] " + ChatColor.RESET;
	  public static String thrpre = "[§cTHR§f] ";
	  public final PluginDescriptionFile pdfFile = getDescription();
	  public final Plugin plugin0 = this;
	  private SimpleClans sc;
	  private CSDirector cs;

		private static File pluginDir = new File("plugins", "THRPlugin");
	    public static File configfile = new File(pluginDir, "config.yml");
		public static FileConfiguration conf = YamlConfiguration.loadConfiguration(configfile);

	  public void onDisable(){
		  this.logger.info("[THR] Plugin Successfully Disabled!");
		  saveConfig();
	  }

	  public void onEnable(){
		  PluginDescriptionFile pdfFile = getDescription();
		  this.logger.info("[THR]" + pdfFile.getVersion() + " Has Successfully Been Enabled!");
		  Plugin plug = getServer().getPluginManager().getPlugin("SimpleClans");
		  if (plug != null){
			  this.sc = ((SimpleClans)plug);
			  this.logger.info("[THR]" + pdfFile.getVersion() + " Has Successfully hooked with SimpleClans");
		  }
		  Plugin plug2 = getServer().getPluginManager().getPlugin("CrackShot");
		  if (plug2 != null){
			  this.cs = ((CSDirector)plug2);
			  this.logger.info("[THR]" + pdfFile.getVersion() + " Has Successfully hooked with CrackShot");
		  }
		  PluginManager pm = getServer().getPluginManager();
		  pm.registerEvents(this, this);
		  saveDefaultConfig();

		  getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			  public void run(){
				  for (final Player player : Bukkit.getOnlinePlayers()){
					  if (player.hasMetadata("batman")) {
						  for (final LivingEntity bat : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(Bat.class)) {
							  if (bat.hasMetadata("invincible")) {
								  if (((MetadataValue)player.getMetadata("batman").get(0)).asString().toString().contains(((MetadataValue)bat.getMetadata("invincible").get(0)).asString().toString())) {
									  THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable(){
										  public void run()	{
											  player.teleport(bat);
											  player.setGameMode(GameMode.SURVIVAL);
											  MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
											  player.setMetadata("using-magic", usingmagic);
											  player.removeMetadata("batman", THRPlugin.this.plugin0);
											  player.sendMessage(THRPlugin.this.touhouraces + ChatColor.RED + "バンプカモフラージュの効果が切れました");
											  bat.removeMetadata("invincible", THRPlugin.this.plugin0);
											  bat.damage(1000.0D);
										  }
									  }, 100L);
								  }
							  }
						  }
					  }
					  if ((THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") < 100.0D) && (((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble() == 0.0D)){
						  THRPlugin.this.getConfig().set("user." + player.getUniqueId() + ".spilit", Double.valueOf(THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") + 5.0D));
						  if (player.isSneaking()) {
							  player.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + THRPlugin.this.getConfig().getDouble(new StringBuilder("user.").append(player.getUniqueId()).append(".spilit").toString()));
						  }
					  }else if ((THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") < 100.0D) && (((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble() < 0.0D)){
						  THRPlugin.this.getConfig().set("user." + player.getUniqueId() + ".spilit", Double.valueOf(THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") - ((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble()));
						  player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0F, -1.0F);
						  if (player.isSneaking()) {
							  player.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + THRPlugin.this.getConfig().getDouble(new StringBuilder("user.").append(player.getUniqueId()).append(".spilit").toString()));
						  }
					  }else if ((THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") > 0.0D) && (((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble() > 0.0D)){
						  THRPlugin.this.getConfig().set("user." + player.getUniqueId() + ".spilit", Double.valueOf(THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") - ((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble()));
						  if (player.isSneaking()) {
							  player.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + THRPlugin.this.getConfig().getDouble(new StringBuilder("user.").append(player.getUniqueId()).append(".spilit").toString()));
						  }
					  }
					  if (player.hasPermission("thr.skill")){
						  if (!player.hasMetadata("ignoreskill")) {
							  if ((player.hasMetadata("satorin0")) && (player.isSneaking())){
								  Player dpl = Bukkit.getPlayer(((MetadataValue)player.getMetadata("satorin0").get(0)).asString());
								  if (dpl != null){
									  player.sendMessage("名前:" + ((MetadataValue)player.getMetadata("satorin0").get(0)).asString());
									  player.sendMessage("体力:" + dpl.getHealth());
									  player.sendMessage("座標:" + dpl.getLocation().getBlockX() + "," + dpl.getLocation().getBlockY() + "," + dpl.getLocation().getBlockZ());
								  }
							  }
						  }
						  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("youma")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kappa")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("tenngu"))){
							  if (!player.isDead()) {
								  if (player.getHealth() > player.getMaxHealth() - 2.0D) {
									  player.setHealth(player.getMaxHealth());
								  } else {
									  player.setHealth(2.0D + player.getHealth());
								  }
							  }
						  }else if (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kennyou")){
							  if ((!player.isDead()) && (THRPlugin.this.getConfig().getDouble("user." + player.getUniqueId() + ".spilit") >= 10.0D) && (((MetadataValue)player.getMetadata("spilituse").get(0)).asDouble() > 0.0D)){
								  if (player.getHealth() > player.getMaxHealth() - 5.0D) {
									  player.setHealth(player.getMaxHealth());
								  } else {
									  player.setHealth(5.0D + player.getHealth());
								  }
							  }else if (!player.isDead()) {
								  player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0F, 2.0F);
							  }
						  }else if (!player.isDead()) {
							  if (player.getHealth() > player.getMaxHealth() - 1.0D) {
								  player.setHealth(player.getMaxHealth());
							  } else {
								  player.setHealth(1.0D + player.getHealth());
							  }
						  }
					  }
				  }
			  }
		  }, 0L, 102L);
		  getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			  public void run()	{
				  for (Player player : Bukkit.getOnlinePlayers()){
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("youma")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kappa")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("tenngu")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kennyou"))){
						  player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						  player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2000, 0));
						  if(THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kappa")){
							  player.removePotionEffect(PotionEffectType.WATER_BREATHING);
							  player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2000, 0));
						  }
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("akuma")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kyuuketuki")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("oni"))){
						  player.removePotionEffect(PotionEffectType.ABSORPTION);
						  player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2000, 1));
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("yousei")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("satori")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kobito")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kibito"))){
						  player.removePotionEffect(PotionEffectType.JUMP);
						  player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2000, 1));
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("youzyuu")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("siki")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("zyuuzin")) || (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("ninngyo"))) {
						  if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
							  player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 0));
						  }
						  if(THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("ninngyo")){
							  player.removePotionEffect(PotionEffectType.WATER_BREATHING);
							  player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2000, 0));
						  }
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("zyuuzin")) && (player.getWorld().getTime() >= 16000L)){
						  if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
							  player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 1));
						  }
						  player.removePotionEffect(PotionEffectType.JUMP);
						  player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						  player.removePotionEffect(PotionEffectType.REGENERATION);
						  player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2000, 0));
						  player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2000, 0));
						  player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2000, 0));
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("zyuuzin")) && (player.getWorld().getTime() >= 16000L) && (player.getWorld().getTime() < 16100L)){
						  player.sendMessage(THRPlugin.this.touhouraces + ChatColor.RED + "あなたは獣の血を呼び覚ました！！");
						  player.playSound(player.getLocation(), Sound.WOLF_DEATH, 1.0F, -1.0F);
					  }
					  if ((THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kyuuketuki")) && (player.getWorld().getTime() >= 14000L)){
						  player.removePotionEffect(PotionEffectType.NIGHT_VISION);
						  player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						  if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
							  player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 1));
						  }
						  player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2000, 0));
						  player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000, 0));
						  player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 1));
					  }else if (THRPlugin.this.getConfig().getString("user." + player.getUniqueId() + ".race").toString().contains("kyuuketuki")){
						  player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2000, 0));
					  }
				  }
			  }
		  }, 0L, 301L);
		  getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			  public void run(){
				  for (Bat bat : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(Bat.class)) {
					  if (bat.hasMetadata("invincible")){
						  List<Entity> entityforsyugorei = bat.getNearbyEntities(20.0D, 20.0D, 20.0D);
						  for (Entity entity : entityforsyugorei) {
							  if ((entity instanceof Player)){
								  bat.setVelocity(bat.getVelocity().add(new Vector(new Double(20.0D - (bat.getLocation().getX() - entity.getLocation().getX())).doubleValue() / 16.0D, 0.0D, new Double(20.0D - (bat.getLocation().getZ() - entity.getLocation().getZ())).doubleValue() / 16.0D)));
								  break;
							  }
						  }
					  }
				  }
				  for (final Snowman snowman : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(Snowman.class)) {
					  if (snowman.hasMetadata("syugoreisnow")){
						  if (snowman.hasMetadata("syugoreitarget")){
							  List<Entity> entityforsyugorei = snowman.getNearbyEntities(20.0D, 20.0D, 20.0D);
							  for (Entity entity : entityforsyugorei) {
								  if ((entity instanceof Player)) {
									  if (!((Player)entity).getName().toString().contains(((MetadataValue)snowman.getMetadata("syugoreitarget").get(0)).asString())){
										  snowman.setTarget((LivingEntity)entity);
										  break;
									  }
								  }
							  }
						  }
						  THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable(){
							  public void run(){
								  snowman.damage(1000.0D);
							  }
						  }, 300L);
					  }
				  }
				  for (final IronGolem irongolem : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(IronGolem.class)) {
					  if (irongolem.hasMetadata("syugoreiiron")){
						  if (irongolem.hasMetadata("syugoreitarget")) {
							  if (irongolem.getMetadata("syugoreitarget").get(0) != null){
								  List<Entity> entityforsyugorei = irongolem.getNearbyEntities(20.0D, 20.0D, 20.0D);
								  for (Entity entity : entityforsyugorei) {
									  if ((entity instanceof Player)) {
										  if (!((Player)entity).getName().toString().contains(((MetadataValue)irongolem.getMetadata("syugoreitarget").get(0)).asString())){
											  irongolem.setTarget((LivingEntity)entity);
											  break;
										  }
									  }
								  }
							  }
						  }
						  THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable(){
							  public void run(){
								  irongolem.damage(1000.0D);
							  }
						  }, 300L);
					  }
				  }
				  for (Wolf wolf : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(Wolf.class)) {
					  if (wolf.hasMetadata("tamedwolf")) {
						  if (wolf.hasMetadata("wolfowner")){
							  String owner = ((MetadataValue)wolf.getMetadata("wolfowner").get(0)).asString();
							  for (Entity enemy : wolf.getNearbyEntities(10.0D, 10.0D, 10.0D)) {
								  if ((enemy instanceof LivingEntity)) {
									  if ((enemy instanceof Player)){
										  if (!((Player)enemy).getUniqueId().toString().contains(owner)){
											  wolf.setTarget((LivingEntity)enemy);
											  break;
										  }
									  }else if (!(enemy instanceof Wolf)){
										  wolf.setTarget((LivingEntity)enemy);
										  break;
									  }
								  }
							  }
						  }
					  }
				  }
				  for (Ocelot cat : Bukkit.getWorld(THRPlugin.this.getConfig().getString("enableworld")).getEntitiesByClass(Ocelot.class)) {
					  if (cat.hasMetadata("tamedcat")) {
						  if (cat.hasMetadata("catowner")){
							  String owner = ((MetadataValue)cat.getMetadata("catowner").get(0)).asString();
							  for (Entity enemy : cat.getNearbyEntities(10.0D, 10.0D, 10.0D)) {
								  if ((enemy instanceof LivingEntity)) {
									  if ((enemy instanceof Player)){
										  if (!((Player)enemy).getUniqueId().toString().contains(owner)){
											  cat.setTarget((LivingEntity)enemy);
											  cat.teleport((LivingEntity)enemy);
											  break;
										  }
									  }else if (!(enemy instanceof Ocelot)){
										  cat.setTarget((LivingEntity)enemy);
										  cat.teleport((LivingEntity)enemy);
										  break;
									  }
								  }
							  }
						  }
					  }
				  }
				  THRPlugin.this.saveConfig();
			  }
		  }, 0L, 200L);
	  }

	  public boolean onCommand(CommandSender sender, Command mcd, String commandLabel, String[] args){
		  if ((commandLabel.equalsIgnoreCase("touhouraces")) && ((sender instanceof Player)) && (args.length == 0)){
			  Player pl = (Player)sender;
			  pl.sendMessage(this.touhouraces + "Version " + this.pdfFile.getVersion() + ". Made by:" + this.pdfFile.getAuthors().toString());
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && ((sender instanceof Player)) && (args.length == 0)){
			  Player pl = (Player)sender;
			  pl.sendMessage(this.touhouraces + "Version " + this.pdfFile.getVersion() + ". Made by:" + this.pdfFile.getAuthors().toString());
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("reload")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.reload")) || (pl.isOp())){
					  saveConfig();
					  reloadConfig();
					  sender.sendMessage(this.touhouraces + "§6 リロードしました.");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("help")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.help")) || (pl.hasPermission("thr.user"))){
					  reloadConfig();
					  sender.sendMessage(this.touhouraces + "§6可能なプラグイン一覧");
					  sender.sendMessage("§btouhouraces/thr : バージョン説明");
					  sender.sendMessage("thr reload : リロード");
					  sender.sendMessage("thr mana : 現在マナ確認");
					  sender.sendMessage("thr heal-mana [num] : マナをnum分回復する");
					  sender.sendMessage("thr racelist : オンラインでいるプレイヤーの種族の統計をとる");
					  sender.sendMessage("thr toggleskill : 行動系のスキルの発動をトグルする");
					  sender.sendMessage("thr setpoint [num] : 自分のポイント（使い方は任意）を設定する");
					  sender.sendMessage("thr addpoint [num] : 自分のポイント（使い方は任意）を追加する");
					  sender.sendMessage("thr setpoint [playername] [num] : playernameのポイント（使い方は任意）を設定する");
					  sender.sendMessage("thr addpoint [playername] [num] : playernameのポイント（使い方は任意）を追加する");
					  sender.sendMessage("thr steppoint [max] : ポイント（使い方は任意）をmaxを上限として1上昇する");
					  sender.sendMessage("thr steppoint [playername] [max] : playernameのポイント（使い方は任意）をmaxを上限として1上昇する");
					  sender.sendMessage("thr setrace  [内部種族名] : 自分の種族を種族名（内部名）に変更する");
					  sender.sendMessage("thr setrace [playername] [内部種族名] : playernameの種族を種族名（内部名）に変更する");
					  sender.sendMessage("thr setrace [playername] [内部種族名] : playernameの種族を種族名（内部名）に変更する");
					  sender.sendMessage("thr setrace [playername] [内部種族名] : playernameの種族を種族名（内部名）に変更する");
					  sender.sendMessage("thr setrace [playername] [内部種族名] : playernameの種族を種族名（内部名）に変更する");
					  sender.sendMessage("thr info : playernameの種族の情報を表示する");
					  sender.sendMessage("thr evolinfo [内部種族名] : 種族の情報を表示する");
					  sender.sendMessage("thr evollist : playernameの進化できる種族のリストを表示する");
					  sender.sendMessage("thr evolchange [内部種族名] : 種族の進化を試みる");
				  }else{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("mana")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.checkmana")) || (pl.hasPermission("thr.user"))) {
					  pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
				  } else {
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("heal-mana")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if (pl.hasPermission("thr.healmana")){
					  getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") + Integer.parseInt(args[1])));
					  pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
				  }else{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("racelist")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.racelist")) || (pl.hasPermission("thr.user"))){
					  OfflinePlayer[] ppl = Bukkit.getOfflinePlayers();
					  pl.sendMessage(this.touhouraces + "§a オンライン中の種族リスト.");
					  int p = 0;
					  while (p < ppl.length){
						  if (ppl[p].isOnline()) {
							  pl.sendMessage(ChatColor.GREEN + "+" + getConfig().getString(new StringBuilder("user.").append(ppl[p].getUniqueId()).append(".name").toString()) + ":" + getConfig().getString(new StringBuilder("user.").append(ppl[p].getUniqueId()).append(".race").toString()) + "(" + getConfig().getString(new StringBuilder("user.").append(ppl[p].getUniqueId()).append(".point").toString()) + ")");
						  }
						  p++;
					  }
				  }else{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("toggleskill")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.toggleskill")) || (pl.hasPermission("thr.user"))){
					  if (pl.hasMetadata("ignoreskill")){
						  pl.removeMetadata("ignoreskill", this.plugin0);
						  pl.sendMessage(this.touhouraces + ChatColor.DARK_AQUA + "行動スキルは再び発動します");
					  }else{
						  MetadataValue ignoreskill = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
						  pl.setMetadata("ignoreskill", ignoreskill);
						  pl.sendMessage(this.touhouraces + ChatColor.RED + "行動スキルを封印しました");
					  }
				  }else {
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("setrace")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setrace")){
					  getConfig().set("user." + commander.getUniqueId() + ".race", args[1].toString());
					  saveConfig();
					  commander.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたは種族が" + getConfig().getString(new StringBuilder("user.").append(commander.getUniqueId()).append(".race").toString()) + "になりました。");
				  }else{
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("setrace")) && (args.length == 3)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setrace")){
					  if (Bukkit.getPlayer(args[1]) != null){
						  Player pl = Bukkit.getPlayer(args[1]);
						  getConfig().set("user." + pl.getUniqueId() + ".race", args[2].toString());
						  saveConfig();
						  commander.sendMessage(this.touhouraces + ChatColor.AQUA + pl.getName() + "の種族を" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".race").toString()) + "にしました。");
						  pl.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたは種族が" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".race").toString()) + "になりました。");
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("setpoint")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setpoint")){
					  if (Bukkit.getPlayer(args[1]) != null){
						  int point = Integer.parseInt(args[1]);
						  getConfig().set("user." + commander.getUniqueId() + ".point", Integer.valueOf(point));
						  saveConfig();
						  commander.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(commander.getUniqueId()).append(".point").toString()) + "になりました。");
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("setpoint")) && (args.length == 3)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setpoint")){
					  if (Bukkit.getPlayer(args[1]) != null){
						  Player pl = Bukkit.getPlayer(args[1]);
						  int point = Integer.parseInt(args[2]);
						  getConfig().set("user." + pl.getUniqueId() + ".point", Integer.valueOf(point));
						  saveConfig();
						  commander.sendMessage(this.touhouraces + ChatColor.AQUA + pl.getName() + "のポイントを" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
						  pl.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("addpoint")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setpoint")){
					  int point = Integer.parseInt(args[1]);
					  getConfig().set("user." + commander.getUniqueId() + ".point", Integer.valueOf(getConfig().getInt("user." + commander.getUniqueId() + ".point") + point));
					  saveConfig();
					  commander.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(commander.getUniqueId()).append(".point").toString()) + "になりました。");
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("addpoint")) && (args.length == 3)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.setpoint")){
					  if (Bukkit.getPlayer(args[1]) != null){
						  Player pl = Bukkit.getPlayer(args[1]);
						  int point = Integer.parseInt(args[2]);
						  getConfig().set("user." + pl.getUniqueId() + ".point", Integer.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".point") + point));
						  saveConfig();
						  commander.sendMessage(this.touhouraces + ChatColor.AQUA + pl.getName() + "のポイントを" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
						  pl.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("steppoint")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.steppoint")){
					  if (getConfig().getInt("user." + commander.getUniqueId() + ".point") < Integer.parseInt(args[1])){
						  getConfig().set("user." + commander.getUniqueId() + ".point", Integer.valueOf(getConfig().getInt("user." + commander.getUniqueId() + ".point") + 1));
						  saveConfig();
						  commander.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(commander.getUniqueId()).append(".point").toString()) + "になりました。");
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("steppoint")) && (args.length == 3)){
			  if ((sender instanceof Player)){
				  Player commander = ((Player)sender).getPlayer();
				  if (commander.hasPermission("thr.steppoint")){
					  if (Bukkit.getPlayer(args[1]) != null){
						  Player pl = Bukkit.getPlayer(args[1]);
						  if (getConfig().getInt("user." + commander.getUniqueId() + ".point") < Integer.parseInt(args[2])){
							  getConfig().set("user." + pl.getUniqueId() + ".point", Integer.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".point") + 1));
							  saveConfig();
							  commander.sendMessage(this.touhouraces + ChatColor.AQUA + pl.getName() + "のポイントを" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
							  pl.sendMessage(this.touhouraces + ChatColor.AQUA + "あなたはポイントが" + getConfig().getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
						  }
					  }
				  }else {
					  commander.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません！");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("evollist")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.evol.user.list")) || (pl.hasPermission("thr.user"))){
					  pl.sendMessage(this.touhouraces + ChatColor.AQUA + pl.getName() + "の進化できる先リスト");
					  List<String> evolraces = new ArrayList();
					  for (String race : getConfig().getConfigurationSection("race").getKeys(false)) {
						  if (getConfig().getString("race." + race + ".racetype.root").contains(getConfig().getString("user." + pl.getUniqueId() + ".race"))) {
							  evolraces.add(race);
						  }
					  }
					  for (String evolrace : evolraces) {
						  pl.sendMessage(getConfig().getString(new StringBuilder("race.").append(evolrace).append(".display.real").toString()) + "：内部name＞" + evolrace);
					  }
				  }else	{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません。");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("evolinfo")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.evol.user.info")) || (pl.hasPermission("thr.user"))){
					  boolean existrace = false;
					  String inforace = "";
					  for (String race : getConfig().getConfigurationSection("race").getKeys(false)) {
						  if (race.toLowerCase().contains(args[1].toLowerCase())){
							  existrace = true;
							  inforace = race;
							  break;
						  }
					  }
					  if (existrace){
						  pl.sendMessage(getConfig().getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "：内部name＞" + inforace + "（" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".display.tag").toString()) + "）の情報");
						  pl.sendMessage("元種族：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()));
						  pl.sendMessage("ランク：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".racetype.rank").toString()));
						  pl.sendMessage("進化に必要な進化の欠片：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.shard").toString()));
						  pl.sendMessage("進化に必要な進化の宝石：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.crystal").toString()));
						  pl.sendMessage("進化に必要な種族素材：" + getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.amount").toString()) + "個の" + Material.getMaterial(getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.typeid").toString())) + "(メタ" + getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.meta").toString()) + "）");
						  pl.sendMessage(getConfig().getString("race." + inforace + ".intro.story"));
						  pl.sendMessage(getConfig().getString("race." + inforace + ".intro.skills"));
					  }else	{
						  pl.sendMessage(this.touhouraces + ChatColor.RED + "その種族内部nameは存在しません。");
					  }
				  }else	{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません。");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("info")) && (args.length == 1)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.info")) || (pl.hasPermission("thr.user"))){
					  boolean existrace = false;
					  String inforace = "";
					  for (String race : getConfig().getConfigurationSection("race").getKeys(false)) {
						  if (race.toLowerCase().contains(getConfig().getString("user." + pl.getUniqueId() + ".race").toLowerCase())){
							  existrace = true;
							  inforace = race;
							  break;
						  }
					  }
					  if (existrace){
						  pl.sendMessage(getConfig().getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "：内部name＞" + inforace + "（" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".display.tag").toString()) + "）の情報");
						  pl.sendMessage("元種族：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()));
						  pl.sendMessage("ランク：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".racetype.rank").toString()));
						  pl.sendMessage("進化に必要な進化の欠片：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.shard").toString()));
						  pl.sendMessage("進化に必要な進化の宝石：" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.crystal").toString()));
						  pl.sendMessage("進化に必要な種族素材：" + getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.amount").toString()) + "個の" + Material.getMaterial(getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.typeid").toString())) + "(メタ" + getConfig().getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.meta").toString()) + "）");
						  pl.sendMessage(getConfig().getString("race." + inforace + ".intro.story"));
						  pl.sendMessage(getConfig().getString("race." + inforace + ".intro.skills"));
					  }else {
						  pl.sendMessage(this.touhouraces + ChatColor.RED + "その種族内部nameは存在しません。");
					  }
				  }else	{
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません。");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args[0].equalsIgnoreCase("evolchange")) && (args.length == 2)){
			  if ((sender instanceof Player)){
				  Player pl = ((Player)sender).getPlayer();
				  if ((pl.hasPermission("thr.evol.user.change")) || (pl.hasPermission("thr.user"))){
					  boolean existrace = false;
					  String inforace = "";
					  for (String race : getConfig().getConfigurationSection("race").getKeys(false)) {
						  if (race.toLowerCase().contains(args[1].toLowerCase())){
							  existrace = true;
							  inforace = race;
							  break;
						  }
					  }
					  if (existrace){
						  if (getConfig().getString("race." + inforace + ".racetype.root").contains(getConfig().getString("user." + pl.getUniqueId() + ".race"))){
							  PlayerInventory inventory = pl.getInventory();
							  int ok_shard = 0;
							  int ok_crystal = 0;
							  int ok_raceitem = 0;
							  ItemStack shard = null;
							  ItemStack crystal = null;
							  ItemStack raceitem = null;
							  if (getConfig().getInt("race." + inforace + ".evol.evolpoint.shard") != 0){
								  shard = new ItemStack(Material.PRISMARINE_SHARD, getConfig().getInt("race." + inforace + ".evol.evolpoint.shard"));
								  if (inventory.contains(shard)) {
									  ok_shard = 1;
								  } else {
									  ok_shard = 2;
								  }
							  }
							  if (getConfig().getInt("race." + inforace + ".evol.evolpoint.crystal") != 0){
								  crystal = new ItemStack(Material.PRISMARINE_CRYSTALS, getConfig().getInt("race." + inforace + ".evol.evolpoint.crystal"));
								  if (inventory.contains(crystal)) {
									  ok_crystal = 1;
								  } else {
									  ok_shard = 2;
								  }
							  }
							  if (getConfig().getInt("race." + inforace + ".evol.raceitem.amount") != 0){
								  raceitem = new ItemStack(Material.getMaterial(getConfig().getInt("race." + inforace + ".evol.raceitem.typeid")), getConfig().getInt("race." + inforace + ".evol.raceitem.amount"));
								  int raceitemmeta = getConfig().getInt("race." + inforace + ".evol.raceitem.meta");
								  raceitem.setDurability((short)raceitemmeta);
								  if (inventory.contains(raceitem)) {
									  ok_raceitem = 1;
								  } else {
									  ok_raceitem = 2;
								  }
							  }
							  if ((ok_shard == 2) || (ok_crystal == 2) || (ok_raceitem == 2)){
								  pl.sendMessage(this.touhouraces + ChatColor.RED + "その種族に進化する為のアイテムがありません！");
							  }else {
								  pl.playSound(pl.getLocation(), Sound.PORTAL_TRAVEL, 1.0F, 1.0F);
								  if (ok_shard == 1) {
									  pl.getInventory().remove(shard);
								  }
								  if (ok_crystal == 1) {
									  pl.getInventory().remove(crystal);
								  }
								  if (ok_raceitem == 1) {
									  pl.getInventory().remove(raceitem);
								  }
								  getConfig().set("user." + pl.getUniqueId() + ".race", inforace);
								  saveConfig();
								  Bukkit.broadcastMessage(this.touhouraces + ChatColor.BLUE + pl.getName() + "は" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()) + "から" + getConfig().getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "に進化した！！");

								  ItemStack rewarditem = null;
								  if (getConfig().getInt("race." + inforace + ".evol.rewarditem.amount") != 0){
									  rewarditem = new ItemStack(Material.getMaterial(getConfig().getInt("race." + inforace + ".evol.rewarditem.typeid")), getConfig().getInt("race." + inforace + ".evol.rewarditem.amount"));
									  int rewarditemmeta = getConfig().getInt("race." + inforace + ".evol.rewarditem.meta");
									  rewarditem.setDurability((short)rewarditemmeta);
									  pl.getInventory().addItem(new ItemStack[] { rewarditem });
								  }
							  }
						  }else {
							  pl.sendMessage(this.touhouraces + ChatColor.RED + "進化できる種族ではありません！");
						  }
					  }else {
						  pl.sendMessage(this.touhouraces + ChatColor.RED + "その種族内部nameは存在しません。");
					  }
				  }else {
					  pl.sendMessage(this.touhouraces + ChatColor.RED + "権限がありません。");
				  }
			  }
		  }else if ((commandLabel.equalsIgnoreCase("thr")) && (args.length >= 1)){
			  sender.sendMessage(this.touhouraces + "§c コマンドが間違っているよ.");
		  }
		  return true;
	  }

	  @EventHandler(priority=EventPriority.LOW)
	  public void chatlegend(AsyncPlayerChatEvent event){
		  Player pl = event.getPlayer();
		  String format = event.getFormat();
		  if (getConfig().contains("user." + pl.getUniqueId())){
			  boolean existrace = false;
			  String inforace = "";
			  for (String race : getConfig().getConfigurationSection("race").getKeys(false)) {
				  if (race.toLowerCase().contains(getConfig().getString("user." + pl.getUniqueId() + ".race"))){
					  existrace = true;
					  inforace = race;
					  break;
				  }
			  }
			  if (existrace){
				  String race = getConfig().getString("race." + inforace + ".display.tag");
				  event.setFormat("§f[" + race + "§f]" + format);
			  }else {
				  String race = getConfig().getString("user." + pl.getUniqueId() + ".race");
				  event.setFormat("§f[" + race + "§f]" + format);
			  }
		  }
	  }

	  @EventHandler
	  public void on_quit(PlayerQuitEvent event){
		  Player pl = event.getPlayer();
		  for (LivingEntity bat : pl.getWorld().getEntitiesByClass(Bat.class)) {
			  if (bat.hasMetadata("invincible")) {
				  if (pl.hasMetadata("batman")) {
					  if (((MetadataValue)pl.getMetadata("batman").get(0)).asString().toString().contains(((MetadataValue)bat.getMetadata("invincible").get(0)).asString().toString())){
						  bat.removeMetadata("invincible", this.plugin0);
						  bat.damage(1000.0D);
					  }
				  }
			  }
		  }
		  if (pl.hasMetadata("batman")) {
			  pl.removeMetadata("batman", this.plugin0);
		  }
		  if (pl.hasMetadata("casting")) {
			  pl.removeMetadata("casting", this.plugin0);
		  }
		  if (pl.hasMetadata("using-magic")) {
			  pl.removeMetadata("using-magic", this.plugin0);
		  }
		  if (pl.hasMetadata("satorin0")) {
			  pl.removeMetadata("satorin0", this.plugin0);
		  }
		  if (pl.getGameMode() == GameMode.SPECTATOR) {
			  pl.setGameMode(GameMode.SURVIVAL);
		  }
		  if (pl.hasMetadata("freeze")) {
			  pl.removeMetadata("freeze", this.plugin0);
		  }
	  }

	  @EventHandler
	  public void on_join(PlayerJoinEvent event){
		  Player pl = event.getPlayer();
		  pl.removeMetadata("casting", this.plugin0);
		  pl.removeMetadata("using-magic", this.plugin0);
		  pl.removeMetadata("spilituse", this.plugin0);

		  MetadataValue casted = new FixedMetadataValue(this.plugin0, Boolean.valueOf(false));
		  pl.setMetadata("casting", casted);
		  MetadataValue usingmagic = new FixedMetadataValue(this.plugin0, Boolean.valueOf(false));
		  pl.setMetadata("using-magic", usingmagic);
		  MetadataValue spilituse = new FixedMetadataValue(this.plugin0, Integer.valueOf(0));
		  pl.setMetadata("spilituse", spilituse);
		  if (!getConfig().contains("user." + pl.getUniqueId())){
			  getConfig().set("user." + pl.getUniqueId() + ".name", pl.getName());
			  getConfig().set("user." + pl.getUniqueId() + ".point", Integer.valueOf(0));
			  getConfig().set("user." + pl.getUniqueId() + ".race", "kedama");
			  getConfig().set("user." + pl.getUniqueId() + ".spilit", Integer.valueOf(0));
			  saveConfig();
		  }
		  getConfig().set("user." + pl.getUniqueId() + ".spilit", Integer.valueOf(0));
		  saveConfig();
	  }

	  @EventHandler
	  public void on_respawn(PlayerRespawnEvent event){
		  Player pl = event.getPlayer();
		  if (pl.hasPermission("thr.skill")) {
			  if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("youma")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu"))) {
				  pl.setMaxHealth(120.0D);
			  } else if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kennyou")) {
				  pl.setMaxHealth(150.0D);
			  } else {
				  pl.setMaxHealth(100.0D);
			  }
		  }
	  }

	  @EventHandler
	  public void on_move(PlayerMoveEvent event){
		  if (event.getPlayer().hasMetadata("batman")) {
			  event.setCancelled(true);
		  }
		  Player pl = event.getPlayer();
		  if (pl.hasPermission("thr.skill")){
			  if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngyo")) {
				  if ((pl.getLocation().getBlock().getType() == Material.WATER) || (pl.getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
					  if ((!pl.isSneaking()) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 40.0D) && (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() > 0.0D)) {
						  pl.setVelocity(pl.getLocation().getDirection().multiply(0.7D));
					  }
				  }
			  }
			  if ((pl.hasMetadata("freeze")) && (pl.isOnGround())) {
				  event.setCancelled(true);
			  }
		  }
	  }

	  @EventHandler
	  public void on_sneaktoggle(PlayerToggleSneakEvent event)
	  {
	    Player pl = event.getPlayer();
	    if ((!pl.hasMetadata("ignoreskill")) && (pl.hasPermission("thr.skill")))
	    {
	      if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yousei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("satori")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kobito")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kibito")))
	      {
	        if ((!pl.isOnGround()) && (pl.isSneaking()) && (getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 20.0D))
	        {
	          pl.setVelocity(pl.getLocation().getDirection().multiply(1.1D));
	          pl.getWorld().playSound(pl.getLocation(), Sound.SHOOT_ARROW, 1.0F, 1.0F);
	          pl.getWorld().playEffect(pl.getLocation(), Effect.TILE_DUST, 133, 1);
	          getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 5.0D));
	          saveConfig();
	          pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	        }
	      }
	      else if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu")) {
	        if ((!pl.isOnGround()) && (pl.isSneaking()) && (getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 30.0D))
	        {
	          pl.setVelocity(pl.getLocation().getDirection().multiply(15.0D));
	          pl.getWorld().playSound(pl.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
	          pl.getWorld().playSound(pl.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 0.0F);
	          pl.getWorld().playSound(pl.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, -1.0F);
	          getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 40.0D));
	          saveConfig();
	          pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	        }
	      }
	      if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sennnin")) {
	        if ((!pl.isOnGround()) && (pl.isSneaking()) && (getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 20.0D))
	        {
	          float pitch = pl.getLocation().getPitch();
	          float yaw = pl.getLocation().getYaw();
	          Location warploc = new Location(pl.getWorld(), pl.getLocation().getX() + pl.getLocation().getDirection().getX() * 2.0D, pl.getLocation().getY() + pl.getLocation().getDirection().getY() * 2.0D, pl.getLocation().getZ() + pl.getLocation().getDirection().getZ() * 2.0D);
	          if (pl.getWorld().getBlockAt(warploc).getType() != Material.AIR)
	          {
	            pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_HIT, 2.0F, 0.0F);
	          }
	          else
	          {
	            pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0F, 1.0F);
	            pl.getWorld().playEffect(pl.getLocation(), Effect.COLOURED_DUST, 1, 5);
	            warploc.setPitch(pitch);
	            warploc.setYaw(yaw);
	            pl.teleport(warploc);
	            getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 20.0D));
	            saveConfig();
	          }
	        }
	      }
	    }
	  }

	  @EventHandler
	  public void on_click(final PlayerInteractEvent event)
	  {
	    Player pl = event.getPlayer();
	    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getClickedBlock().getType() == Material.PRISMARINE))
	    {
	      boolean existrace = false;
	      String useshopname = "";
	      for (String shopname : getConfig().getConfigurationSection("rankshop").getKeys(false)) {
	        if (getConfig().getString("rankshop." + shopname.toLowerCase() + ".world").contains(event.getClickedBlock().getLocation().getWorld().getName())) {
	          if ((getConfig().getInt("rankshop." + shopname.toLowerCase() + ".vector.x") == event.getClickedBlock().getLocation().getBlockX()) && (getConfig().getInt("rankshop." + shopname.toLowerCase() + ".vector.y") == event.getClickedBlock().getLocation().getBlockY()) && (getConfig().getInt("rankshop." + shopname.toLowerCase() + ".vector.z") == event.getClickedBlock().getLocation().getBlockZ()))
	          {
	            existrace = true;
	            useshopname = shopname;
	            break;
	          }
	        }
	      }
	      if (existrace)
	      {
	        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("faith");
	        int cost_faith = getConfig().getInt("rankshop." + useshopname + ".require.faith");
	        int cost_racepoint = getConfig().getInt("rankshop." + useshopname + ".require.racepoint");
	        ItemStack cost_item = new ItemStack(Material.getMaterial(getConfig().getInt("rankshop." + useshopname + ".require.item.typeid")), getConfig().getInt("rankshop." + useshopname + ".require.item.amount"));
	        cost_item.setDurability((short)getConfig().getInt("rankshop." + useshopname + ".require.item.meta"));
	        if ((objective.getScore(pl.getPlayer()).getScore() >= cost_faith) && (getConfig().getInt("user." + pl.getUniqueId() + ".point") >= cost_racepoint) && (pl.getInventory().contains(cost_item)))
	        {
	          objective.getScore(pl.getPlayer()).setScore(objective.getScore(pl.getPlayer()).getScore() - cost_faith);
	          pl.getInventory().remove(cost_item);

	          ItemStack buy_item = new ItemStack(Material.getMaterial(getConfig().getInt("rankshop." + useshopname + ".buyitem.typeid")), getConfig().getInt("rankshop." + useshopname + ".buyitem.amount"));
	          buy_item.setDurability((short)getConfig().getInt("rankshop." + useshopname + ".buyitem.meta"));
	          pl.getInventory().addItem(new ItemStack[] { buy_item });
	          pl.sendMessage(this.touhouraces + ChatColor.YELLOW + cost_faith + "の信仰と" + ChatColor.GOLD + cost_racepoint + "の種族ポイントと" + cost_item + "のアイテムを消費を消費して" + ChatColor.GREEN + buy_item.getAmount() + "個の" + buy_item.getType().name() + "(メタ:" + buy_item.getDurability() + ")を手に入れた！");
	        }
	        else if (objective.getScore(pl.getPlayer()).getScore() < cost_faith)
	        {
	          pl.sendMessage(this.touhouraces + ChatColor.RED + cost_faith + "分の信仰を持っていません！");
	        }
	        else if (getConfig().getInt("user." + pl.getUniqueId() + ".point") < cost_racepoint)
	        {
	          pl.sendMessage(this.touhouraces + ChatColor.RED + cost_racepoint + "分の種族ポイントを持っていません！");
	        }
	        else if (!pl.getInventory().contains(cost_item))
	        {
	          pl.sendMessage(cost_item.getAmount() + "個の" + cost_item.getType().name() + "(メタ:" + cost_item.getDurability() + ")がありません！！");
	        }
	      }
	    }
	    if ((pl.hasPermission("thr.skill")) && (!pl.hasMetadata("ignoreskill"))) {
	      if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK))
	      {
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("youzyuu")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("siki")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("zyuuzin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngyo"))) {
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 30.0D) && (pl.isSneaking())){
	            Material other_is_ok = pl.getItemInHand().getType();
	            if ((other_is_ok == Material.FISHING_ROD) || (other_is_ok == Material.BOW) || (other_is_ok == Material.ARROW)) {
	              if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	              {
	                pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	              }
	              else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	              {
	                pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	              }
	              else if (pl.getItemInHand().getType() == Material.FISHING_ROD)
	              {
	            	  if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("siki")){
	            		  THSkillYUZ.siki_summon_oc(pl, THRPlugin.this.plugin0, event);
	            	  }else{
	            		  THSkillYUZ.yuz_summon_wolf(pl, THRPlugin.this.plugin0, event);
	                }
	              }
	              else
	              {
	            	  THSkillYUZ.yuz_up(pl, THRPlugin.this.plugin0, event);
	              }
	            }
	          }
	        }
	      }
	      else if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
	      {
	        Material dust_is_ok = pl.getItemInHand().getType();
	        if (pl.isSneaking()) {
	          if (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() != 0.0D)
	          {
	            MetadataValue spilituse = new FixedMetadataValue(this.plugin0, Integer.valueOf(0));
	            pl.setMetadata("spilituse", spilituse);
	            pl.sendMessage(this.touhouraces + ChatColor.WHITE + "霊力ノーマル");
	          }
	          else if (dust_is_ok == Material.SUGAR)
	          {
	            MetadataValue spilituse = new FixedMetadataValue(this.plugin0, Integer.valueOf(5));
	            pl.setMetadata("spilituse", spilituse);
	            pl.sendMessage(this.touhouraces + ChatColor.AQUA + "霊力消費小");
	          }
	          else if (dust_is_ok == Material.SULPHUR)
	          {
	            MetadataValue spilituse = new FixedMetadataValue(this.plugin0, Integer.valueOf(15));
	            pl.setMetadata("spilituse", spilituse);
	            pl.sendMessage(this.touhouraces + ChatColor.DARK_GRAY + "霊力消費大");
	          }
	          else if (dust_is_ok == Material.GLOWSTONE_DUST)
	          {
	            MetadataValue spilituse = new FixedMetadataValue(this.plugin0, Integer.valueOf(-10));
	            pl.setMetadata("spilituse", spilituse);
	            pl.sendMessage(this.touhouraces + ChatColor.YELLOW + "霊力回復中");
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("mazyo"))
	        {
	          Material sword_is_ok = pl.getItemInHand().getType();
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 30.0D) && (pl.isSneaking()) && (
	            (sword_is_ok == Material.WOOD_SWORD) || (sword_is_ok == Material.STONE_SWORD) || (sword_is_ok == Material.IRON_SWORD) || (sword_is_ok == Material.DIAMOND_SWORD) || (sword_is_ok == Material.GOLD_SWORD))) {
	            if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	            }
	            else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	            }
	            else
	            {
	              MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	              pl.setMetadata("casting", casting);
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "剣を構えた！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
	              pl.getWorld().playEffect(pl.getLocation(), Effect.WITCH_MAGIC, 1, 1);
	              getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	              {
	                public void run()
	                {
	                  Player pl = event.getPlayer();
	                  if (pl.getItemInHand().getType() == Material.WOOD_SWORD)
	                  {
	                	  THSkillNNG.magic_dirt(pl, THRPlugin.this.plugin0, event);
	                  }
	                  else if (pl.getItemInHand().getType() == Material.STONE_SWORD)
	                  {
	                	  THSkillNNG.magic_wind(pl, THRPlugin.this.plugin0, event);
	                  }
	                  else
	                  {
	                    float yaw;
	                    if (pl.getItemInHand().getType() == Material.IRON_SWORD)
	                    {
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.RED + "火の魔法を唱えた！");
	                      pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.FIRE, 1.0F, 0.0F);
	                      Location location = pl.getEyeLocation();
	                      float pitch = location.getPitch() / 180.0F * 3.1415927F;
	                      yaw = location.getYaw() / 180.0F * 3.1415927F;
	                      double motX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
	                      double motZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);
	                      double motY = -MathHelper.sin(pitch);
	                      Vector velocity = new Vector(motX, motY, motZ).multiply(2.0D);
	                      Snowball snowball = pl.throwSnowball();
	                      MetadataValue shooter = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getUniqueId().toString());
	                      snowball.setMetadata("mazyo-fireball", shooter);
	                      snowball.setVelocity(velocity);
	                      snowball.setFireTicks(300);
	                      MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                      pl.setMetadata("using-magic", usingmagic);
	                      THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                      {
	                        public void run()
	                        {
	                          Player pl = event.getPlayer();
	                          MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                          pl.setMetadata("using-magic", usingmagic);
	                          pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                        }
	                      }, 10L);
	                    }
	                    else if (pl.getItemInHand().getType() == Material.DIAMOND_SWORD)
	                    {
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "水の魔法を唱えた！");
	                      pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.MAGMACUBE_JUMP, 1.0F, 0.0F);
	                      List<Entity> enemys = pl.getNearbyEntities(8.0D, 8.0D, 8.0D);
	                      enemys.add(pl);
	                      for (Entity enemy : enemys) {
	                        if (((enemy instanceof LivingEntity)) && (!enemy.isDead())) {
	                          if (((LivingEntity)enemy).getHealth() + 20.0D > ((LivingEntity)enemy).getMaxHealth())
	                          {
	                            ((LivingEntity)enemy).setHealth(((LivingEntity)enemy).getMaxHealth());
	                            enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
	                          }
	                          else
	                          {
	                            ((LivingEntity)enemy).setHealth(((LivingEntity)enemy).getHealth() + 20.0D);
	                            enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
	                          }
	                        }
	                      }
	                      MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                      pl.setMetadata("using-magic", usingmagic);
	                      THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                      {
	                        public void run()
	                        {
	                          Player pl = event.getPlayer();
	                          MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                          pl.setMetadata("using-magic", usingmagic);
	                          pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                        }
	                      }, 180L);
	                    }
	                    else
	                    {
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_PURPLE + "雷の魔法を唱えた！");
	                      Entity lightning1 = pl.getWorld().spawnEntity(pl.getLocation().add(7.0D, 0.0D, 0.0D), EntityType.LIGHTNING);
	                      Entity lightning2 = pl.getWorld().spawnEntity(pl.getLocation().add(-7.0D, 0.0D, 0.0D), EntityType.LIGHTNING);
	                      Entity lightning3 = pl.getWorld().spawnEntity(pl.getLocation().add(0.0D, 0.0D, 7.0D), EntityType.LIGHTNING);
	                      Entity lightning4 = pl.getWorld().spawnEntity(pl.getLocation().add(0.0D, 0.0D, -7.0D), EntityType.LIGHTNING);
	                      MetadataValue lightningeffect = new FixedMetadataValue(THRPlugin.this.plugin0, Double.valueOf(15.0D));
	                      lightning1.setMetadata("lightningeffect", lightningeffect);
	                      lightning2.setMetadata("lightningeffect", lightningeffect);
	                      lightning3.setMetadata("lightningeffect", lightningeffect);
	                      lightning4.setMetadata("lightningeffect", lightningeffect);

	                      MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                      pl.setMetadata("using-magic", usingmagic);
	                      THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                      {
	                        public void run()
	                        {
	                          Player pl = event.getPlayer();
	                          MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                          pl.setMetadata("using-magic", usingmagic);
	                          pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                        }
	                      }, 180L);
	                    }
	                  }
	                  MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                  pl.setMetadata("casting", casted);
	                }
	              }, 60L);
	              getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 20.0D));
	              saveConfig();
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	            }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngen")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("mazyo")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houraizin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("gennzinnsin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sibito")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sennnin")))
	        {
	          Material sword_is_ok = pl.getItemInHand().getType();
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 20.0D) && (pl.isSneaking()) &&
	            (sword_is_ok == Material.STICK)) {
	            if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	            }
	            else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	            }
	            else
	            {
	              MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	              pl.setMetadata("casting", casting);
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "棒を構えた！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
	              pl.getWorld().playEffect(pl.getLocation(), Effect.WITCH_MAGIC, 1, 1);
	              getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	              {
	                public void run()
	                {
	                  Player pl = event.getPlayer();
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.YELLOW + "自己治癒を使った！");
	                  pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
	                  if (pl.getHealth() + 10.0D > pl.getMaxHealth())
	                  {
	                    pl.setHealth(pl.getMaxHealth());
	                    pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
	                  }
	                  else
	                  {
	                    pl.setHealth(pl.getHealth() + 1.0D);
	                    pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
	                  }
	                  MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                  pl.setMetadata("using-magic", usingmagic);
	                  THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                  {
	                    public void run()
	                    {
	                      Player pl = event.getPlayer();
	                      MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                      pl.setMetadata("using-magic", usingmagic);
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                    }
	                  }, 20L);
	                  MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                  pl.setMetadata("casting", casted);
	                }
	              }, 40L);
	              getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 15.0D));
	              saveConfig();
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	            }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("youma")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kennyou")))
	        {
	          Material axe_is_ok = pl.getItemInHand().getType();
	          int remMana = 0;
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 20.0D) && (pl.isSneaking()) && (
	            (axe_is_ok == Material.WOOD_AXE) || (axe_is_ok == Material.STONE_AXE) || (axe_is_ok == Material.IRON_AXE) || (axe_is_ok == Material.DIAMOND_AXE) || (axe_is_ok == Material.GOLD_AXE))) {
	            if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	            }
	            else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	            }
	            else
	            {
	              MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	              pl.setMetadata("casting", casting);
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "斧を構えた！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_IDLE, 1.0F, 1.0F);
	              pl.getWorld().playEffect(pl.getLocation(), Effect.FOOTSTEP, 3, 3);
	              getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	              {
	                public void run()
	                {
	                  Player pl = event.getPlayer();
	                  if (pl.getItemInHand().getType() == Material.GOLD_AXE)
	                  {
	                    pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_GREEN + "金の斧で全てを吹き飛ばす！");
	                    pl.getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 2.0F, 0.0F);
	                    pl.getWorld().playSound(pl.getLocation(), Sound.EXPLODE, 2.0F, 0.0F);
	                    pl.getWorld().playEffect(pl.getLocation(), Effect.EXPLOSION_HUGE, 1, 1);
	                    List<Entity> enemys = pl.getNearbyEntities(7.0D, 7.0D, 7.0D);
	                    for (Entity enemy : enemys) {
	                      if ((enemy instanceof LivingEntity))
	                      {
	                        enemy.setVelocity(enemy.getVelocity().add(new Vector(new Double(enemy.getLocation().getX() - pl.getLocation().getX()).doubleValue(), 0.0D, new Double(enemy.getLocation().getZ() - pl.getLocation().getZ()).doubleValue())));
	                        enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.HURT_FLESH, 1.0F, 1.0F);
	                      }
	                    }
	                  }
	                  else
	                  {
	                    float yaw;
	                    if ((THRPlugin.this.getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa")) && (pl.getItemInHand().getType() == Material.STONE_AXE)){
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "石の斧でTNTを投げた！");
	                      pl.getWorld().playSound(pl.getLocation(), Sound.FIRE_IGNITE, 1.0F, 0.0F);
	                      Location location = pl.getEyeLocation();
	                      float pitch = location.getPitch() / 180.0F * 3.1415927F;
	                      yaw = location.getYaw() / 180.0F * 3.1415927F;
	                      double motX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
	                      double motZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);
	                      double motY = -MathHelper.sin(pitch);
	                      Vector velocity = new Vector(motX, motY, motZ).multiply(1.3D);
	                      //TODO 河童のTNTはここへ
	                      TNTPrimed tnt = (TNTPrimed)pl.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
	                      MetadataValue shooter = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getName());
	                      tnt.setMetadata("kappa-tnt", shooter);
	                      tnt.setVelocity(velocity);
	                      tnt.setIsIncendiary(true);
	                      tnt.setFireTicks(20);
	                      tnt.setFuseTicks(20);
	                      NoDamageTick(pl, 15, 30);
	                    }else{
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "斧で地面を叩き上げた！");
	                      pl.getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 2.0F, 0.0F);
	                      pl.getWorld().playEffect(pl.getLocation(), Effect.EXPLOSION_LARGE, 1, 1);
	                      List<Entity> enemys = pl.getNearbyEntities(7.0D, 7.0D, 7.0D);
	                      for (Entity enemy : enemys) {
	                        if ((enemy instanceof LivingEntity))
	                        {
	                          enemy.setVelocity(enemy.getVelocity().add(new Vector(0.0D, 1.5D, 0.0D)));
	                          enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.HURT_FLESH, 1.0F, 0.0F);
	                        }
	                      }
	                    }
	                  }
	                  MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                  pl.setMetadata("casting", casted);
	                }
	              }, 20L);
	              if (THRPlugin.this.getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa")){
	            	  getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 25.0D));
	              }else {
	            	  getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 15.0D));
	              }
	              saveConfig();
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	            }
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki"))
	        {
	          Material pickel_is_ok = pl.getItemInHand().getType();
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 30.0D) && (pl.isSneaking()) &&
	            (pickel_is_ok == Material.STONE_PICKAXE)) {
	            if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	            }
	            else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	            }
	            else
	            {
	              MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	              pl.setMetadata("casting", casting);
	              pl.sendMessage(this.touhouraces + ChatColor.GRAY + "バンプカモフラージュを唱えた！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.BAT_IDLE, 1.0F, 0.0F);
	              pl.getWorld().playEffect(pl.getLocation(), Effect.SMOKE, 1, 1);
	              getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	              {
	                public void run()
	                {
	                  Player pl = event.getPlayer();
	                  MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                  MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                  pl.setMetadata("casting", casted);
	                  MetadataValue batman = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getUniqueId());
	                  pl.setMetadata("batman", batman);
	                  pl.setGameMode(GameMode.SPECTATOR);
	                  pl.getWorld().playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 0.0F);
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.RED + "あなたは蝙蝠になった！");
	                  Entity bat = pl.getWorld().spawnEntity(pl.getEyeLocation(), EntityType.BAT);
	                  MetadataValue invincible = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getUniqueId());
	                  bat.setMetadata("invincible", invincible);
	                  pl.setMetadata("using-magic", usingmagic);
	                }
	              }, 20L);
	              getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 30.0D));
	              saveConfig();
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	            }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yousei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("satori")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kobito")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kibito")))
	        {
	          Material spade_is_ok = pl.getItemInHand().getType();
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 20.0D) && (pl.isSneaking()) && (
	            (spade_is_ok == Material.WOOD_SPADE) || (spade_is_ok == Material.STONE_SPADE) || (spade_is_ok == Material.IRON_SPADE) || (spade_is_ok == Material.DIAMOND_SPADE) || (spade_is_ok == Material.GOLD_SPADE))) {
	            if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	            }
	            else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	            {
	              pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	            }
	            else
	            {
	              MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	              pl.setMetadata("casting", casting);
	              pl.sendMessage(this.touhouraces + ChatColor.YELLOW + "シャベルを構えた！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.CAT_MEOW, 1.0F, 0.0F);
	              pl.getWorld().playEffect(pl.getLocation(), Effect.FOOTSTEP, 1, 0);
	              getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	              {
	                public void run()
	                {
	                  Player pl = event.getPlayer();
	                  if (pl.getItemInHand().getType() == Material.GOLD_SPADE)
	                  {
	                    pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.LIGHT_PURPLE + "金のシャベルの輝きがあたりを惑わす！");
	                    pl.getWorld().playSound(pl.getLocation(), Sound.CAT_PURR, 3.0F, -1.0F);
	                    pl.getWorld().playEffect(pl.getLocation(), Effect.HAPPY_VILLAGER, 1, 1);
	                    List<Entity> enemys = pl.getNearbyEntities(14.0D, 14.0D, 14.0D);
	                    for (Entity enemy : enemys) {
	                      if ((enemy instanceof Player)) {
	                        ((Player)enemy).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 3));
	                      }
	                    }
	                  }
	                  else if ((THRPlugin.this.getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kibito")) && (pl.getItemInHand().getType() == Material.STONE_SPADE))
	                  {
	                    pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_GREEN + "樹人は毒をばらまいた！");
	                    pl.getWorld().playSound(pl.getLocation(), Sound.PIG_DEATH, 3.0F, -1.0F);
	                    pl.getWorld().playEffect(pl.getLocation(), Effect.VOID_FOG, 1, 1);
	                    List<Entity> enemys = pl.getNearbyEntities(14.0D, 14.0D, 14.0D);
	                    for (Entity enemy : enemys) {
	                      if ((enemy instanceof LivingEntity)) {
	                        ((LivingEntity)enemy).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2));
	                      }
	                    }
	                    MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                    pl.setMetadata("using-magic", usingmagic);
	                    THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                    {
	                      public void run()
	                      {
	                        Player pl = event.getPlayer();
	                        MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                        pl.setMetadata("using-magic", usingmagic);
	                        pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                      }
	                    }, 100L);
	                  }
	                  else
	                  {
	                    pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_PURPLE + "シャベルは向いた方向へ転移の門を開いた！");
	                    float pitch = pl.getLocation().getPitch();
	                    float yaw = pl.getLocation().getYaw();
	                    Location warploc = new Location(pl.getWorld(), pl.getLocation().getX() + pl.getLocation().getDirection().getX() * 10.0D, pl.getLocation().getY() + pl.getLocation().getDirection().getY() * 10.0D, pl.getLocation().getZ() + pl.getLocation().getDirection().getZ() * 10.0D);
	                    if (pl.getWorld().getBlockAt(warploc).getType() != Material.AIR)
	                    {
	                      pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_HIT, 2.0F, 0.0F);
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.RED + "しかし十分な出口空間が無かったため入れなかった！");
	                    }
	                    else
	                    {
	                      pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0F, 0.0F);
	                      pl.getWorld().playEffect(pl.getLocation(), Effect.COLOURED_DUST, 1, 5);
	                      warploc.setPitch(pitch);
	                      warploc.setYaw(yaw);
	                      pl.teleport(warploc);
	                    }
	                  }
	                  MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                  pl.setMetadata("casting", casted);
	                }
	              }, 20L);
	              getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 25.0D));
	              saveConfig();
	              pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	            }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("seirei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("onnryou")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("hannrei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sourei"))) {
	          if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 40.0D) && (pl.isSneaking()))
	          {
	            Material hoe_is_ok = pl.getItemInHand().getType();
	            if ((hoe_is_ok == Material.WOOD_HOE) || (hoe_is_ok == Material.STONE_HOE) || (hoe_is_ok == Material.IRON_HOE) || (hoe_is_ok == Material.DIAMOND_HOE) || (hoe_is_ok == Material.GOLD_HOE)) {
	              if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	              {
	                pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	              }
	              else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	              {
	                pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	              }
	              else if (pl.getItemInHand().getType() == Material.GOLD_HOE)
	              {
	                MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	                pl.setMetadata("casting", casting);
	                pl.sendMessage(this.touhouraces + ChatColor.LIGHT_PURPLE + "守護霊を呼び出し、自身を保護する！");
	                pl.getWorld().playSound(pl.getLocation(), Sound.ANVIL_USE, 2.0F, 0.0F);
	                pl.getWorld().playEffect(pl.getLocation(), Effect.RECORD_PLAY, 1, 1);
	                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	                {
	                  public void run()
	                  {
	                    Player pl = event.getPlayer();
	                    MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                    pl.setMetadata("casting", casted);
	                    MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                    pl.setMetadata("using-magic", usingmagic);
	                    double type = Math.random();
	                    if (type <= 8.0D)
	                    {
	                      int n = 0;
	                      while (n < 3)
	                      {
	                        Entity snowman = pl.getWorld().spawnEntity(pl.getLocation(), EntityType.SNOWMAN);
	                        MetadataValue syugoreisnow = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                        snowman.setMetadata("syugoreisnow", syugoreisnow);
	                        MetadataValue syugoreitarget = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getName());
	                        snowman.setMetadata("syugoreitarget", syugoreitarget);
	                        n++;
	                      }
	                      pl.getWorld().playSound(pl.getLocation(), Sound.IRONGOLEM_HIT, 2.0F, 1.0F);
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.AQUA + "雪の霊だ！");
	                    }
	                    else
	                    {
	                      int n = 0;
	                      while (n < 1)
	                      {
	                        Entity snowman = pl.getWorld().spawnEntity(pl.getLocation(), EntityType.IRON_GOLEM);
	                        MetadataValue syugoreiiron = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                        snowman.setMetadata("syugoreiiron", syugoreiiron);
	                        MetadataValue syugoreitarget = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getName());
	                        snowman.setMetadata("syugoreitarget", syugoreitarget);
	                        n++;
	                      }
	                      pl.getWorld().playSound(pl.getLocation(), Sound.IRONGOLEM_HIT, 2.0F, -1.0F);
	                      pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.GOLD + "岩の霊だ！");
	                    }
	                    THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                    {
	                      public void run()
	                      {
	                        Player pl = event.getPlayer();
	                        MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                        pl.setMetadata("using-magic", usingmagic);
	                        pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
	                      }
	                    }, 600L);
	                  }
	                }, 20L);
	                getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 40.0D));
	                saveConfig();
	                pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	              }
	              else if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("hannrei")) && (hoe_is_ok == Material.STONE_HOE))
	              {
	                MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	                pl.setMetadata("casting", casting);
	                pl.sendMessage(this.touhouraces + ChatColor.DARK_PURPLE + "半霊を詠唱した！");
	                pl.getWorld().playSound(pl.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
	                pl.getWorld().playEffect(pl.getLocation(), Effect.WITCH_MAGIC, 1, 1);
	                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	                {
	                  public void run()
	                  {
	                    Player pl = event.getPlayer();
	                    pl.getWorld().playSound(pl.getLocation(), Sound.DIG_SAND, 2.0F, 2.0F);
	                    pl.getWorld().playEffect(pl.getLocation(), Effect.SNOW_SHOVEL, 1, 1);
	                    Location location = pl.getEyeLocation();
	                    float pitch = location.getPitch() / 180.0F * 3.1415927F;
	                    float yaw = location.getYaw() / 180.0F * 3.1415927F;
	                    double motX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
	                    double motZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);
	                    double motY = -MathHelper.sin(pitch);
	                    Vector velocity = new Vector(motX, motY, motZ).multiply(2.0D);
	                    Snowball snowball = pl.throwSnowball();
	                    MetadataValue shooter = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getUniqueId().toString());
	                    snowball.setMetadata("hannrei-curseball", shooter);
	                    snowball.setVelocity(velocity);
	                    pl.removeMetadata("casting", THRPlugin.this.plugin0);
	                    MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                    pl.setMetadata("casting", casted);
	                  }
	                }, 30L);
	                getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 30.0D));
	                saveConfig();
	                pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	              }
	              else if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sourei")) && (hoe_is_ok == Material.IRON_HOE))
	              {
	                MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	                pl.setMetadata("casting", casting);
	                pl.sendMessage(this.touhouraces + ChatColor.UNDERLINE + ChatColor.BOLD + "レッツッオーケストラ！！");
	                pl.getWorld().playSound(pl.getLocation(), Sound.NOTE_BASS_DRUM, 1.0F, 0.0F);
	                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	                {
	                  public void run()
	                  {
	                    Player pl = event.getPlayer();
	                    List<Entity> enemys = pl.getNearbyEntities(16.0D, 16.0D, 16.0D);
	                    double rand = Math.random();
	                    if (rand >= 0.8D)
	                    {
	                      pl.getWorld().playSound(pl.getLocation(), Sound.NOTE_BASS_GUITAR, 10.0F, -2.0F);
	                      for (Entity enemy : enemys) {
	                        if ((enemy instanceof Player))
	                        {
	                          ((Player)enemy).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 5));
	                          ((Player)enemy).sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_BLUE + "鬱だ・・・");
	                        }
	                      }
	                    }
	                    else if (rand >= 0.4D)
	                    {
	                      pl.getWorld().playSound(pl.getLocation(), Sound.NOTE_SNARE_DRUM, 10.0F, 1.0F);
	                      for (Entity enemy : enemys) {
	                        if ((enemy instanceof Player))
	                        {
	                          ((Player)enemy).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 2));
	                          ((Player)enemy).sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_RED + "躁だ★");
	                        }
	                      }
	                    }
	                    else
	                    {
	                      pl.getWorld().playSound(pl.getLocation(), Sound.NOTE_PIANO, 10.0F, 0.0F);
	                      for (Entity enemy : enemys) {
	                        if ((enemy instanceof Player))
	                        {
	                          ((Player)enemy).sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "騒音だ！！");
	                          if (((Player)enemy).getHealth() - 15.0D >= 0.0D) {
	                            ((Player)enemy).setHealth(((Player)enemy).getHealth() - 15.0D);
	                          } else {
	                            ((Player)enemy).setHealth(0.0D);
	                          }
	                        }
	                      }
	                    }
	                    pl.removeMetadata("casting", THRPlugin.this.plugin0);
	                    MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                    pl.setMetadata("casting", casted);
	                  }
	                }, 60L);
	                getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 40.0D));
	                saveConfig();
	                pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	              }
	              else
	              {
	                MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	                pl.setMetadata("casting", casting);
	                pl.sendMessage(this.touhouraces + ChatColor.YELLOW + "光弾を詠唱した！");
	                pl.getWorld().playSound(pl.getLocation(), Sound.GHAST_SCREAM2, 1.0F, 1.0F);
	                pl.getWorld().playEffect(pl.getLocation(), Effect.WITCH_MAGIC, 1, 1);
	                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	                {
	                  public void run()
	                  {
	                    Player pl = event.getPlayer();
	                    pl.getWorld().playSound(pl.getLocation(), Sound.DIG_SNOW, 2.0F, 2.0F);
	                    pl.getWorld().playEffect(pl.getLocation(), Effect.SNOW_SHOVEL, 1, 1);
	                    Location location = pl.getEyeLocation();
	                    int n = 0;
	                    while (n < 8)
	                    {
	                      float pitch = location.getPitch() / 180.0F * 3.1415927F;
	                      float yaw = location.getYaw() / 180.0F * 3.1415927F + n * 45;
	                      double motX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
	                      double motZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);
	                      double motY = -MathHelper.sin(pitch);
	                      Vector velocity = new Vector(motX, motY, motZ).multiply(2.0D);
	                      Snowball snowball = pl.throwSnowball();
	                      MetadataValue shooter = new FixedMetadataValue(THRPlugin.this.plugin0, pl.getUniqueId().toString());
	                      snowball.setMetadata("seirei-lightball", shooter);
	                      snowball.setShooter(pl);
	                      snowball.setVelocity(velocity);
	                      n++;
	                    }
	                    pl.removeMetadata("casting", THRPlugin.this.plugin0);
	                    MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                    pl.setMetadata("casting", casted);
	                  }
	                }, 15L);
	                getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 4.0D));
	                saveConfig();
	                pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	              }
	            }
	          }
	        }
	      }
	    }
	  }

	  @EventHandler
	  public void onSkillDamage(EntityDamageEvent e){
		  if(e.getEntity() instanceof Player){
			  Player p = (Player) e.getEntity();
		        if( ((EntityDamageByEntityEvent) e).getDamager() instanceof Snowball ){
		        	Entity damagerentity = ((EntityDamageByEntityEvent) e).getDamager();
		            Snowball snowball = (Snowball)damagerentity;
		            if (snowball.hasMetadata("seirei-lightball")) {
		            	//精霊 光弾
		            	e.setDamage(6.0D);
		            }else if (snowball.hasMetadata("hannrei-curseball")) {
		                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 3));
		                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 150, 3));
		                if (((e.getEntity() instanceof Player)) && (Bukkit.getPlayer(UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString())) != null))
		                {
		                  if (getConfig().getInt("user." + p.getUniqueId() + ".spilit") >= 30)
		                  {
		                    getConfig().set("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit", Double.valueOf(getConfig().getInt("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit") + 30.0D));
		                    getConfig().set("user." + p.getUniqueId() + ".spilit", Double.valueOf(getConfig().getInt("user." + p.getUniqueId() + ".spilit") - 30.0D));
		                    if (getConfig().getInt("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit") > 100) {
		                      getConfig().set("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit", Double.valueOf(100.0D));
		                    }
		                  }
		                  else
		                  {
		                    getConfig().set("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit", Integer.valueOf(getConfig().getInt("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit") + getConfig().getInt("user." + p.getUniqueId() + ".spilit")));
		                    getConfig().set("user." + p.getUniqueId() + ".spilit", Integer.valueOf(0));
		                    if (getConfig().getInt("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit") > 100) {
		                      getConfig().set("user." + UUID.fromString(((MetadataValue)((EntityDamageByEntityEvent) e).getDamager().getMetadata("hannrei-curseball").get(0)).asString()) + ".spilit", Double.valueOf(100.0D));
		                    }
		                  }
		                  p.sendMessage(this.touhouraces + ChatColor.DARK_PURPLE + "霊力を吸い取られた！");
		                }
		            }
		        }
		  }
	  }

	  @EventHandler
	  public void onEntityExplode(EntityExplodeEvent e){
		  Entity ent = e.getEntity();
		  if(ent.getType().equals(EntityType.PRIMED_TNT)){
			  if(ent.hasMetadata("kappa-tnt")){
				  TNTPrimed tnt = (TNTPrimed) e.getEntity();
				  World world = e.getLocation().getWorld();
				  Location loc = e.getLocation();
				  e.setCancelled(true);
				  world.createExplosion(loc, 0.0F);
				  Player shooter = getServer().getPlayer(ent.getMetadata("kappa-tnt").get(0).asString());
				  for (int shot = 200; shot > 0; shot--){
					  int x = new Random().nextInt(90) - 45;
					  int y = new Random().nextInt(70) - 45;
					  int z = new Random().nextInt(90) - 45;
					  Snowball snowball = world.spawn(loc, Snowball.class);
	                  snowball.setMetadata("kappa-yukidama", new FixedMetadataValue(THRPlugin.this.plugin0, true));
	                  snowball.setShooter(shooter);
	                  Vector vectory = new Vector(x, y, z);
	                  snowball.setVelocity(vectory);
				  }
					Bukkit.getScheduler().runTaskLater(this, new Runnable() {
						@Override
						public void run() {
							  world.createExplosion(loc, 0.0F);
							  for (int shot = 200; shot > 0; shot--){
								  int x = new Random().nextInt(90) - 45;
								  int y = new Random().nextInt(70) - 45;
								  int z = new Random().nextInt(90) - 45;
								  Snowball snowball = world.spawn(loc, Snowball.class);
				                  snowball.setMetadata("kappa-yukidama", new FixedMetadataValue(THRPlugin.this.plugin0, true));
				                  snowball.setShooter(shooter);
				                  Vector vectory = new Vector(x, y, z);
				                  snowball.setVelocity(vectory);
							  }
						}
					}, 5);
					Bukkit.getScheduler().runTaskLater(this, new Runnable() {
						@Override
						public void run() {
							  world.createExplosion(loc, 0.0F);
							  for (int shot = 200; shot > 0; shot--){
								  int x = new Random().nextInt(90) - 45;
								  int y = new Random().nextInt(70) - 45;
								  int z = new Random().nextInt(90) - 45;
								  Snowball snowball = world.spawn(loc, Snowball.class);
				                  snowball.setMetadata("kappa-yukidama", new FixedMetadataValue(THRPlugin.this.plugin0, true));
				                  snowball.setShooter(shooter);
				                  Vector vectory = new Vector(x, y, z);
				                  snowball.setVelocity(vectory);
							  }
						}
					}, 10);
			  }
		  }
	  }

	  @EventHandler
	  public void SnowballDamage(EntityDamageByEntityEvent e){
		  if(e.getEntity() instanceof Player){
			  if(e.getDamager().getType() == EntityType.PRIMED_TNT){
				  if(e.getDamager().hasMetadata("kappa-tnt")){
					  e.setCancelled(true);
				  }
			  }else if(e.getDamager().getType() == EntityType.SNOWBALL){
				  Snowball sb = (Snowball) e.getDamager();
				  if((Player)e.getEntity() == sb.getShooter()){
					  e.setCancelled(true);
				  }else {
					  if(sb.hasMetadata("kappa-yukidama")){
						  e.setDamage(20);
					  }
				  }
			  }
		  }
	  }

	  public void NoDamageTick(Player p,int wait, int tick){
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run() {
					p.setNoDamageTicks(tick);
				}
			}, wait);
	  }


	  @EventHandler(priority=EventPriority.LOWEST)
	  public void on_click_MOB(final PlayerInteractEntityEvent event)
	  {
	    Player pl = event.getPlayer();
	    if (event.getRightClicked().getType() == EntityType.VILLAGER)
	    {
	      if ((!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngen")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("mazyo")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houraizin")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("gennzinnsin")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("misou")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sibito")) && (!getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sennninn")))
	      {
	        pl.sendMessage(this.touhouraces + ChatColor.GRAY + "このニンゲンは何を話しているんだろう・・・");
	        pl.closeInventory();
	        event.setCancelled(true);
	      }
	    }
	    else if ((!pl.hasMetadata("ignoreskill")) && (pl.hasPermission("thr.skill"))) {
	      if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki"))
	      {
	        Material pickel_is_ok = pl.getItemInHand().getType();
	        if ((getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") >= 30.0D) && (pl.isSneaking()) && (
	          (pickel_is_ok == Material.WOOD_PICKAXE) || (pickel_is_ok == Material.STONE_PICKAXE) || (pickel_is_ok == Material.IRON_PICKAXE) || (pickel_is_ok == Material.DIAMOND_PICKAXE) || (pickel_is_ok == Material.GOLD_PICKAXE))) {
	          if (((MetadataValue)pl.getMetadata("casting").get(0)).asBoolean())
	          {
	            pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を詠唱中です！");
	          }
	          else if (((MetadataValue)pl.getMetadata("using-magic").get(0)).asBoolean())
	          {
	            pl.sendMessage(this.touhouraces + ChatColor.RED + "他の魔法を使用中です！");
	          }
	          else
	          {
	            MetadataValue casting = new FixedMetadataValue(this.plugin0, Boolean.valueOf(true));
	            pl.setMetadata("casting", casting);
	            pl.sendMessage(this.touhouraces + ChatColor.DARK_RED + "牙を構えた！");
	            pl.getWorld().playSound(pl.getLocation(), Sound.SPIDER_IDLE, 2.0F, 1.0F);
	            pl.getWorld().playEffect(pl.getLocation(), Effect.LAVADRIP, 2, 1);
	            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              public void run()
	              {
	                Player pl = event.getPlayer();
	                Entity target = event.getRightClicked();
	                if (pl.getItemInHand().getType() == Material.GOLD_PICKAXE)
	                {
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_GRAY + "きゅっとして" + ChatColor.BOLD + ChatColor.YELLOW + "どかーん!！！");
	                  target.getWorld().playSound(target.getLocation(), Sound.NOTE_PIANO, 3.0F, 3.0F);
	                  target.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_GRAY + "きゅっとして" + ChatColor.BOLD + ChatColor.YELLOW + "どかーん!！！");
	                  MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(true));
	                  pl.setMetadata("using-magic", usingmagic);
	                  THRPlugin.this.plugin0.getServer().getScheduler().scheduleSyncDelayedTask(THRPlugin.this.plugin0, new Runnable()
	                  {
	                    public void run()
	                    {
	                      Player pl = event.getPlayer();
	                      Entity target = event.getRightClicked();
	                      if (pl.getLocation().distanceSquared(event.getRightClicked().getLocation()) >= 150.0D)
	                      {
	                        pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "しかし逃げられてしまった！");
	                        target.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "逃げ切った！");
	                      }
	                      else
	                      {
	                        target.getWorld().playSound(target.getLocation(), Sound.EXPLODE, 2.0F, 1.0F);
	                        target.getWorld().playEffect(target.getLocation(), Effect.EXPLOSION_LARGE, 1, 1);
	                        if ((target instanceof LivingEntity)) {
	                          if (((LivingEntity)target).getHealth() / 4.0D >= 0.0D) {
	                            ((LivingEntity)target).setHealth(((LivingEntity)target).getHealth() / 4.0D);
	                          } else {
	                            ((LivingEntity)target).setHealth(0.0D);
	                          }
	                        }
	                      }
	                      MetadataValue usingmagic = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                      pl.setMetadata("using-magic", usingmagic);
	                    }
	                  }, 120L);
	                  THRPlugin.this.getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(THRPlugin.this.getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 30.0D));
	                  THRPlugin.this.saveConfig();
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + THRPlugin.this.getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	                }
	                else if (pl.getLocation().distanceSquared(target.getLocation()) >= 80.0D)
	                {
	                  pl.getWorld().playSound(pl.getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.BLUE + "しかし逃げられてしまった！");
	                }
	                else if ((target.getType() != EntityType.VILLAGER) && ((target instanceof LivingEntity)))
	                {
	                  pl.sendMessage(THRPlugin.this.touhouraces + ChatColor.DARK_RED + "あなたは吸血した！");
	                  target.getWorld().playSound(pl.getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
	                  target.getWorld().playEffect(pl.getLocation(), Effect.TILE_BREAK, 1, 152);
	                  if (((LivingEntity)target).getHealth() - 30.0D >= 0.0D) {
	                    ((LivingEntity)target).setHealth(((LivingEntity)target).getHealth() - 30.0D);
	                  } else {
	                    ((LivingEntity)target).setHealth(0.0D);
	                  }
	                  if (pl.getHealth() > pl.getMaxHealth() - 30.0D) {
	                    pl.setHealth(pl.getMaxHealth());
	                  } else {
	                    pl.setHealth(15.0D + pl.getHealth());
	                  }
	                }
	                MetadataValue casted = new FixedMetadataValue(THRPlugin.this.plugin0, Boolean.valueOf(false));
	                pl.setMetadata("casting", casted);
	              }
	            }, 40L);
	            getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 20.0D));
	            saveConfig();
	            pl.sendMessage(this.touhouraces + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + getConfig().getDouble(new StringBuilder("user.").append(pl.getUniqueId()).append(".spilit").toString()));
	          }
	        }
	      }
	    }
	  }

	  @EventHandler(priority=EventPriority.HIGH)
	  public void on_simpleclans_crackshot_hook(WeaponDamageEntityEvent event)
	  {
	    if (((event.getPlayer() instanceof Player)) && (event.getDamager() != null)) {
	      if ((event.getVictim() instanceof Player))
	      {
	        UUID shooter_owner_id = event.getPlayer().getUniqueId();
	        UUID victim_id = ((Player)event.getVictim()).getUniqueId();
	        if ((this.sc.getClanManager().getClanByPlayerUniqueId(shooter_owner_id) != null) && (this.sc.getClanManager().getClanByPlayerUniqueId(victim_id) != null)) {
	          if (this.sc.getClanManager().getClanByPlayerUniqueId(shooter_owner_id) == this.sc.getClanManager().getClanByPlayerUniqueId(victim_id)) {
	            event.setCancelled(true);
	          } else if (this.sc.getClanManager().getClanPlayer(shooter_owner_id).isAlly(this.sc.getClanManager().getClanPlayer(victim_id).toPlayer())) {
	            event.setCancelled(true);
	          }
	        }
	      }
	    }
	    EntityDamageByEntityEvent weaponattack = new EntityDamageByEntityEvent(event.getPlayer(), event.getVictim(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, event.getDamage());
	    on_attack_entity(weaponattack);
	  }

	  @EventHandler(priority=EventPriority.LOW)
	  public void on_attack_entity(EntityDamageByEntityEvent event)
	  {
	    if (((event.getEntity() instanceof Bat)) && (event.getEntity().hasMetadata("invincible")))
	    {
	      event.setDamage(0.0D);
	      event.getDamager().sendMessage(this.touhouraces + ChatColor.RED + "化けているのは分かったから待ちましょう！");
	      event.setCancelled(true);
	    }
	    if (((event.getEntity() instanceof Snowman)) && (event.getEntity().hasMetadata("syugoreisnow"))) {
	      event.setDamage(event.getDamage() / 4.0D);
	    }
	    if (((event.getEntity() instanceof IronGolem)) && (event.getEntity().hasMetadata("syugoreiiron"))) {
	      event.setDamage(event.getDamage() / 4.0D);
	    }
	    if (((event.getEntity() instanceof Wolf)) && (event.getEntity().hasMetadata("tamedwolf"))) {
	      event.setDamage(event.getDamage() / 3.0D);
	    }
	    if (((event.getEntity() instanceof Ocelot)) && (event.getEntity().hasMetadata("tamedcat"))) {
	      event.setDamage(event.getDamage() / 2.0D);
	    }
	    if ((event.getDamager() instanceof Player))
	    {
	      Player pl = (Player)event.getDamager();
	      if (pl.hasPermission("thr.skill"))
	      {
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("akuma")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("oni"))) {
	          if ((pl.getEyeLocation().getBlock().getLightLevel() <= 8) && (event.getDamage() > 1.0D))
	          {
	            event.setDamage(event.getDamage() + 1.0D);
	            event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.STEP_SOUND, 152);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki")) {
	          if ((pl.getEyeLocation().getBlock().getLightLevel() <= 4) && (event.getDamage() > 0.0D))
	          {
	            event.setDamage(event.getDamage() + 2.0D);
	            event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.STEP_SOUND, 152);
	          }
	          else if ((pl.getEyeLocation().getBlock().getLightLevel() >= 14) && (event.getDamage() > 4.0D))
	          {
	            event.setDamage(event.getDamage() - 2.0D);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("oni")) {
	          if ((pl.getLocation().distanceSquared(event.getEntity().getLocation()) <= 8.0D) && (event.getDamage() > 0.0D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 25.0D) && (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() > 0.0D))
	          {
	            event.setDamage(event.getDamage() + 2.0D);
	            event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("youzyuu")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("siki")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("zyuuzin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngyo"))) {
	          if ((pl.getLocation().distanceSquared(event.getEntity().getLocation()) >= 10.0D) && (event.getDamage() > 0.0D))
	          {
	        	  //TODO 妖獣・獣人・式 ダメージ量調整 ポーションエフェクト
	            event.setDamage(event.getDamage() + 5.0D);
	            event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.POTION_BREAK, 2);
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kami")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houzyousin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("zyuuzin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yakusin"))) {
	          if ((event.getDamage() <= 9.0D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 10.0D) && (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() > 0.0D))
	          {
	            getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 10.0D));
	            event.setDamage(event.getDamage() + 2.0D);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houzyousin")) {
	          if ((Math.random() >= 0.8D) && ((event.getEntity() instanceof Player)) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 10.0D) && (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() > 0.0D))
	          {
	            ((Player)event.getEntity()).setFoodLevel(((Player)event.getEntity()).getFoodLevel() - 2);
	            event.getEntity().sendMessage(this.touhouraces + ChatColor.GOLD + pl.getName() + "はおいしい芋を見せつけてきた！");
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sibito")) {
	          if (pl.getHealth() <= 20.0D)
	          {
	            event.setDamage(event.getDamage() + 3.0D);
	            event.getDamager().getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_PIG_HURT, 1.0F, 1.0F);
	            event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.TILE_BREAK, 49);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("gennzinnsin")) {
	          if ((Math.random() > 0.6D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 5.0D))
	          {
	            getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 5.0D));
	            event.setDamage(event.getDamage() + 5.0D);
	            pl.getWorld().playSound(pl.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
	          }
	        }
	        if (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() < 0.0D)
	        {
	          event.setDamage(event.getDamage() / 2.0D);
	          if (pl.isSneaking()) {
	            pl.sendMessage(this.touhouraces + ChatColor.RED + pl.getName() + "貴方は霊力再生モードの為本気を出せません！");
	          }
	        }
	      }
	    }
	    if ((event.getEntity() instanceof Player))
	    {
	      Player pl = (Player)event.getEntity();
	      if (pl.hasPermission("thr.skill"))
	      {
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("satori")) {
	          if ((event.getDamage() >= pl.getHealth()) && ((event.getDamager() instanceof Player)) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 50))
	          {
	            getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 50.0D));
	            pl.sendMessage(event.getDamager().getName() + ":体力:" + ((Player)event.getDamager()).getHealth());
	            pl.sendMessage(event.getDamager().getName() + ":座標:" + event.getDamager().getLocation().getBlockX() + "," + event.getDamager().getLocation().getBlockY() + "," + event.getDamager().getLocation().getBlockZ());
	            pl.sendMessage("覚りました・・・覚えてなさい・・・");
	            String satorin0 = event.getDamager().getName();
	            MetadataValue satorin00 = new FixedMetadataValue(this.plugin0, satorin0);
	            pl.setMetadata("satorin0", satorin00);
	          }
	        }
	        if (getConfig().getString("user." + event.getEntity().getUniqueId() + ".race").toString().contains("yakusin"))
	        {
	          Entity killer = event.getDamager();
	          if (((killer instanceof Player)) && (event.getDamage() >= pl.getHealth()) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 20))
	          {
	            getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 20.0D));
	            Player killplayer = (Player)killer;
	            if (!killplayer.isDead())
	            {
	              killplayer.sendMessage(this.touhouraces + ChatColor.RED + "あなたは厄神の祟りを受けた！！");
	              killplayer.damage(20.0D);
	            }
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("onnryou")) {
	          if (event.getDamage() >= pl.getHealth())
	          {
	            double rand = Math.random();
	            if ((rand > 0.6D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 40))
	            {
	              getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 40.0D));
	              pl.setHealth(50.0D);
	              pl.sendMessage(this.touhouraces + ChatColor.DARK_RED + "消えたくない・・・っ");
	              if ((event.getDamager() instanceof Player))
	              {
	                Player dpl = (Player)event.getDamager();
	                dpl.sendMessage(this.touhouraces + ChatColor.DARK_RED + "消えたくない・・・っ");
	                dpl.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 4));
	              }
	              pl.getWorld().playSound(pl.getLocation(), Sound.GHAST_CHARGE, 2.0F, 2.0F);
	              event.setCancelled(true);
	            }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("akuma")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("oni"))) {
	          if ((event.getDamage() >= 10.0D) && (Math.random() >= 0.6D) && ((event.getDamager() instanceof LivingEntity)) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 10))
	          {
	            ((LivingEntity)event.getDamager()).damage(5.0D);
	            if ((event.getDamager() instanceof Player)) {
	              getConfig().set("user." + pl.getUniqueId() + ".spilit", Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 10.0D));
	            }
	            Player dpl = (Player)event.getDamager();
	            dpl.playSound(dpl.getLocation(), Sound.HURT_FLESH, 1.0F, 0.0F);
	            dpl.sendMessage(this.touhouraces + ChatColor.DARK_RED + "貴方は反撃を喰らった！！");
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yousei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kibito")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("satori")))
	        {
	          double ran = Math.random();
	          if (ran >= 0.9D)
	          {
	            pl.getWorld().playSound(pl.getLocation(), Sound.CAT_HISS, 1.0F, 2.0F);
	            pl.sendMessage(ChatColor.LIGHT_PURPLE + "グレイズ！");
	            event.setCancelled(true);
	          }
	          else if (event.getDamage() > 3.0D)
	          {
	            event.setDamage(event.getDamage() + 1.0D);
	          }
	        }
	        else if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kobito"))
	        {
	          double ran = Math.random();
	          if (ran >= 0.7D)
	          {
	            pl.getWorld().playSound(pl.getLocation(), Sound.CAT_HISS, 1.0F, 2.0F);
	            pl.sendMessage(this.touhouraces + ChatColor.LIGHT_PURPLE + "超グレイズ！");
	            event.setCancelled(true);
	          }
	          else if ((event.getDamage() > 1.0D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 20))
	          {
	            event.setDamage(event.getDamage() + 2.0D);
	          }
	          else if (event.getDamage() > 1.0D)
	          {
	            event.setDamage(event.getDamage() + 4.0D);
	          }
	        }
	      }
	      else{
	        if (event.getDamager().hasMetadata("lightningeffect")) {
	          event.setDamage(((MetadataValue)event.getDamager().getMetadata("lightningeffect").get(0)).asDouble());
	        } else if (event.getDamager().hasMetadata("mazyo-fireball"))
	        {
	          if (((MetadataValue)event.getDamager().getMetadata("mazyo-fireball").get(0)).asString() != pl.getUniqueId().toString())
	          {
	            event.setDamage(10.0D);
	            event.getEntity().setFireTicks(200);
	          }
	        }
	      }
	    }
	  }

	  @EventHandler
	  public void on_every_damaged(EntityDamageEvent event)
	  {
	    if ((event.getEntityType() == EntityType.SNOWMAN) && (event.getEntity().hasMetadata("syugoreisnow"))) {
	      event.setDamage(0.0D);
	    } else if ((event.getEntityType() == EntityType.IRON_GOLEM) && (event.getEntity().hasMetadata("syugoreiiron"))) {
	      event.setDamage(0.0D);
	    }
	    if ((event.getEntity() instanceof Player))
	    {
	      Player pl = (Player)event.getEntity();
	      if (pl.hasPermission("thr.skill"))
	      {
	        if (pl.hasPotionEffect(PotionEffectType.INVISIBILITY))
	        {
	          pl.removePotionEffect(PotionEffectType.INVISIBILITY);
	          pl.playSound(pl.getLocation(), Sound.WOLF_HURT, 1.0F, -1.0F);
	          pl.sendMessage(this.touhouraces + ChatColor.RED + "1.8のバグで名前丸見えなのよね・・・");
	        }
	        if (((pl.hasPermission("thr.skill")) && getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("youma") || getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa") || getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu") || getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kennyou"))) {
	            if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa") && event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
	              event.setCancelled(true);
	            }
	            if(event.getCause() == EntityDamageEvent.DamageCause.FALL && !getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu")){
	            	event.setDamage(event.getDamage() / 2.0D);
	            }
	          }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yousei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("satori")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kobito")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kibito"))) {
	          if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
	            event.setDamage(event.getDamage() / 3.0D);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("tenngu")) {
	          if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
	        	  if(pl.isSneaking()){
	                  double reverse = Math.random();
	                  if (reverse > 0.5D){
	            		  event.setDamage(event.getDamage() / 15.0D);
	                  }else {
	            		  event.setDamage(event.getDamage() / 10.0D);
	                  }
	        	  }else {
	        		  event.setDamage(event.getDamage() / 8.0D);
	        	  }
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("akuma")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("oni"))) {
	          if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
	            event.setCancelled(true);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kyuuketuki")) {
	          if ((event.getCause() == EntityDamageEvent.DamageCause.FIRE) || (event.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
	            event.setCancelled(true);
	          }
	        }
	        if (((pl.hasPermission("thr.skill")) && (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("ninngyo"))) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kappa"))) {
	          if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
	            event.setCancelled(true);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houzyousin")) {
	          if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION) {
	            event.setCancelled(true);
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("seirei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("onnryou")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("hannrei")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("sourei"))) {
	          if ((pl.isSneaking()) && (event.getDamage() > 3.0D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 5.0D))
	          {
	            getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 5.0D));
	            event.setDamage(event.getDamage() - 3.0D);
	          }
	        }
	        if ((getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("kami")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houzyousin")) || (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("yakusin"))) {
	          if ((event.getDamage() > 1.0D) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 10.0D) && (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() > 0.0D)) {
	            event.setDamage(event.getDamage() - 1.0D);
	          }
	        }
	        if (getConfig().getString("user." + pl.getUniqueId() + ".race").toString().contains("houraizin")) {
	          if ((event.getDamage() >= pl.getHealth()) && (getConfig().getInt("user." + pl.getUniqueId() + ".spilit") > 30.0D))
	          {
	            double reverse = Math.random();
	            if (reverse > 0.6D)
	            {
	              getConfig().set(getConfig().getString("user." + pl.getUniqueId() + ".spilit"), Double.valueOf(getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 30.0D));
	              pl.setHealth(pl.getMaxHealth());
	              pl.sendMessage(this.touhouraces + ChatColor.AQUA + "貴方は不死の力を使い蘇った！！");
	              pl.getWorld().playSound(pl.getLocation(), Sound.BLAZE_BREATH, 1.0F, -1.0F);
	              event.setDamage(0.0D);
	            }
	          }
	        }
	        if (((MetadataValue)pl.getMetadata("spilituse").get(0)).asDouble() < 0.0D)
	        {
	          event.setDamage(event.getDamage() * 2.0D);
	          if (pl.isSneaking()) {
	            pl.sendMessage(this.touhouraces + ChatColor.RED + pl.getName() + "貴方は霊力再生モードの為非常に柔いです！");
	          }
	        }
	      }
	    }
	  }
}
