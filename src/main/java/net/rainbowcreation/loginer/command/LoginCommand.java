package net.rainbowcreation.loginer.command;

import net.rainbowcreation.loginer.AuthMod;
import net.rainbowcreation.loginer.config.AuthModConfig;
import net.rainbowcreation.loginer.event.Handler;
import net.rainbowcreation.loginer.exception.BannedPlayerException;
import net.rainbowcreation.loginer.exception.InvalidEmailException;
import net.rainbowcreation.loginer.exception.LoginException;
import net.rainbowcreation.loginer.exception.PlayerNotFoundException;
import net.rainbowcreation.loginer.exception.WrongPasswordException;
import net.rainbowcreation.loginer.exception.WrongUsernameException;
import net.rainbowcreation.loginer.guard.authentication.Authenticator;
import net.rainbowcreation.loginer.guard.datasource.IDataSourceStrategy;
import net.rainbowcreation.loginer.guard.payload.LoginPayload;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements ICommand {
  private static final Logger LOGGER = AuthMod.LOGGER;
  
  private final List<String> aliases;
  
  private final Authenticator authenticator;
  
  private final Handler handler;
  
  private final boolean emailRequired;
  
  public LoginCommand(Handler handler, IDataSourceStrategy strategy) {
    this(handler, strategy, false);
  }
  
  public LoginCommand(Handler handler, IDataSourceStrategy strategy, boolean emailRequired) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("log");
    this.authenticator = new Authenticator(strategy);
    this.emailRequired = emailRequired;
  }

  @Override
  public String getName() {
    return "login";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return AuthModConfig.i18n.loginUsage;
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer)sender;
    LOGGER.info(String.format("%s is using /login", new Object[] { player.getDisplayNameString() }));
    if (args.length == (this.emailRequired ? 2 : 1)) {
      if (!this.handler.isLogged(player)) {
        LoginPayload payload = createPayload(this.emailRequired, player, args);
        LOGGER.info(payload.toString() + " is going to log in");
        try {
          if (this.authenticator.login(payload)) {
            this.handler.authorizePlayer(player);
            LOGGER.info(player.getDisplayNameString() + " authenticated");
          } 
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginSuccess));
        } catch (WrongUsernameException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginWrongUsername));
        } catch (WrongPasswordException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginWrongPassword));
        } catch (BannedPlayerException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginBanned));
        } catch (PlayerNotFoundException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(String.format(AuthModConfig.i18n.loginUnknown, new Object[] { player.getDisplayNameString() })));
        } catch (InvalidEmailException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginInvalidEmail));
        } catch (LoginException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.error));
          LOGGER.error(e.getMessage());
        } 
      } 
    } else {
      sender.sendMessage((ITextComponent)new TextComponentString(getUsage(sender)));
    } 
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
  
  static LoginPayload createPayload(boolean emailRequired, EntityPlayer player, String[] args) {
    LoginPayload payload = new LoginPayload();
    payload.setEmailRequired(emailRequired);
    payload.setEmail(emailRequired ? args[0] : null);
    payload.setPassword(emailRequired ? args[1] : args[0]);
    payload.setUsername(player.getDisplayNameString());
    return payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return new ArrayList<>();
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return true;
  }
  
  public int compareTo(ICommand iCommand) {
    return getName().compareTo(iCommand.getName());
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\command\LoginCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */