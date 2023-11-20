package net.rainbowcreation.loginer.command;

import net.rainbowcreation.loginer.Loginer;
import net.rainbowcreation.loginer.config.AuthModConfig;
import net.rainbowcreation.loginer.event.Handler;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

public class LoggedCommand implements ICommand {
  private static final Logger LOGGER = Loginer.LOGGER;
  
  private final List<String> aliases;
  
  private final Handler handler;
  
  private final String no;
  
  private final String yes;
  
  public LoggedCommand(Handler handler) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("logged?");
    this.yes = AuthModConfig.i18n.loggedYes;
    this.no = AuthModConfig.i18n.loggedNo;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    EntityPlayer player = (EntityPlayer)sender;
    LOGGER.info(String.format("%s is using /logged", new Object[] { player.getDisplayNameString() }));
    boolean logged = this.handler.isLogged(player);
    sender.sendMessage((ITextComponent)new TextComponentString(logged ? this.yes : this.no));
  }
  
  public int compareTo(ICommand iCommand) {
    return getName().compareTo(iCommand.getName());
  }

  @Override
  public String getName() {
    return "logged";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return AuthModConfig.i18n.loggedUsage;
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return new ArrayList<>();
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return true;
  }
}