package net.rainbowcreation.loginer;

import net.minecraftforge.fml.client.config.GuiEditArrayEntries;
import net.rainbowcreation.api.API;
import net.rainbowcreation.api.utils.IString;
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

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]", dependencies = "after:rbclobbyspawn")
public class Main {
  public static Logger LOGGER = FMLLog.log;
  
  private Handler handler;
  
  private IDataSourceStrategy dataSourceStrategy;
  public static API api;
  public static IString iString;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) throws Exception {
    api = API.getInstance();
    iString = new IString();
    LOGGER = event.getModLog();
    api.setLogger(LOGGER);
    iString.header(Reference.NAME+":"+Reference.VERSION);
    switch (AuthModConfig.dataSourceStrategy) {
      case DATABASE:
        this.dataSourceStrategy = (IDataSourceStrategy)new DatabaseSourceStrategy(AuthModConfig.database.table, (IConnectionFactory)new ConnectionFactory(AuthModConfig.database.dialect, AuthModConfig.database.host, AuthModConfig.database.port, AuthModConfig.database.database, AuthModConfig.database.user, AuthModConfig.database.password));
        LOGGER.info("Now using DatabaseSourceStrategy.");
        return;
      case FILE:
        this.dataSourceStrategy = (IDataSourceStrategy)new FileDataSourceStrategy(Paths.get(event.getModConfigurationDirectory().getAbsolutePath(), new String[] { Reference.NAME+"_players.csv" }).toFile());
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
        LOGGER.info("Registering loginer event handler");
        MinecraftForge.EVENT_BUS.register(this.handler);
        LOGGER.info("Registering loginer /login command");
        event.registerServerCommand((ICommand)new LoginCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
        LOGGER.info("Registering loginer /logged command");
        event.registerServerCommand((ICommand)new LoggedCommand(this.handler));
      } 
      if (AuthModConfig.enableRegistration) {
        LOGGER.info("Registering loginer /register command");
        event.registerServerCommand((ICommand)new RegisterCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
      } 
    } 
  }
}