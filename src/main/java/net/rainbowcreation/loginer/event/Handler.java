package net.rainbowcreation.loginer.event;

import net.minecraftforge.fml.common.Loader;
import net.rainbowcreation.api.compatibles.lobbyspawn;
import net.rainbowcreation.loginer.Main;
import net.rainbowcreation.loginer.config.AuthModConfig;
import net.rainbowcreation.loginer.model.PlayerDescriptor;
import net.rainbowcreation.loginer.model.PlayerPos;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class Handler {
  private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
  
  private static final Map<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
  
  private static final Map<EntityPlayer, Boolean> logged = new HashMap<>();
  
  private static final String WELCOME = AuthModConfig.i18n.welcome;
  
  private static final String WAKE_UP = String.format(AuthModConfig.i18n.delay, new Object[] { Integer.toString(AuthModConfig.delay) });
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
    EntityPlayer entity = event.player;
    if (Loader.isModLoaded("rbclobbyspawn")) { //actually it modid
      if (Main.api.setting().ENABLE) {
        lobbyspawn lobbyspawn = new lobbyspawn();
        lobbyspawn.onJoin(entity);
      }
    }
    BlockPos pos = entity.getPosition();
    float yaw = entity.rotationYaw, pitch = entity.rotationPitch;
    PlayerPos pp = new PlayerPos(pos, yaw, pitch);
    PlayerDescriptor dc = new PlayerDescriptor(entity, pp);
    descriptors.put(entity, dc);
    scheduler.schedule(() -> {
          if (descriptors.containsKey(entity)) {
            descriptors.remove(entity);
            logged.remove(entity);
            ((EntityPlayerMP)entity).connection.sendPacket((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString(WAKE_UP)));
          } 
        },AuthModConfig.delay, TimeUnit.SECONDS);
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLeave(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
    logged.remove(event.player);
  }
  
  @SubscribeEvent
  public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    if (descriptors.containsKey(event.player)) {
      PlayerPos pp = ((PlayerDescriptor)descriptors.get(event.player)).getPosition();
      BlockPos pos = pp.getPosition();
      ((EntityPlayerMP)event.player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), pp.getYaw(), pp.getPitch());
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onPlayerEvent(PlayerEvent event) {
    EntityPlayer entity = event.getEntityPlayer();
    if (descriptors.containsKey(entity) && event.isCancelable()) {
      event.setCanceled(true);
      entity.sendMessage((ITextComponent)new TextComponentString(WELCOME));
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onCommand(CommandEvent event) {
    String name = event.getCommand().getName();
    if (descriptors.containsKey(event.getSender()) && 
      !name.equals("register") && !name.equals("login") && !name.equals("logged") && event
      .getSender() instanceof EntityPlayer && event
      .isCancelable()) {
      event.setCanceled(true);
      event.getSender().sendMessage((ITextComponent)new TextComponentString(WELCOME));
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onChatEvent(ServerChatEvent event) {
    EntityPlayerMP entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      event.getPlayer().sendMessage((ITextComponent)new TextComponentString(WELCOME));
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTossEvent(ItemTossEvent event) {
    EntityPlayer entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.inventory.addItemStackToInventory(event.getEntityItem().getItem());
      event.getPlayer().sendMessage((ITextComponent)new TextComponentString(WELCOME));
    } 
  }
  
  private static void handleLivingEvents(LivingEvent event, Entity entity) {
    if (event.getEntity() instanceof EntityPlayer && event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.sendMessage((ITextComponent)new TextComponentString(WELCOME));
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingAttackEvent(LivingAttackEvent event) {
    handleLivingEvents((LivingEvent)event, event.getEntity());
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingDeathEvent(LivingDeathEvent event) {
    handleLivingEvents((LivingEvent)event, event.getEntity());
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
    handleLivingEvents((LivingEvent)event, event.getEntity());
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingHealEvent(LivingHealEvent event) {
    handleLivingEvents((LivingEvent)event, event.getEntity());
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingHurtEvent(LivingHurtEvent event) {
    handleLivingEvents((LivingEvent)event, event.getEntity());
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingSetTargetAttackEvent(LivingSetAttackTargetEvent event) {
    if (event.getTarget() instanceof EntityPlayer && descriptors.containsKey(event.getTarget()))
      ((EntityLiving)event.getEntityLiving()).setAttackTarget(null);
  }
  
  @SubscribeEvent
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals("authmod"))
      ConfigManager.sync("authmod", Config.Type.INSTANCE); 
  }
  
  public void authorizePlayer(EntityPlayer player) {
    logged.put(player, Boolean.valueOf(true));
    descriptors.remove(player);
  }
  
  public boolean isLogged(EntityPlayer player) {
    return ((Boolean)logged.getOrDefault(player, Boolean.valueOf(false))).booleanValue();
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\event\Handler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
