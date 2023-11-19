package net.rainbowcreation.loginer.command;

import net.rainbowcreation.loginer.AuthMod;
import net.rainbowcreation.loginer.config.AuthModConfig;
import net.rainbowcreation.loginer.event.Handler;
import net.rainbowcreation.loginer.exception.InvalidEmailException;
import net.rainbowcreation.loginer.exception.InvalidPasswordException;
import net.rainbowcreation.loginer.exception.PlayerAlreadyExistException;
import net.rainbowcreation.loginer.exception.RegistrationException;
import net.rainbowcreation.loginer.exception.WrongPasswordConfirmationException;
import net.rainbowcreation.loginer.guard.datasource.IDataSourceStrategy;
import net.rainbowcreation.loginer.guard.payload.IPayload;
import net.rainbowcreation.loginer.guard.payload.RegistrationPayload;
import net.rainbowcreation.loginer.guard.registration.Registrator;
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

public class RegisterCommand implements ICommand {
  private static final Logger LOGGER = AuthMod.LOGGER;
  
  private final List<String> aliases;
  
  private final Registrator registrator;
  
  private final Handler handler;
  
  private final boolean emailRequired;
  
  public RegisterCommand(Handler handler, IDataSourceStrategy strategy, boolean emailRequired) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("reg");
    this.registrator = new Registrator(strategy);
    this.emailRequired = emailRequired;
  }
  
  public RegisterCommand(Handler handler, IDataSourceStrategy strategy) {
    this(handler, strategy, false);
  }

  @Override
  public String getName() {
    return "register";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return this.emailRequired ? AuthModConfig.i18n.registerUsage : AuthModConfig.i18n.registerAlternativeUsage;
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer)sender;
    String playerName = player.getDisplayNameString();
    LOGGER.info(String.format("%s is using /register", new Object[] { playerName }));
    if (args.length == (this.emailRequired ? 3 : 2)) {
      if (!this.handler.isLogged(player)) {
        try {
          LOGGER.info(String.format("Forging payload for player %s", new Object[] { playerName }));
          RegistrationPayload payload = createPayload(player, args);
          LOGGER.info(String.format("Registering payload for player %s", new Object[] { playerName }));
          this.registrator.register(payload);
          LOGGER.info(String.format("Authorizing player %s", new Object[] { playerName }));
          this.handler.authorizePlayer(player);
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.registerSuccess));
        } catch (ArrayIndexOutOfBoundsException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(getUsage(sender)));
        } catch (InvalidEmailException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.loginInvalidEmail));
        } catch (PlayerAlreadyExistException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.registerExist));
        } catch (WrongPasswordConfirmationException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.registerWrongPasswordConfirmation));
        } catch (InvalidPasswordException e) {
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.registerPasswordTooShort));
        } catch (RegistrationException e) {
          LOGGER.error(e.getMessage());
          sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.error));
        } 
      } else {
        sender.sendMessage((ITextComponent)new TextComponentString(AuthModConfig.i18n.registerAlreadyLogged));
      } 
    } else {
      sender.sendMessage((ITextComponent)new TextComponentString(getUsage(sender)));
    } 
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
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
  
  private RegistrationPayload createPayload(EntityPlayer player, String[] args) {
    return new RegistrationPayload((IPayload)LoginCommand.createPayload(this.emailRequired, player, args), this.emailRequired ? args[2] : args[1]);
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\command\RegisterCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */