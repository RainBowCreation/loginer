package net.rainbowcreation.loginer;

import net.rainbowcreation.loginer.command.LoggedCommand;
import net.rainbowcreation.loginer.command.LoginCommand;
import net.rainbowcreation.loginer.command.RegisterCommand;
import net.rainbowcreation.loginer.config.AuthModConfig;
import net.rainbowcreation.loginer.event.Handler;
import net.rainbowcreation.loginer.guard.datasource.DatabaseSourceStrategy;
import net.rainbowcreation.loginer.guard.datasource.FileDataSourceStrategy;
import net.rainbowcreation.loginer.guard.datasource.IDataSourceStrategy;
import net.rainbowcreation.loginer.guard.datasource.db.ConnectionFactory;
import net.rainbowcreation.loginer.guard.datasource.db.IConnectionFactory;
import java.nio.file.Paths;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.rainbowcreation.loginer.utils.Reference;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]")
public class AuthMod {
  public static final String MODID = "authmod";
  
  static final String NAME = "AuthMod";
  
  static final String VERSION = "3.2";
  
  public static Logger LOGGER = FMLLog.log;
  
  private Handler handler;
  
  private IDataSourceStrategy dataSourceStrategy;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) throws Exception {
    LOGGER = event.getModLog();
    switch (AuthModConfig.dataSourceStrategy) {
      case DATABASE:
        this.dataSourceStrategy = (IDataSourceStrategy)new DatabaseSourceStrategy(AuthModConfig.database.table, (IConnectionFactory)new ConnectionFactory(AuthModConfig.database.dialect, AuthModConfig.database.host, AuthModConfig.database.port, AuthModConfig.database.database, AuthModConfig.database.user, AuthModConfig.database.password));
        LOGGER.info("Now using DatabaseSourceStrategy.");
        return;
      case FILE:
        this
          
          .dataSourceStrategy = (IDataSourceStrategy)new FileDataSourceStrategy(Paths.get(event.getModConfigurationDirectory().getAbsolutePath(), new String[] { "authmod_players.csv" }).toFile());
        LOGGER.info("Now using FileDataSourceStrategy.");
        return;
    } 
    this.dataSourceStrategy = null;
    LOGGER.info("Unknown guard strategy selected. Nothing will happen.");
  }
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    if (AuthModConfig.dataSourceStrategy != null) {
      this.handler = new Handler();
      if (AuthModConfig.enableAuthentication) {
        LOGGER.info("Registering AuthMod event handler");
        MinecraftForge.EVENT_BUS.register(this.handler);
        LOGGER.info("Registering AuthMod /login command");
        event.registerServerCommand((ICommand)new LoginCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
        LOGGER.info("Registering AuthMod /logged command");
        event.registerServerCommand((ICommand)new LoggedCommand(this.handler));
      } 
      if (AuthModConfig.enableRegistration) {
        LOGGER.info("Registering AuthMod /register command");
        event.registerServerCommand((ICommand)new RegisterCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
      } 
    } 
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\AuthMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */