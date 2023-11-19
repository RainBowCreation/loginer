package net.rainbowcreation.loginer.guard.registration;

import net.rainbowcreation.loginer.exception.InvalidEmailException;
import net.rainbowcreation.loginer.exception.InvalidPasswordException;
import net.rainbowcreation.loginer.exception.PlayerAlreadyExistException;
import net.rainbowcreation.loginer.exception.RegistrationException;
import net.rainbowcreation.loginer.exception.WrongPasswordConfirmationException;
import net.rainbowcreation.loginer.guard.PlayerFactory;
import net.rainbowcreation.loginer.guard.datasource.FileDataSourceStrategy;
import net.rainbowcreation.loginer.guard.datasource.IDataSourceStrategy;
import net.rainbowcreation.loginer.guard.payload.IPayload;
import net.rainbowcreation.loginer.guard.payload.RegistrationPayload;
import net.rainbowcreation.loginer.model.IPlayer;
import java.nio.file.Paths;
import javax.validation.ConstraintViolation;

import org.mindrot.jbcrypt.BCrypt;

public class Registrator {
  private final IDataSourceStrategy dataSource;
  
  public Registrator() {
    this((IDataSourceStrategy)new FileDataSourceStrategy(Paths.get(System.getProperty("java.io.tmpdir"), new String[] { "authmod.csv" }).toFile()));
  }
  
  public Registrator(IDataSourceStrategy dataSourceStrategy) {
    this.dataSource = dataSourceStrategy;
  }
  
  public boolean register(RegistrationPayload payload) throws RegistrationException {
    if (payload != null) {
      if (payload.isValid()) {
        IPlayer player = PlayerFactory.createFromRegistrationPayload(payload);
        if (this.dataSource.exist(player))
          throw new PlayerAlreadyExistException(); 
        player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
        return this.dataSource.add(player);
      } 
      for (ConstraintViolation<IPayload> c : (Iterable<ConstraintViolation<IPayload>>)payload.getErrors()) {
        if (c.getPropertyPath().toString().equals("email"))
          throw new InvalidEmailException(); 
        if (c.getPropertyPath().toString().equals("passwordConfirmationMatches"))
          throw new WrongPasswordConfirmationException(); 
        if (c.getPropertyPath().toString().equals("password"))
          throw new InvalidPasswordException(); 
      } 
    } 
    return false;
  }
  
  public IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\registration\Registrator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */