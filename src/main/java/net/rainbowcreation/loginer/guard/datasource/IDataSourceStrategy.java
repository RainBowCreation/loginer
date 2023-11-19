package net.rainbowcreation.loginer.guard.datasource;

import net.rainbowcreation.loginer.exception.RegistrationException;
import net.rainbowcreation.loginer.model.IPlayer;

public interface IDataSourceStrategy {
  IPlayer find(String paramString1, String paramString2);
  
  boolean add(IPlayer paramIPlayer) throws RegistrationException;
  
  boolean exist(IPlayer paramIPlayer);
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\datasource\IDataSourceStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */