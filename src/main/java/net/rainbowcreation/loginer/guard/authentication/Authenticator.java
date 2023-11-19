package net.rainbowcreation.loginer.guard.authentication;

import net.rainbowcreation.loginer.exception.BannedPlayerException;
import net.rainbowcreation.loginer.exception.InvalidEmailException;
import net.rainbowcreation.loginer.exception.LoginException;
import net.rainbowcreation.loginer.exception.PlayerNotFoundException;
import net.rainbowcreation.loginer.exception.WrongPasswordException;
import net.rainbowcreation.loginer.exception.WrongUsernameException;
import net.rainbowcreation.loginer.guard.datasource.IDataSourceStrategy;
import net.rainbowcreation.loginer.guard.payload.IPayload;
import net.rainbowcreation.loginer.guard.payload.LoginPayload;
import net.rainbowcreation.loginer.model.IPlayer;
import javax.validation.ConstraintViolation;

import org.mindrot.jbcrypt.BCrypt;

public class Authenticator {
  private final IDataSourceStrategy dataSource;
  
  public Authenticator(IDataSourceStrategy dataSourceStrategy) {
    this.dataSource = dataSourceStrategy;
  }
  
  public boolean login(LoginPayload payload) throws LoginException, InvalidEmailException {
    if (payload != null) {
      if (payload.isValid()) {
        IPlayer player = this.dataSource.find(payload.getEmail(), payload.getUsername());
        if (player == null)
          throw new PlayerNotFoundException(); 
        if (!player.getUsername().equals(payload.getUsername()))
          throw new WrongUsernameException();
        if (player.isBanned())
          throw new BannedPlayerException(); 
        boolean correct = BCrypt.checkpw(payload.getPassword(), player.getPassword());
        if (!correct)
          throw new WrongPasswordException(); 
        return true;
      } 
      for (ConstraintViolation<IPayload> c : (Iterable<ConstraintViolation<IPayload>>)payload.getErrors()) {
        if (c.getPropertyPath().toString().equals("email"))
          throw new InvalidEmailException(); 
        if (c.getPropertyPath().toString().equals("password"))
          throw new WrongPasswordException(); 
      } 
    } 
    return false;
  }
  
  IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\authentication\Authenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */